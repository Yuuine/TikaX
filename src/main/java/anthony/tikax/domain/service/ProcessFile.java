package anthony.tikax.domain.service;

import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.domain.spi.MinioService;
import anthony.tikax.mapper.FileMapper;
import anthony.tikax.parser.TikaFileDetector;
import anthony.tikax.utils.MD5Util;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProcessFile {

    private final TikaFileDetector tikaFileDetector;
    private final MD5Util md5Util;
    private final MinioService minioService;
    private final FileMapper fileMapper;

    public UploadFileDO processFile(MultipartFile file) {

        validateFile(file);//验证文件是否合法

        //先获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //1. 读取文件到内存
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new BizException(ErrorCode.FILE_PROCESS_ERROR, e);
        }
        //2.计算文件的MD5值，作为文件的新文件名
        String md5;
        try {
            md5 = md5Util.md5(file);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FILE_MD5_ERROR, e);
        }
        //判断用户是否上传了重复文件
        if (fileMapper.getFileByMd5(md5)) {
            throw new BizException(ErrorCode.FILE_UPLOAD_EXIST);
        }

        //4. 上传文件到MinIO
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        minioService.uploadFile(md5, inputStream);
        //5. 解析文件基础信息
        FileMetaInfo fileMetaInfo = parserFileMeta(file, originalFilename, fileBytes.length);
        UploadFileDO uploadFileDO = new UploadFileDO();

        //6. 将文件相关的信息设置到 UploadFileDO 中
        //TODO: 添加上传者的ID (在Controller中实现)
        uploadFileDO.setFileMd5(md5);
        uploadFileDO.setFileName(fileMetaInfo.getOriginalFileName());
        uploadFileDO.setTotalSize(fileMetaInfo.getTotalSize());
        uploadFileDO.setFileType(fileMetaInfo.getFileType());
        uploadFileDO.setExtension(fileMetaInfo.getExtension());
        uploadFileDO.setMimeType(fileMetaInfo.getMimeType());
        uploadFileDO.setStatus(1);
        uploadFileDO.setCreateAt(LocalDateTime.now());
        return uploadFileDO;

    }

    private void validateFile(MultipartFile file) {
        // 禁止不上传文件（原始文件名为空或 null）
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BizException(ErrorCode.FILE_UPLOAD_NULL);
        }
        //不允许上传文件内容为空
        if (file.isEmpty()) {
            throw new BizException(ErrorCode.FILE_UPLOAD_EMPTY);
        }
        if (file.getSize() > 50 * 1024 * 1024) { // 50MB
            throw new BizException(ErrorCode.FILE_SIZE_TOO_LARGE);
        }
    }

    /**
     * 保存文件记录
     *
     * @param uploadFileDO 文件记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFileRecord(UploadFileDO uploadFileDO) {
        fileMapper.insert(uploadFileDO);
    }


    /**
     * 解析文件基础信息
     *
     * @param file             文件
     * @param originalFilename 文件的原始名称
     * @return 文件基础信息
     */
    private FileMetaInfo parserFileMeta(MultipartFile file, String originalFilename, long totalSize) {

        String mimeType = tikaFileDetector.detectMimeType(file);
        // 防御性处理：mimeType 为 null 或空时，设为默认值
        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "application/octet-stream";
        }

        String extension = tikaFileDetector.getExtensionFromMimeType(mimeType);
        if (extension == null) {
            extension = "";
        }

        return new FileMetaInfo(originalFilename, totalSize, "common", extension, mimeType);

    }


    @Data
    @AllArgsConstructor
    public static class FileMetaInfo {

        private String originalFileName;//原始文件名
        private Long totalSize;//文件大小
        private String fileType;//文件类型
        private String extension;//扩展名
        private String mimeType;//MIME类型
    }
}


