package com.jarvis.fileuploadsonservers.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FtpService {

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.remote-dir}")
    private String remoteDir;


    // FTPClient is not secured. Apache Commons Net uses plain FTP so use JSch
    public String uploadFile(MultipartFile file) throws IOException {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);

            ftpClient.enterLocalPassiveMode(); // Important
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Optional: create unique filename
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Change directory (create if not exists)
            ftpClient.makeDirectory(remoteDir);
            ftpClient.changeWorkingDirectory(remoteDir);// move to directory

            try (InputStream inputStream = file.getInputStream()) {
                boolean done = ftpClient.storeFile(fileName, inputStream);
                if (!done) {
                    throw new IOException("Failed to upload file");
                }
            }

            return fileName;

        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    public boolean deleteFile(String fileName) throws IOException {

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();

            ftpClient.changeWorkingDirectory(remoteDir);

            return ftpClient.deleteFile(fileName);

        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }


}
