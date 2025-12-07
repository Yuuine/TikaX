package anthony.tikax.domain.service;

import anthony.tikax.domain.model.FileProcessingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

//TODO: 通过文件信息解析的结果选择文件解析器
@Service
public class FileParser {

    public String parse(FileProcessingContext ctx) {

        return "测试文本";

    }
}
