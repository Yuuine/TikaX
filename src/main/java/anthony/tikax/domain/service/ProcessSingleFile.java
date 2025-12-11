package anthony.tikax.domain.service;

import anthony.tikax.context.FileContextTL;
import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.dto.file.response.FileResult;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import anthony.tikax.mapper.FileMapper;
import anthony.tikax.service.impl.FileRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * 文件处理服务类，用于处理单个上传文件的完整流程。
 * 包括文件上传、信息提取、内容解析及数据持久化等操作。
 */
@Service
@RequiredArgsConstructor
public class ProcessSingleFile {

    private final ProcessFile processFile;
    private final FileRecordServiceImpl fileRecordServiceImpl;
    private final FileMapper fileMapper;
    private final FileParser fileParser;

    /**
     * 处理单个上传文件的主要方法。
     * <p>
     * 此方法会完成以下主要步骤：
     * 1. 解析并上传文件到存储系统（如 MinIO）；
     * 2. 将文件元信息保存至数据库；
     * 3. 解析文件内容为纯文本；
     * 4. 将纯文本内容存入数据库；
     * 5. 返回处理结果。
     *
     * @param userId 当前用户的ID，用于关联文件归属
     * @param file   Spring封装的上传文件对象
     * @return FileResult 文件处理结果，包括MD5、名称、是否成功以及文本内容等信息
     * @throws BizException 当文件上传或解析过程中发生业务异常时抛出
     */
    public FileResult processSingleFile(Integer userId, MultipartFile file) {
        FileProcessingContext ctx;
        //1. 解析文件基本信息，并将文件上传到存储系统（如MinIO）
        try {
            ctx = processFile.processFile(file);
        } catch (BizException e) {
            return failResult(file.getOriginalFilename(), e.getMessage()); // 业务错误直接返回
        } catch (Exception e) {
            throw new BizException(ErrorCode.FILE_UPLOAD_FAILED, e); // 系统异常继续抛
        }

        //2. 保存文件元信息到数据库
        String fileMd5 = ctx.getFileMd5();
        String fileName = ctx.getFileName();
        Long totalSize = ctx.getTotalSize();
        String extension = ctx.getExtension();
        String mimeType = ctx.getMimeType();

        UploadFileDO uploadFileDO = new UploadFileDO();

        uploadFileDO.setFileMd5(fileMd5);
        uploadFileDO.setFileName(fileName);
        uploadFileDO.setTotalSize(totalSize);
        uploadFileDO.setFileType("common");
        uploadFileDO.setExtension(extension);
        uploadFileDO.setMimeType(mimeType);
        uploadFileDO.setStatus(1);
        uploadFileDO.setUserId(userId);
        uploadFileDO.setCreateAt(LocalDateTime.now());

        fileRecordServiceImpl.saveFileRecord(uploadFileDO);

        //3. 解析文件内容为纯文本格式
        String plainText = fileParser.parse(FileContextTL.get());

        //4. 将解析后的纯文本插入数据库中
        fileMapper.insertPlainText(uploadFileDO.getFileMd5(), plainText);

        //5. 构造返回结果对象
        FileResult fileResult = new FileResult();

        fileResult.setState(true);
        fileResult.setFileMd5(fileMd5);
        fileResult.setFileName(fileName);
        fileResult.setMessage("上传成功");
        fileResult.setText(plainText);

        return fileResult;
    }

    /**
     * 创建一个表示操作失败的文件结果对象
     *
     * @param fileName 文件名
     * @param message 错误信息
     * @return 包含失败状态信息的FileResult对象
     */
    private FileResult failResult(String fileName, String message) {

        FileResult r = new FileResult();

        r.setState(false);
        r.setFileMd5("default");
        r.setFileName(fileName);
        r.setMessage(message);
        r.setText("default");
        return r;
    }

}