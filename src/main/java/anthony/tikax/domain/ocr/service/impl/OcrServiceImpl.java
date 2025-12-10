package anthony.tikax.domain.ocr.service.impl;

import anthony.tikax.domain.ocr.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Component
public class OcrServiceImpl implements OcrService {

    @Value("${tesseract.datapath}")
    private String dataPath;

    @Value("${tesseract.language}")
    private String language; // chi_sim+eng

    /**
     * PDF OCR：使用 Tika -> Tesseract
     */
    @Override
    public String recognizePdf(InputStream is, boolean fullMode) {

        try {
            // 配置 PDF OCR
            PDFParserConfig config = new PDFParserConfig();
            config.setOcrDPI(300);
            config.setExtractInlineImages(true);

            // OCR策略
            config.setOcrStrategy(
                    fullMode ?
                            PDFParserConfig.OCR_STRATEGY.OCR_AND_TEXT_EXTRACTION :
                            PDFParserConfig.OCR_STRATEGY.OCR_ONLY
            );

            // Tesseract 配置
            System.setProperty("tessdata.prefix", dataPath);

            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler(-1);
            ParseContext parseContext = new ParseContext();

            parseContext.set(PDFParserConfig.class, config);

            parser.parse(is, handler, metadata, parseContext);

            return handler.toString();

        } catch (Exception e) {
            throw new RuntimeException("PDF OCR Failed", e);
        }
    }

    /**
     * 图片 OCR：Tess4J + 内存字节流
     */
    @Override
    public String recognizeImage(byte[] imgBytes) {
        try (InputStream is = new ByteArrayInputStream(imgBytes)) {

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new RuntimeException("Image decode failed");
            }

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(dataPath);
            tesseract.setLanguage(language); // chi_sim+eng

            return tesseract.doOCR(image);

        } catch (Exception e) {
            throw new RuntimeException("Image OCR Failed", e);
        }
    }
}
