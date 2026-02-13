package com.jarvis.fileuploadsonservers.controller;

import com.jarvis.fileuploadsonservers.service.AzureBlobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class AzureBlobFileController {

    private final AzureBlobService blobService;

    public AzureBlobFileController(AzureBlobService blobService) {
        this.blobService = blobService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        String url = blobService.uploadFile(file);

        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {

        boolean deleted = blobService.deleteFile(fileName);

        if (deleted)
            return ResponseEntity.ok("File deleted successfully");

        return ResponseEntity.badRequest().body("File not found");
    }


}
