package anthony.tikax.domain.service;

import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.parser.TikaFileDetector;
import anthony.tikax.service.FileService;
import anthony.tikax.utils.MD5Util;
import exception.BizException;
import exception.ErrorCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProcessFile {

    private final TikaFileDetector tikaFileDetector;
    private final FileService fileService;
    private final MD5Util md5Util;

    public String processFile(MultipartFile file) throws Exception {

        validateFile(file);//验证文件是否合法

        //TODO: 1.读取文件上传minio
        //2. 计算文件的MD5值作为该文件的主键
        String md5 = md5Util.md5(file);
        //3. 解析文件基础信息
        byte[] fileBytes = file.getBytes();
        FileMetaInfo fileMetaInfo = parserFileMeta(file, fileBytes);
        UploadFileDO uploadFileDO = new UploadFileDO();
        uploadFileDO.setFileMd5(md5);
        uploadFileDO.setFileName(fileMetaInfo.getBaseName());


        return "";
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ErrorCode.FILE_UPLOAD_NULL);
        }
        if (file.getSize() > 50 * 1024 * 1024) { // 50MB
            throw new BizException(ErrorCode.FILE_SIZE_TOO_LARGE);
        }
    }

    /**
     * 保存文件记录
     *
     * @param md5       文件的MD5值
     * @param meta      文件基础信息
     * @param objectPath 文件在MinIO中的存储路径
     * @return 文件的MD5值
     */
    @Transactional(rollbackFor = Exception.class)
    protected String saveFileRecord(String md5, FileMetaInfo meta, String objectPath) {
        UploadFileDO uploadFileDO = new UploadFileDO();

        uploadFileDO.setFileMd5(md5);
        return md5;
    }


    /**
     * 解析文件基础信息
     *
     * @param file  文件
     * @param bytes 文件字节数组
     * @return 文件基础信息
     */
    private FileMetaInfo parserFileMeta(MultipartFile file, byte[] bytes) {
        String baseName = file.getOriginalFilename();
        String mimeType = tikaFileDetector.delectMimeType(file);
        String extension = tikaFileDetector.getExtensionFromMimeType(mimeType);

        FileMetaInfo fileMetaInfo = new FileMetaInfo(
                baseName,
                (long) bytes.length,
                "common",
                extension,
                mimeType
        );

    }


    @Data
    public static class FileMetaInfo {

        private String baseName;//不含扩展名的文件名
        private Long totalSize;//文件大小
        private String fileType;//文件类型
        private String extension;//扩展名
        private String mimeType;//MIME类型
    }
}


