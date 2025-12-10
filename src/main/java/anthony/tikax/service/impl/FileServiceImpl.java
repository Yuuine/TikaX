package anthony.tikax.service.impl;

import anthony.tikax.context.FileContextTL;
import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.domain.service.FileParser;
import anthony.tikax.domain.service.ProcessFile;
import anthony.tikax.dto.file.response.FileVO;
import anthony.tikax.mapper.FileMapper;
import anthony.tikax.service.FileService;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProcessFile processFile;
    private final FileParser fileParser;
    private final FileMapper fileMapper;
    private final FileRecordServiceImpl fileRecordServiceImpl;

    /**
     * 文件上传
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileVO fileUpload(Integer userId, MultipartFile file) {

        FileProcessingContext ctx;
        //解析文件基本信息，将文件上传到 minio
        try {
            ctx = processFile.processFile(file);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FILE_UPLOAD_FAILED, e);
        }

        //将文件相关信息上传到数据库
        UploadFileDO uploadFileDO = new UploadFileDO();
        uploadFileDO.setFileMd5(ctx.getFileMd5());
        uploadFileDO.setFileName(ctx.getFileName());
        uploadFileDO.setTotalSize(ctx.getTotalSize());
        uploadFileDO.setFileType("common");
        uploadFileDO.setExtension(ctx.getExtension());
        uploadFileDO.setMimeType(ctx.getMimeType());
        uploadFileDO.setStatus(1);
        uploadFileDO.setUserId(userId);
        uploadFileDO.setCreateAt(LocalDateTime.now());
        //保存文件记录
        fileRecordServiceImpl.saveFileRecord(uploadFileDO);
        //解析文件内容
        String plainText = fileParser.parse(FileContextTL.get());
        //保存文件内容
        fileMapper.insertPlainText(uploadFileDO.getFileMd5(), plainText);


        //构建文件返回结果
        FileVO fileVO = new FileVO();
        fileVO.setFileMd5(uploadFileDO.getFileMd5());
        fileVO.setText(plainText);

        return fileVO;
    }

    /**
     * 文件删除
     */
    @Override
    public void deleteFile(Integer userId, String fileName) {

        fileRecordServiceImpl.deleteFile(userId, fileName);
    }

    @Override
    public Map<String, String> getFileMd5(Integer userId, String fileName) {
        //判断文件是否存在
        String md5 = fileMapper.getFileMd5(userId, fileName);
        if (md5 == null) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }
        Map<String, String> map = new HashMap<>();
        map.put("fileMd5", md5);
        map.put("fileName", fileName);

        return map;
    }
}
