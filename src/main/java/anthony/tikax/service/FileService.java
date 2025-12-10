package anthony.tikax.service;

import anthony.tikax.dto.file.response.FileVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileVO fileUpload(Integer userId, MultipartFile file);

    void deleteFile(Integer userId, String fileName);
}
