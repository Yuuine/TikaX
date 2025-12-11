package anthony.tikax.service;

import anthony.tikax.dto.file.response.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {
    FileVO fileUpload(Integer userId, List<MultipartFile> files);

    void deleteFile(Integer userId, String fileName);

    String getPresignedUrl(Integer userId, String fileName);
}
