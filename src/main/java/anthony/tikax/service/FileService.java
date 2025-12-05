package anthony.tikax.service;

import anthony.tikax.dto.file.request.FileUploadReq;
import org.springframework.stereotype.Service;

@Service
public interface FileService {
    String fileUpload(FileUploadReq fileUploadReq);
}
