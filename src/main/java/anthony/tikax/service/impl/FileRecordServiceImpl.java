package anthony.tikax.service.impl;

import anthony.tikax.domain.model.UploadFileDO;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import anthony.tikax.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileRecordServiceImpl {


    private final FileMapper fileMapper;
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
}
