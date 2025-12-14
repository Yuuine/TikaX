package anthony.tikax.utils;

import java.util.UUID;

public class UUIDUtil {

    private UUIDUtil() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }

    /**
     * 生成小写不带横线的32位UUID
     * @return 32位小写UUID字符串
     */
    public static String UUIDGenerate() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
