package com.yulgnier.common.minio;


import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 通用MinIO工具类（Spring Boot+yulgnier+minio8.3.0集成版）
 * yulgnier设计思想：
 * 1. 仅声明方法需要的核心成员变量，至于怎么实现看改造者
 * 2. 所有方法做异常捕获，打印中文提示并抛异常，后续怎么做异常处理看改造者
 */
@Component
public class MinioUtils {

    // ======================== 核心成员变量（这里使用配置类注入） ========================

    private final MinioClient minioClient;  // MinIO客户端对象，这里先占一个位置

    private final String defaultBucket;    // 默认桶名，这里占位
    private final MinIOProperties minIOProperties;

    @Autowired
    public MinioUtils(MinioConfiguration minioConfiguration, MinIOProperties minIOProperties) {
        // 从配置类获取已初始化的MinioClient（自带所有连接信息，开箱即用）
        this.minioClient = minioConfiguration.minioClient();
        // 从配置属性类读取默认桶名（业务层面的兜底配置）
        this.defaultBucket = minIOProperties.getBucketName();
        this.minIOProperties = minIOProperties; // 新增：赋值MinIO配置（获取endpoint）
    }

    // ======================== 功能1：列出所有Bucket ========================

    /**
     * 【基础功能】列出MinIO服务器上所有的存储桶
     *
     * @return List<String> 所有桶名列表（无桶/异常时返回空列表，不返回null）
     * @注意事项： 1. 异常仅打印提示，不抛异常，使用者自己修改
     * 2. 返回空列表而非null，符合Java集合使用规范（避免空指针）
     */
    public List<String> listAllBuckets() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 初始化返回列表（默认空列表，避免返回null）
        List<String> bucketNames = new ArrayList<>();
        try {
            // 前置检查：确保MinioClient已正确注入
            checkClient();
            // 核心API：调用MinIO客户端获取所有桶信息
            List<Bucket> buckets = minioClient.listBuckets();
            // 遍历桶列表，提取桶名（只返回业务关心的桶名，不返回原始Bucket对象）
            for (Bucket bucket : buckets) {
                bucketNames.add(bucket.name());
            }
        } catch (Exception e) {
            // 异常处理：中文提示+具体原因，方便排查问题
            System.err.println("【MinIO异常】列出所有存储桶失败：" + e.getMessage());
            throw e;
        }
        return bucketNames;
    }

    // ======================== 功能2：判断单个Bucket是否存在 ========================

    /**
     * 【基础功能】判断指定Bucket是否存在
     *
     * @param bucketName 桶名（必填，空值/全空格会直接返回false并提示）
     * @return boolean 存在返回true，不存在/参数非法/异常返回false
     * @参数约束： 1. bucketName不可为空/全空格，否则打印提示并返回false
     * 2. 自动去除桶名首尾空格（避免因空格导致的"桶不存在"误判）
     */
    public boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：参数合法性校验（前置拦截无效参数，避免调用MinIO API浪费资源）
        if (bucketName == null || bucketName.trim().isEmpty()) {
            System.err.println("【MinIO异常】判断桶是否存在失败：桶名不能为空/全空格");
            return false;
        }
        try {
            // 前置检查：确保MinioClient可用
            checkClient();
            // 核心API：调用MinIO客户端判断桶是否存在（传入去空格后的桶名）
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName.trim()) // 去空格：避免" test-bucket "和"test-bucket"判为不同桶
                            .build()
            );
        } catch (Exception e) {
            // 异常兜底：无论什么异常，都返回false，同时打印具体原因
            System.err.println("【MinIO异常】判断桶[" + bucketName + "]是否存在失败：" + e.getMessage());
            throw e;
        }
    }

    // ======================== 功能3：批量创建私有Bucket ========================

    /**
     * 【核心功能】批量创建私有Bucket（生产环境推荐权限）
     *
     * @param bucketNames 可变参数（支持传1个/多个桶名，如：createBuckets("user", "order")）
     * @ 核心逻辑： 1. 空参数校验：未传桶名直接提示，不执行后续逻辑
     * 2. 单桶处理：遍历桶名，逐个判断+创建
     * 3. 存在则提示：不报错，符合"幂等性"设计（重复调用不影响结果）
     * 4. 不存在则创建：默认创建私有桶（MinIO默认权限，无需额外配置）
     * @ 注意事项： - MinIO桶名规则：仅支持小写字母、数字、横线（-），不支持大写/空格/中文
     */
    public void createBuckets(String... bucketNames) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 空参数校验：可变参数为null/空数组时，直接提示并返回
        if (bucketNames == null || bucketNames.length == 0) {
            System.err.println("【MinIO异常】创建桶失败：未指定任何桶名");
            return;
        }

        try {
            checkClient();
            // 遍历所有待创建的桶名，逐个处理（批量操作拆分为单桶操作，便于定位问题）
            for (String bucketName : bucketNames) {
                // 空桶名过滤：单个桶名为null/空，跳过并提示
                String targetBucket = bucketName == null ? "" : bucketName.trim();
                if (targetBucket.isEmpty()) {
                    System.err.println("【MinIO提示】跳过创建空桶名（传入的桶名可能为null/全空格）");
                    continue;
                }

                // 第一步：判断桶是否已存在
                if (bucketExists(targetBucket)) {
                    System.out.println("【MinIO提示】桶[" + targetBucket + "]已存在，无需重复创建");
                    continue;
                }

                // 第二步：创建私有桶（MinIO默认创建私有桶，无需额外权限配置）
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(targetBucket)
                                .build()
                );
                System.out.println("【MinIO提示】桶[" + targetBucket + "]创建成功（默认私有权限，仅桶拥有者可写）");
            }
        } catch (Exception e) {
            System.err.println("【MinIO异常】批量创建桶失败：" + e.getMessage());
            throw e;
        }
    }

    // ======================== 功能4：文件上传（自动生成唯一文件名） ========================

    /**
     * 【核心功能】文件上传（自动生成唯一文件名，无需前端传路径）
     * 👉 核心场景：前端上传任意文件，后端自动生成唯一名称存储，避免文件覆盖
     *
     * @param file       前端上传的文件流（Spring Boot接收上传文件的标准对象，必填）
     * @param bucketName 可选参数：目标桶名（不传则使用配置文件的默认桶）
     * @return String     生成的唯一文件名（UUID+原文件名，失败返回null）
     * @ 核心逻辑： 1. 参数校验：拦截空文件，避免无效上传
     * 2. 桶名处理：优先使用传入桶名，无则用默认桶
     * 3. 唯一文件名生成：UUID + 原文件名（解决文件覆盖问题）
     * 4. 桶自动创建：上传前检查桶是否存在，不存在则自动创建
     * 5. 流上传：使用try-with-resources自动关闭流，避免内存泄漏
     * 6. ContentType：固定使用文件本身的ContentType，适配浏览器展示逻辑
     * @ 注意事项： - 原文件名为null时，自动补充"unknown-file"作为默认名
     * - 生成的文件名格式：UUID-原文件名（如：123e4567-e89b-12d3-a456-426614174000-test.jpg）
     */
    public String uploadFile(MultipartFile file, String... bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：核心参数校验（文件流为空直接返回null）
        if (file == null || file.isEmpty()) {
            System.err.println("【MinIO异常】文件上传失败：文件流为空（前端可能未传文件）");
            return null;
        }

        // 第二步：确定目标桶名（封装为私有方法，复用逻辑）
        String targetBucket = getTargetBucket(bucketName);
        if (targetBucket == null) { // 桶名非法时直接返回null
            return null;
        }

        // 第三步：生成唯一文件名（UUID + 原文件名，避免覆盖）
        String originalFileName = file.getOriginalFilename();
        // 处理原文件名为null的边界情况
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            originalFileName = "unknown-file";
        }
        // 生成UUID（去掉横线，缩短长度） + 原文件名，拼接为唯一名称
        String uniqueFileName = java.util.UUID.randomUUID().toString().replace("-", "") + "-" + originalFileName;

        // 第四步：核心上传逻辑（try-with-resources自动关闭流）
        try (InputStream inputStream = file.getInputStream()) {
            checkClient();

            // 前置操作：桶不存在则自动创建（避免上传时桶不存在报错）
            if (!bucketExists(targetBucket)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(targetBucket).build());
                System.out.println("【MinIO提示】上传文件时，桶[" + targetBucket + "]不存在，已自动创建（私有权限）");
            }

            // 构建上传参数：核心配置
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(targetBucket)          // 目标桶
                    .object(uniqueFileName)        // 自动生成的唯一文件名
                    .stream(inputStream, file.getSize(), -1) // 文件流+大小（-1表示自动适配分片）
                    .contentType(file.getContentType()); // 固定使用文件本身的ContentType

            // 执行上传：调用MinIO客户端完成文件存储
            minioClient.putObject(builder.build());
            System.out.println("【MinIO提示】文件上传成功，唯一文件名：" + uniqueFileName + "，存储桶：" + targetBucket);

            // 返回生成的唯一文件名
            return uniqueFileName;
        } catch (Exception e) {
            System.err.println("【MinIO异常】文件上传失败，生成的文件名：" + uniqueFileName + "，原因：" + e.getMessage());
            throw e;
        }
    }

    // ======================== 功能5：生成带时效的预签名URL ========================

    /**
     * 【核心功能】生成私有文件的临时访问URL（过期自动失效，生产环境安全方案）
     * 👉 核心场景：前端展示私有图片/下载私有文档，避免文件链接永久有效
     *
     * @param uniqueFileName 唯一文件路径（与上传时一致，如：user/avatar/123-uuid.jpg）
     * @param expireTime     有效时间数值（必须>0，如：1表示1小时/1分钟，配合timeUnit使用）
     * @param timeUnit       有效时间单位（如TimeUnit.HOURS=小时，TimeUnit.MINUTES=分钟）
     * @param bucketName     可选参数：目标桶名（不传则用默认桶）
     * @return String 临时访问URL（失败/参数非法返回null）
     * @ 核心逻辑： 1. 严格参数校验：拦截无效时间/空路径，避免生成无效URL
     * 2. 文件存在校验：生成URL前检查文件是否存在，避免返回"能生成但访问404"的URL
     * 3. 预签名URL：基于MinIO签名机制，URL包含时效和权限信息，过期失效
     * @ 注意事项： - expireTime建议不超过24小时（避免URL有效期过长导致安全风险）
     * - 预签名URL仅包含"读权限"（Method.GET），无法修改/删除文件
     */
    public String getPresignedUrl(String uniqueFileName, int expireTime, TimeUnit timeUnit, String... bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：参数合法性校验（路径为空）
        if (uniqueFileName == null || uniqueFileName.trim().isEmpty()) {
            System.err.println("【MinIO异常】生成预签名URL失败：唯一文件路径不能为空");
            return null;
        }
        // 第二步：时间参数校验（有效时间必须>0）
        if (expireTime <= 0) {
            System.err.println("【MinIO异常】生成预签名URL失败：有效时间必须大于0（建议传1-24）");
            return null;
        }
        // 第三步：时间单位校验（不能为空）
        if (timeUnit == null) {
            System.err.println("【MinIO异常】生成预签名URL失败：时间单位不能为空（如TimeUnit.HOURS）");
            return null;
        }
        // 第四步：确定目标桶名
        String targetBucket = getTargetBucket(bucketName);
        if (targetBucket == null) {
            return null;
        }

        // 第五步：生成预签名URL
        try {
            checkClient();

            // 前置校验：检查文件是否存在（避免生成"无效URL"）
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(targetBucket)
                            .object(uniqueFileName)
                            .build()
            );

            // 核心API：生成带时效的GET权限URL（仅允许读，不允许写/删）
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)          // 权限类型：仅读
                            .bucket(targetBucket)        // 目标桶
                            .object(uniqueFileName)      // 唯一文件路径
                            .expiry(expireTime, timeUnit) // 时效配置（如1小时）
                            .build()
            );
        } catch (Exception e) {
            System.err.println("【MinIO异常】生成预签名URL失败，路径：" + uniqueFileName + "，原因：" + e.getMessage());
            throw e;
        }
    }

    // ======================== 功能6：批量删除Bucket ========================

    /**
     * 【运维功能】批量删除Bucket（非空桶自动清空后删除）
     * 👉 核心场景：系统清理、租户注销时删除专属桶
     *
     * @param bucketNames 可变参数：待删除的桶名（支持传1个/多个）
     * @ 核心逻辑： 1. 空参数校验：未传桶名直接提示
     * 2. 单桶处理：遍历桶名，逐个判断+删除
     * 3. 不存在则提示：不报错，符合幂等性设计
     * 4. 非空桶处理：先清空桶内所有文件，再删除桶（MinIO强制要求桶为空才能删除）
     * @ 注意事项： - 删除操作不可逆，建议业务层增加二次确认逻辑
     * - 清空文件时遍历所有子目录（recursive=true），避免遗漏文件导致删桶失败
     */
    public void deleteBuckets(String... bucketNames) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 空参数校验
        if (bucketNames == null || bucketNames.length == 0) {
            System.err.println("【MinIO异常】删除桶失败：未指定任何桶名");
            return;
        }

        try {
            checkClient();
            // 遍历待删除桶名，逐个处理
            for (String bucketName : bucketNames) {
                String targetBucket = bucketName == null ? "" : bucketName.trim();
                // 空桶名过滤
                if (targetBucket.isEmpty()) {
                    System.err.println("【MinIO提示】跳过删除空桶名（传入的桶名可能为null/全空格）");
                    continue;
                }

                // 第一步：判断桶是否存在
                if (!bucketExists(targetBucket)) {
                    System.out.println("【MinIO提示】桶[" + targetBucket + "]不存在，无需删除");
                    continue;
                }

                // 第二步：清空桶内所有文件（MinIO非空桶无法删除）
                // listObjects：递归列出桶内所有文件（包括子目录，recursive=true）
                Iterable<Result<Item>> items = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(targetBucket)
                                .recursive(true) // 递归遍历所有子目录
                                .build()
                );
                // 遍历并删除每个文件
                for (Result<Item> item : items) {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(targetBucket)
                                    .object(item.get().objectName()) // 文件路径
                                    .build()
                    );
                }

                // 第三步：删除空桶
                minioClient.removeBucket(
                        RemoveBucketArgs.builder()
                                .bucket(targetBucket)
                                .build()
                );
                System.out.println("【MinIO提示】桶[" + targetBucket + "]已清空并删除成功");
            }
        } catch (Exception e) {
            System.err.println("【MinIO异常】批量删除桶失败：" + e.getMessage());
            throw e;
        }
    }

