package anthony.tikax.service.impl;

import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.domain.service.FileParser;
import anthony.tikax.domain.service.ProcessFile;
import anthony.tikax.dto.file.response.FileVO;
import anthony.tikax.service.FileService;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ProcessFile processFile;
    private final FileParser fileParser;

    /**
     * 文件上传
     *
     */
    @Override
    public FileVO fileUpload(Integer userId, MultipartFile file) {

        UploadFileDO uploadFileDO;
        //解析文件基本信息，将文件上传到 minio
        try {
            uploadFileDO = processFile.processFile(file);
        }
        catch (BizException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BizException(ErrorCode.FILE_UPLOAD_FAILED, e);
        }

        //将文件相关信息上传到数据库
        uploadFileDO.setUserId(userId);
        processFile.saveFileRecord(uploadFileDO);

        //TODO: 解析文件
        fileParser.parse(file);


        //构建文件返回结果
        FileVO fileVO = new FileVO();
        fileVO.setFileMd5(uploadFileDO.getFileMd5());
        fileVO.setText("测试文本");//临时写死

        return fileVO;
    }
}
