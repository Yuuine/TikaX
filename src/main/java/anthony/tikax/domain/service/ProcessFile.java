package anthony.tikax.domain.service;

import anthony.tikax.domain.model.FileProcessingContext;
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
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

//TODO: 策略模式 + 上下文传递
@RequiredArgsConstructor
@Service
public class ProcessFile {

    private final TikaFileDetector tikaFileDetector;
    private final MD5Util md5Util;
    private final MinioService minioService;
    private final FileMapper fileMapper;

    public FileProcessingContext processFile(MultipartFile file) throws IOException {

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
            md5 = md5Util.md5(fileBytes);
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

        //5. 构建上下文
        FileProcessingContext ctx = new FileProcessingContext();
        ctx.setFileMd5(md5);
        ctx.setFileName(originalFilename);
        ctx.setTotalSize((long) fileBytes.length);
        ctx.setContent(fileBytes);

        //6. 解析文件元信息
        String mimeType = detectMimeType(new ByteArrayInputStream(fileBytes));
        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "application/octet-stream";
        }
        String extension = tikaFileDetector.getExtensionFromMimeType(mimeType);
        if (extension == null) extension = "";

        ctx.setExtension(extension);
        ctx.setMimeType(mimeType);

        //返回上下文
        return ctx;

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
        try {
            fileMapper.insert(uploadFileDO);
        } catch (Exception e) {
            throw new BizException(ErrorCode.SERVER_ERROR, e);
        }
    }

    // 重构检测文件类型，使其接收一个输入流
    public String detectMimeType(InputStream inputStream) throws IOException {
        // 使用 Tika 的 AutoDetectParser
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();
        // Tika 需要可标记的流（Markable），ByteArrayInputStream 是
        MediaType mediaType = detector.detect(inputStream, metadata);
        return mediaType.toString();
    }
}


