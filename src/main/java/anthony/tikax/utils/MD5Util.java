package anthony.tikax.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Component
public class MD5Util {

    /**
     * 计算字节数组的MD5值
     *
     * @param file 文件字节数组
     * @return 文件的MD5值
     * @throws Exception
     */
    public String md5(byte[] file) throws Exception {
        String md5 = DigestUtils.md5Hex(file);
        log.info("md5:{}", md5);
        return md5;
    }

    /**
     * 计算InputStream的MD5值
     *
     * @param inputStream 文件输入流
     * @return 文件的MD5值
     * @throws Exception
     */
    public String md5(InputStream inputStream) throws Exception {
        String md5 = DigestUtils.md5Hex(inputStream);
        log.info("md5:{}", md5);
        return md5;
    }

    /**
     * 计算字节数组转换为InputStream的MD5值
     *
     * @param file 文件字节数组
     * @return 文件的MD5值
     * @throws Exception
     */
    public String md5FromBytes(byte[] file) throws Exception {
        try (InputStream is = new ByteArrayInputStream(file)) {
            String md5 = DigestUtils.md5Hex(is);
            log.info("md5:{}", md5);
            return md5;
        }
    }
}