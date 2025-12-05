package anthony.tikax.parser;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class TikaFileDetector {

    private final Tika tika;

    //检测文件类型
    public String delectMimeType(MultipartFile file) {

    }
}
