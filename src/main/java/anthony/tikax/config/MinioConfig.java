package anthony.tikax.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {
    private String endpoint; // MinIO服务地址
    private String accessKey; // 访问密钥
    private String secretKey; // 秘密密钥
    private String bucketName; // 存储桶名称

    /**
     * 创建MinIO客户端
     *
     * @return
     */
    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(endpoint) // MinIO服务地址
                    .credentials(accessKey, secretKey) // 访问密钥
                    .build();// 创建MinIO客户端
        } catch (Exception e) {
            throw new RuntimeException("MinIO客户端初始化失败", e);
        }
    }
}
