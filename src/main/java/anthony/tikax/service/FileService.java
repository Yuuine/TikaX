package anthony.tikax.service;

import anthony.tikax.dto.file.request.FileUploadReq;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    String fileUpload(MultipartFile file);
}