// ======================== 功能7：设置单个文件/整个桶的权限（private/public） ========================

    /**
     * 【核心功能】设置单个文件或整个桶的权限（仅支持private/public两种核心权限）
     * 👉 核心场景：批量开放桶内所有文件访问权限、单独设置某个私密文件权限
     *
     * @param bucketName     可选参数：目标桶名（不传则用默认桶）
     * @param uniqueFileName 可选参数：唯一文件名（不传/空则默认设置整个桶的权限）
     * @param permission     必选参数：权限类型（仅支持"private"或"public"，不区分大小写）
     * @return boolean 设置成功返回true，参数非法/桶不存在/异常返回false
     * @ 核心逻辑： 1. 严格参数校验：权限类型非法直接返回false，桶不存在直接返回false
     * 2. 权限映射：public=公共只读（所有人可访问），private=私有（仅桶拥有者可访问）
     * 3. 桶/文件区分：文件路径为空则设置桶权限，否则设置单个文件权限
     * @ 注意事项： - MinIO 8.3.0通过Policy控制权限，public权限为只读访问（无写/删权限）
     * - 设置桶public后，桶内所有文件默认继承公共权限（无需逐个设置）
     */
    public boolean setPermission(String bucketName, String uniqueFileName, String permission) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：权限类型参数校验（核心必选参数）
        if (permission == null || !(permission.trim().equalsIgnoreCase("private") || permission.trim().equalsIgnoreCase("public"))) {
            System.err.println("【MinIO异常】设置权限失败：权限类型仅支持private/public（不区分大小写），当前传入：" + permission);
            return false;
        }
        String targetPermission = permission.trim().toLowerCase(); // 统一转为小写，方便后续判断

        // 第二步：获取目标桶名并校验合法性
        String targetBucket = getTargetBucket(bucketName);
        if (targetBucket == null) {
            return false;
        }

        // 第三步：检查桶是否存在（桶不存在则无需后续操作）
        if (!bucketExists(targetBucket)) {
            System.err.println("【MinIO异常】设置权限失败：桶[" + targetBucket + "]不存在");
            return false;
        }

        // 第四步：处理文件路径（统一去空格，空则设置桶权限）
        String targetFileName = uniqueFileName == null ? "" : uniqueFileName.trim();

        try {
            checkClient();
            // 分支1：设置整个桶的权限
            if (targetFileName.isEmpty()) {
                setBucketPermission(targetBucket, targetPermission);
            }
            // 分支2：设置单个文件的权限
            else {
                setObjectPermission(targetBucket, targetFileName, targetPermission);
            }
            System.out.println("【MinIO提示】权限设置成功：桶[" + targetBucket + "]" + (targetFileName.isEmpty() ? "" : "文件[" + targetFileName + "]") + " → " + targetPermission);
            return true;
        } catch (Exception e) {
            System.err.println("【MinIO异常】设置权限失败：桶[" + targetBucket + "]" + (targetFileName.isEmpty() ? "" : "文件[" + targetFileName + "]") + "，权限：" + targetPermission + "，原因：" + e.getMessage());
            throw e;
        }
    }

    /**
     * 【内部工具】设置桶的权限（private/public）
     */
    private void setBucketPermission(String bucketName, String permission) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 构建桶权限策略：public=只读公共访问，private=移除公共策略（恢复私有）
        String policyJson;
        if ("public".equals(permission)) {
            // 公共只读策略（MinIO 8.3.0标准桶只读策略）
            policyJson = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": [\"s3:GetObject\"],\n" +
                    "      \"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        } else {
            // private权限：清空桶策略（恢复私有）
            policyJson = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": []\n" +
                    "}";
        }

        // 执行桶权限设置（MinIO 8.3.0 SetBucketPolicyArgs）
        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build()
        );
    }

    /**
     * 【内部工具】设置单个文件的权限（private/public）
     */
    private void setObjectPermission(String bucketName, String objectName, String permission) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 先校验文件是否存在
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("文件[" + objectName + "]不存在，无法设置权限：" + e.getMessage(), e);
        }

        // 构建文件权限策略：public=只读公共访问，private=移除公共策略
        String policyJson;
        if ("public".equals(permission)) {
            policyJson = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": [\"s3:GetObject\"],\n" +
                    "      \"Resource\": [\"arn:aws:s3:::" + bucketName + "/" + objectName + "\"]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        } else {
            policyJson = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": []\n" +
                    "}";
        }

        // 执行文件权限设置（MinIO 8.3.0 需先设置桶策略覆盖到单个文件）
        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build()
        );
    }

