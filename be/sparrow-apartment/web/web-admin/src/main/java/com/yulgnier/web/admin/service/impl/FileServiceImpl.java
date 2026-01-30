package com.yulgnier.web.admin.service.impl;

import com.yulgnier.common.minio.MinioUtils;
import com.yulgnier.web.admin.service.FileService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    private final MinioUtils minioUtils;

    @Autowired
    public FileServiceImpl(MinioUtils minioUtils) {
        this.minioUtils = minioUtils;
    }

    /**
     * 上传文件
     *
     * @param file MultipartFile 对象
     * @return UUID+文件名
     * yulgnier和老师的代码有差异，原本是直接返回永久链接，yulgnier第一个个想法是返回唯一文件名，到时候它调用的提取数据库文件名，处理一下返回临时链接
     * 但是发现演示的时候前端还得改，麻烦，但是我又不想改bucket的权限了，于是在环境变量中放宽了临时链接的过期时间，所以这里直接返回伪永久链接
     * setx _MINIO_MAX_PRESIGNED_URL_EXPIRY 31536000
     * 但是，但是，发现这个方法搞不定，于是又新增了一个设置对象权限的方法，还是设置为公共读权限
     */
    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：确认bucket
        minioUtils.createBuckets("sparrow-apartment");
        //第二步：上传文件，获得文件名
        String fileName = minioUtils.uploadFile(file, "sparrow-apartment");
        //第三步：返回永久url ###废弃###
        //return minioUtils.getPresignedUrl(fileName, 365, TimeUnit.DAYS, "sparrow-apartment");
        //第三步：返回永久的url
        return minioUtils.getPublicUrl(fileName);
    }
}

