package cse308.caramel.caramelkitchen.s3client.services;

public interface S3Services {
        public void downloadFile(String keyName);
        public void uploadFile(String keyName, String uploadFilePath);
        public String getImageUrl(String imageName);
}

