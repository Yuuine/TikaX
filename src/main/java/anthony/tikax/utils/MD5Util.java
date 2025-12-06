package anthony.tikax.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Component
public class MD5Util {

    /**
     * 计算文件的MD5值
     *
     * @param file 文件
     * @return 文件的MD5值
     * @throws Exception
     */
    public String md5(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            String md5 = DigestUtils.md5Hex(is);
            log.info("md5:{}", md5);
            return md5;
        }
    }

}