//  ==================================功能8：获取通用非签名URL===================================

    /**
     * 【基础功能】获取通用非签名URL（适用于公共权限文件的直接访问）
     * 👉 核心场景：公共权限文件无需签名，直接拼接endpoint+桶名+文件名访问
     *
     * @param uniqueFileName 唯一文件名（必填，空值/全空格返回null并提示）
     * @param bucketName     可选参数：目标桶名（不传则使用配置文件的默认桶）
     * @return String 非签名访问URL（参数非法/桶不存在/异常返回null）
     * @ 核心逻辑： 1. 严格参数校验：拦截空文件路径，避免生成无效URL
     * 2. 桶存在校验：生成URL前检查桶是否存在，避免返回"能生成但访问404"的URL
     * 3. URL拼接：endpoint + 桶名 + 唯一文件名（自动处理endpoint结尾的/，避免重复）
     */
    public String getPublicUrl(String uniqueFileName, String... bucketName) {
        // 第一步：参数合法性校验（唯一文件名不能为空）
        if (uniqueFileName == null || uniqueFileName.trim().isEmpty()) {
            System.err.println("【MinIO异常】生成非签名URL失败：唯一文件名不能为空/全空格");
            return null;
        }
        String targetFileName = uniqueFileName.trim();

        // 第二步：确定目标桶名并校验合法性
        String targetBucket = getTargetBucket(bucketName);
        if (targetBucket == null) {
            return null;
        }

        try {
            checkClient();
            // 第三步：校验桶是否存在
            if (!bucketExists(targetBucket)) {
                System.err.println("【MinIO异常】生成非签名URL失败：桶[" + targetBucket + "]不存在");
                return null;
            }

            // 第四步：拼接非签名URL（处理endpoint结尾的/，避免重复）
            String endpoint = minIOProperties.getEndpoint();
            // 去除endpoint结尾的/，避免拼接后出现 http://127.0.0.1:9000//bucket/filename 这种情况
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }
            // 拼接规则：endpoint + / + 桶名 + / + 唯一文件名
            String publicUrl = endpoint + "/" + targetBucket + "/" + targetFileName;

            System.out.println("【MinIO提示】生成非签名URL成功：" + publicUrl);
            return publicUrl;
        } catch (Exception e) {
            System.err.println("【MinIO异常】生成非签名URL失败，文件名：" + targetFileName + "，桶名：" + targetBucket + "，原因：" + e.getMessage());
            return null;
        }
    }


