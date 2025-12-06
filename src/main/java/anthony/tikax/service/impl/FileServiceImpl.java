package anthony.tikax.service.impl;

import anthony.tikax.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    /**
     * 文件上传
     *
     * @param file 文件上传请求参数
     * @return 文件上传结果
     */
    @Override
    public String fileUpload(MultipartFile file) {

        return "";
    }
}
