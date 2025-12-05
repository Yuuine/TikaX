package anthony.tikax.service.impl;

import anthony.tikax.domain.model.FileMetaInfo;
import anthony.tikax.dto.file.request.FileUploadReq;
import anthony.tikax.service.FileService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public class FileServiceImpl implements FileService {
    /**
     * 文件上传
     *
     * @param fileUploadReq 文件上传请求参数
     * @return 文件上传结果
     */
    @Override
    public String fileUpload(FileUploadReq fileUploadReq) {

        return "";
    }

    //
    @Transactional(rollbackFor = Exception.class)
    public String processFile(MultipartFile file) {
        try {
            //完整读取文件到字节数组
            byte[] bytes = file.getBytes();
            //解析文件基础信息

            FileMetaInfo fileMetaInfo = parserFileMeta(file, bytes);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private FileMetaInfo parserFileMeta(MultipartFile file, byte[] bytes) {
        String originalFilename = file.getOriginalFilename();
        String baseName = originalFilename;

        return new FileMetaInfo(
                baseName,
                (long) bytes.length,
                "common",
                null,
                "");

    }

}
