package anthony.tikax.service.impl;

import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.domain.service.ProcessFile;
import anthony.tikax.mapper.FileMapper;
import anthony.tikax.service.FileService;
import exception.BizException;
import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProcessFile processFile;
    private final FileMapper fileMapper;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String fileUpload(MultipartFile file) {

        UploadFileDO uploadFileDO;
        //解析文件基本信息，将文件上传到 minio
        try {
            uploadFileDO = processFile.processFile(file);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        //将文件相关信息上传到数据库
        //TODO: 添加上传者ID
        uploadFileDO.setUserId(1);
        processFile.saveFileRecord(uploadFileDO);

        //解析文件


        return null;
    }
}
