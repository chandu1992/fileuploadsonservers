package com.jarvis.fileuploadsonservers.controller;

import com.jarvis.fileuploadsonservers.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ftp")
public class SftpServerFileUploadController {

    private final FtpService ftpService;

    public SftpServerFileUploadController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = ftpService.uploadFile(file);
        return ResponseEntity.ok("Uploaded: " + fileName);
    }
}
