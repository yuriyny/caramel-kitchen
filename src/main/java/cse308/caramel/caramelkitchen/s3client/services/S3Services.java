package cse308.caramel.caramelkitchen.s3client.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface S3Services {
        public void downloadFile(String keyName);
        public void uploadFile(String keyName, String uploadFilePath);
        public String getImageUrl(String imageName);
        public void uploadFileObject(String keyName, File file);
        public void uploadMultipartFileObject(String keyName, MultipartFile file) throws IOException;
}

