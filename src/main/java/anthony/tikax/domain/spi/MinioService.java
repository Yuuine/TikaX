package anthony.tikax.domain.spi;

import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;

    public void uploadFile(String objectName, InputStream inputStream) {
        // 上传文件到MinIO
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("tikax")
                            .object(objectName)
                            .stream(inputStream, -1, 50 * 1024 * 1024)
                            .build());
        } catch (Exception e) {
            throw new BizException(ErrorCode.MINIO_UPLOAD_ERROR, e);
        }

    }
}
