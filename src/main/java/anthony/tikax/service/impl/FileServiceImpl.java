package anthony.tikax.service.impl;

import anthony.tikax.context.FileContextTL;
import anthony.tikax.domain.service.ProcessSingleFile;
import anthony.tikax.domain.spi.MinioService;
import anthony.tikax.dto.file.response.FileResult;
import anthony.tikax.dto.file.response.FileVO;
import anthony.tikax.mapper.FileMapper;
import anthony.tikax.service.FileService;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final FileRecordServiceImpl fileRecordServiceImpl;
    private final StringRedisTemplate stringRedisTemplate;
    private final MinioService minioService;
    private final ProcessSingleFile processSingleFile;

    /**
     * 文件上传处理方法
     *
     * @param userId 用户ID，用于标识文件上传的用户
     * @param files  多个待上传的文件列表
     * @return FileVO 包含文件上传结果的视图对象
     */
    @Override
    public FileVO fileUpload(Integer userId, List<MultipartFile> files) {

        //构建返回文件结果列表
        List<FileResult> results = new ArrayList<>();

        //遍历上传文件并逐个处理
        for (MultipartFile file : files) {
            try {
                FileResult fileResult = processSingleFile.processSingleFile(userId, file);
                results.add(fileResult);
            } catch (Exception e) {

                FileResult fail = new FileResult();
                fail.setState(false);
                fail.setMessage(e.getMessage());
                results.add(fail);

            } finally {
                FileContextTL.clear();
            }
        }

        //构建文件返回结果
        FileVO vo = new FileVO();
        vo.setResults(results);
        return vo;
    }


    /**
     * 文件删除
     */
    @Override
    public void deleteFile(Integer userId, String fileName) {

        fileRecordServiceImpl.deleteFile(userId, fileName);
    }

    @Override
    public String getPresignedUrl(Integer userId, String fileName) {

        String md5 = fileMapper.getFileMd5(userId, fileName);
        if (md5 == null) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }

        String key = "fileUrl:" + md5;

        // 1. 查询缓存
        String url = stringRedisTemplate.opsForValue().get(key);

        // 2. 缓存未命中 → 生成新链接
        if (url == null) {

            url = minioService.getPresignedUrl(md5, fileName);

            // 3. 存入 Redis，设置过期时间 5 分钟
            stringRedisTemplate.opsForValue().set(key, url, 5, TimeUnit.MINUTES);
        }

        return url;
    }

}
