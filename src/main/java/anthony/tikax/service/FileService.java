package anthony.tikax.service;

import anthony.tikax.dto.file.response.FileVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    FileVO fileUpload(Integer userId, MultipartFile file);
}
