package anthony.tikax.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class BcryptUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private BcryptUtil() {
    }

    /**
     * 对密码进行加密
     *
     * @param password 未加密的密码
     * @return 加密后的密码
     */
    public static String encode(String password) {
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }
        return encoder.encode(password);
    }

    /**
     * 验证密码是否匹配
     *
     * @param rawPassword     未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 匹配结果
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("password cannot be null");
        }
        return encoder.matches(rawPassword, encodedPassword);
    }
}
