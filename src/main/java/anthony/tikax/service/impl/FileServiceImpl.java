package anthony.tikax.service.impl;

import anthony.tikax.domain.service.FileMetaInfo;
import anthony.tikax.parser.TikaFileDetector;
import anthony.tikax.service.FileService;
import anthony.tikax.utils.MD5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final TikaFileDetector tikaFileDetector;
    private final MD5Util md5Util;

    /**
     * 文件上传
     *
     * @param file 文件上传请求参数
     * @return 文件上传结果
     */
    @Override
    public String fileUpload(MultipartFile file) {

        try {
            return processFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //
    @Transactional(rollbackFor = Exception.class)
    public String processFile(MultipartFile file) throws Exception {
    String md5 = md5Util.md5(file);


        try {
            //完整读取文件到字节数组
            byte[] bytes = file.getBytes();
            //解析文件基础信息
            FileMetaInfo fileMetaInfo = parserFileMeta(file, bytes);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
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

        return new FileMetaInfo(
                baseName,
                (long) bytes.length,
                "common",
                extension,
                mimeType
        );

    }

}
