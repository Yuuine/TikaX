package anthony.tikax.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * JSON工具类，提供对象与JSON字符串之间的转换功能
 */
@UtilityClass
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 需要转换的对象
     * @return 转换后的JSON字符串，如果转换失败则返回对象的toString()结果
     */
    public static String toJson(Object obj) {
        try {
            // 使用Jackson ObjectMapper将对象序列化为JSON字符串
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // 转换失败时返回对象的默认字符串表示
            return obj.toString();
        }
    }
}
