package anthony.tikax.domain.ocr.service.impl;

import anthony.tikax.domain.ocr.service.OcrService;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class OcrServiceImpl implements OcrService {
    @Override
    public String recognizePdf(InputStream is, boolean fullMode) {
        // 用 Tika + Tesseract OCR
        PDFParserConfig config = new PDFParserConfig();
        config.setOcrStrategy(PDFParserConfig.OCR_STRATEGY.OCR_ONLY);
        // ... 类似 parsePureText，但返回 OCR 文本
        return ""; // TODO: 实现
    }

    @Override
    public String recognizeImage(byte[] imgBytes) {
        // 用 Tika 或独立 Tesseract 识别单图
        return ""; // TODO
    }
}