//     ==================================功能9：删除文件对象===================================

    /**
     * 【核心功能】批量删除指定桶内的文件对象
     * 👉 核心场景：清理过期文件、用户删除上传的文件等批量文件删除操作
     *
     * @param uniqueFileNames 唯一文件名的集合（必填，空集合/Null会直接返回false并提示）
     * @param bucketName      可选参数：目标桶名（不传则使用配置文件的默认桶）
     * @return boolean 全部删除成功返回true，参数非法/桶不存在/部分/全部删除失败返回false
     * @ 核心逻辑： 1. 严格参数校验：拦截空集合/Null，避免无效删除操作
     * 2. 桶存在校验：删除前检查桶是否存在，避免操作不存在的桶
     * 3. 批量处理：遍历文件名集合，逐个校验+删除，单个文件失败不影响整体（但最终返回false）
     * 4. 幂等性设计：删除不存在的文件时，仅打印提示，不视为失败（重复删除不报错）
     */
    public boolean deleteObjects(Set<String> uniqueFileNames, String... bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：参数合法性校验（集合为空/Null直接返回false）
        if (uniqueFileNames == null || uniqueFileNames.isEmpty()) {
            System.err.println("【MinIO异常】批量删除文件失败：待删除的文件名集合不能为空/Null");
            return false;
        }

        // 第二步：确定目标桶名并校验合法性
        String targetBucket = getTargetBucket(bucketName);
        if (targetBucket == null) {
            return false;
        }

        // 第三步：检查桶是否存在
        if (!bucketExists(targetBucket)) {
            System.err.println("【MinIO异常】批量删除文件失败：桶[" + targetBucket + "]不存在");
            return false;
        }

        boolean isAllSuccess = true;
        try {
            checkClient();
            // 遍历文件名集合，逐个删除
            for (String fileName : uniqueFileNames) {
                // 单个文件名校验：空/全空格跳过并标记失败
                String targetFileName = fileName == null ? "" : fileName.trim();
                if (targetFileName.isEmpty()) {
                    System.err.println("【MinIO提示】跳过删除空文件名（传入的文件名可能为null/全空格）");
                    isAllSuccess = false;
                    continue;
                }

                try {
                    // 先校验文件是否存在（不存在则打印提示，不抛异常）
                    minioClient.statObject(
                            StatObjectArgs.builder()
                                    .bucket(targetBucket)
                                    .object(targetFileName)
                                    .build()
                    );
                    // 核心API：删除文件
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(targetBucket)
                                    .object(targetFileName)
                                    .build()
                    );
                    System.out.println("【MinIO提示】文件删除成功：桶[" + targetBucket + "]，文件名：" + targetFileName);
                } catch (Exception e) {
                    // 文件不存在或删除失败时，标记整体失败并打印提示
                    System.err.println("【MinIO异常】删除文件失败：桶[" + targetBucket + "]，文件名：" + targetFileName + "，原因：" + e.getMessage());
                    isAllSuccess = false;
                }
            }
        } catch (Exception e) {
            // 全局异常兜底：打印异常信息并标记失败
            System.err.println("【MinIO异常】批量删除文件时发生全局异常：" + e.getMessage());
            throw e;
        }

        return isAllSuccess;
    }


    // ======================== 私有辅助方法（工具类内部使用） ========================

    /**
     * 【内部工具】获取目标桶名（处理可选参数，兜底默认桶）
     * 👉 封装桶名处理逻辑，避免重复代码，提高可维护性
     *
     * @param bucketName 可选桶名（可变参数）
     * @return String 合法的目标桶名（非法返回null）
     */
    private String getTargetBucket(String... bucketName) {
        // 优先级：传入桶名 > 默认桶名
        String targetBucket = (bucketName != null && bucketName.length > 0
                && bucketName[0] != null && !bucketName[0].trim().isEmpty())
                ? bucketName[0].trim() // 传入桶名有效，使用传入的
                : this.defaultBucket;  // 传入桶名无效，使用默认桶

        // 最终校验：桶名不能为空（默认桶也可能未配置）
        if (targetBucket == null || targetBucket.trim().isEmpty()) {
            System.err.println("【MinIO异常】目标桶名不能为空（默认桶未配置，且未指定传入桶名）");
            return null;
        }
        return targetBucket;
    }

    /**
     * 【内部工具】检查MinioClient是否初始化完成
     * 👉 前置校验，避免调用MinIO API时出现空指针异常
     *
     * @throws RuntimeException 客户端未初始化时抛出异常（快速失败）
     */
    private void checkClient() {
        if (this.minioClient == null) {
            throw new RuntimeException("""
                    【MinIO致命异常】MinioClient未初始化！请检查：
                    1. MinioConfiguration配置类是否正确注入
                    2. MinIOProperties配置是否完整（endpoint/AK/SK）""");
        }
    }
}