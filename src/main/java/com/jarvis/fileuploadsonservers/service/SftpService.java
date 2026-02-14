package com.jarvis.fileuploadsonservers.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

@Service
public class SftpService {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remote-dir}")
    private String remoteDir;

    //SFTP

    public String uploadFile(MultipartFile file) throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);

        session.setPassword(password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();

        ChannelSftp sftp = (ChannelSftp) channel;

        sftp.cd(remoteDir);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();

        sftp.put(inputStream, fileName);

        sftp.exit();
        session.disconnect();

        return fileName;
    }

    public void deleteFile(String fileName) throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);

        session.setPassword(password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();

        ChannelSftp sftp = (ChannelSftp) channel;

        sftp.cd(remoteDir);

        sftp.rm(fileName);

        sftp.exit();
        session.disconnect();
    }

}
