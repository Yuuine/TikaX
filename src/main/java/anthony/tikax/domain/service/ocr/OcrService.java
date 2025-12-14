package anthony.tikax.domain.service.ocr;

import java.io.InputStream;

public interface OcrService {
    String recognizePdf(InputStream is, boolean fullMode);

    String recognizeImage(byte[] imgBytes);
}
