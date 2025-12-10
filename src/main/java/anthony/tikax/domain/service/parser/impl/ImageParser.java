package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.ocr.service.OcrService;
import anthony.tikax.domain.service.parser.DocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageParser implements DocumentParser {

    private final OcrService ocrService;

    @Override
    public List<String> supportedMimeTypes() {

        return List.of(
                "image/png",
                "image/jpeg",
                "image/jpg",
                "image/gif",
                "image/bmp",
                "image/webp",
                "image/*" // 通配符
        );
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        log.info("ImageParser 开始解析文件");

        byte[] bytes = ctx.getContent();

        String text = ocrService.recognizeImage(bytes);

        log.info("ImageParser 完成 OCR 文本长度：{}", text.length());

        return text;
    }
}
