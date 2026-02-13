package com.jarvis.fileuploadsonservers.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AzureBlobService {

    private final BlobContainerClient containerClient;

    public AzureBlobService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    // Upload file
    public String uploadFile(MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

        BlobClient blobClient = containerClient.getBlobClient(fileName);

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        return blobClient.getBlobUrl();
    }

    // Delete file
    public boolean deleteFile(String fileName) {

        BlobClient blobClient = containerClient.getBlobClient(fileName);

        if (blobClient.exists()) {
            blobClient.delete();
            return true;
        }

        return false;
    }
}
