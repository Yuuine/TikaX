package anthony.tikax.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;

/**
 * Tika 文件解析器
 */
@Service
@RequiredArgsConstructor
public class TikaFileDetector {

    /**
     * 获取文件扩展名
     *
     * @param mimeType
     * @return
     */
    public String getExtensionFromMimeType(String mimeType) {
        try {
            MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();
            return mimeTypes.forName(mimeType).getExtension();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
