package com.jarvis.fileuploadsonservers.config;

import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobContainerClient;
        import com.azure.storage.blob.BlobContainerClientBuilder;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;

@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Bean
    public BlobContainerClient blobContainerClient() {

        String connectionString ="DefaultEndpointsProtocol=https;AccountName="+accountName+";AccountKey="+accountKey+";EndpointSuffix=core.windows.net";

        BlobContainerClient client =
                new BlobContainerClientBuilder()
                        .connectionString(connectionString)
                        .containerName(containerName)
                        .buildClient();

        if (!client.exists()) {
            client.create();
        }

        return client;
    }
}
