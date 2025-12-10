package anthony.tikax.domain.ocr.service;

import java.io.InputStream;

public interface OcrService {
    String recognizePdf(InputStream is, boolean fullMode);
    String recognizeImage(byte[] imgBytes);
}
