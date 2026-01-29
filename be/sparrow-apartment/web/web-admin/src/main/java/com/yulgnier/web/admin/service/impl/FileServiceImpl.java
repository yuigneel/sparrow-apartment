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

@Service
public class FileServiceImpl implements FileService {

    private final MinioUtils minioUtils;

    @Autowired
    public FileServiceImpl(MinioUtils minioUtils) {
        this.minioUtils = minioUtils;
    }
    /**
     * 上传文件
     * @param file MultipartFile 对象
     * @return UUID+文件名
     * yulgnier和老师的代码有差异，这里只储存唯一文件名，到时候用文件名来获取文件签名url
     */
    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 第一步：确认bucket
        minioUtils.createBuckets("sparrow-apartment");
        //第二步：上传文件，获得文件名
        String fileName = minioUtils.uploadFile(file, "sparrow-apartment");
        //第三步：返回临时url
        return minioUtils.getPresignedUrl(fileName, 1, java.util.concurrent.TimeUnit.DAYS, "sparrow-apartment");
    }
}
