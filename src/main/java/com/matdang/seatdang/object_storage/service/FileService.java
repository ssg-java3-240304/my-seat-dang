package com.matdang.seatdang.object_storage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.matdang.seatdang.object_storage.model.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }


//    public List<FileDto> uploadFilesSample(List<MultipartFile> multipartFiles,String folderName){
//
//        return uploadFiles(multipartFiles, folderName);
//    }

//    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
//    public List<FileDto> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {
//
//        List<FileDto> s3files = new ArrayList<>();
//
//        for (MultipartFile multipartFile : multipartFiles) {
//
//            String originalFileName = multipartFile.getOriginalFilename();
//            String uploadFileName = getUuidFileName(originalFileName);
//            String uploadFileUrl = "";
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(multipartFile.getSize());
//            objectMetadata.setContentType(multipartFile.getContentType());
//
//            try (InputStream inputStream = multipartFile.getInputStream()) {
//
//                String keyName = filePath + "/" + uploadFileName;
//
//                // S3에 폴더 및 파일 업로드
//                amazonS3Client.putObject(
//                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
//                                .withCannedAcl(CannedAccessControlList.PublicRead));
//
//                // S3에 업로드한 폴더 및 파일 URL
//                uploadFileUrl = "https://kr.object.ncloudstorage.com/"+ bucketName + "/" + keyName;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            s3files.add(
//                    FileDto.builder()
//                            .originalFileName(originalFileName)
//                            .uploadFileName(uploadFileName)
//                            .uploadFilePath(filePath)
//                            .uploadFileUrl(uploadFileUrl)
//                            .build());
//        }
//
//        return s3files;
//    }


    // 여러개 파일 올릴 때 사용
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {

        List<String> uploadFileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {

                String keyName = filePath + "/" + uploadFileName;

                // S3에 폴더 및 파일 업로드
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadFileUrls.add(uploadFileUrl);
        }

        return uploadFileUrls;
    }

    // 하나의 파일만 올릴 때 사용
    public String uploadSingleFile(MultipartFile multipartFile, String filePath) {

        String uploadFileUrl = "";

        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = filePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFileUrl;
    }






    //현재 업로드한 이미지들 모두 불러오기
    public List<FileDto> listFiles(String filePath) {
        List<FileDto> files = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(filePath + "/");
        ListObjectsV2Result result;

        do {
            result = amazonS3Client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String key = objectSummary.getKey();
                String fileName = key.substring(key.lastIndexOf("/") + 1);

                files.add(FileDto.builder()
                        .originalFileName(fileName)
                        .uploadFileName(fileName)
                        .uploadFilePath(filePath) // 현재 지정한 폴더만 가져옴
                        .uploadFileUrl("https://kr.object.ncloudstorage.com/" + bucketName + "/" + key)
                        .build());
            }
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return files;
    }

    // ai로 생성된이미지 url을 다운받아서 NCP에 업로드
    public String uploadFile(InputStream inputStream, String filePath, ObjectMetadata metadata) {
        // 파일 확장자를 URL로부터 유추
        String fileExtension;
        if (filePath.endsWith(".jpeg") || filePath.endsWith(".jpg")) {
            fileExtension = ".jpg"; // 기본적으로 둘 다 .jpg로 처리
            metadata.setContentType("image/jpeg"); // 여기서 명시적으로 Content-Type을 설정
        } else if (filePath.endsWith(".png")) {
            fileExtension = ".png";
            metadata.setContentType("image/png"); // 여기서 명시적으로 Content-Type을 설정
        } else {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }

        String uploadFileName = getUuidFileName("generated-image" + fileExtension); // 파일명을 UUID로 생성

        try {
            String keyName = filePath + "/" + uploadFileName;

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            // S3에 업로드한 파일의 URL 반환
            return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }





}