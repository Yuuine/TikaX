package anthony.tikax.parser;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class TikaFileDetector {

    private final Tika tika;

    /**
     * 获取文件类型
     *
     * @param file
     */
    public String detectMimeType(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return tika.detect(is, file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
