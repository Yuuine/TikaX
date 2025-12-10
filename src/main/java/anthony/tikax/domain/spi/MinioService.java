package anthony.tikax.domain.spi;

import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;

    private static final String bucketName = "tikax";

    public void uploadFile(String objectName, InputStream inputStream) {
        // 上传文件到MinIO
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, -1, 50 * 1024 * 1024)
                            .build());
        } catch (Exception e) {
            throw new BizException(ErrorCode.MINIO_UPLOAD_ERROR, e);
        }

    }

    /**
     * 获取文件访问URL
     *
     * @param md5 文件MD5
     * @return 文件访问URL
     */
    public String getPresignedUrl(String md5, String downloadFileName) {

        Map<String, String> reqParams = new HashMap<>();
        String encoded = URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        reqParams.put("response-content-disposition",
                "attachment; filename*=UTF-8''" + encoded);

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(md5)
                            .expiry(60 * 10)// 10分钟有效期
                            .extraQueryParams(reqParams)
                            .build()
            );
        } catch (Exception e) {
            throw new BizException(ErrorCode.MINIO_GET_URL_ERROR, e);
        }
    }
}
