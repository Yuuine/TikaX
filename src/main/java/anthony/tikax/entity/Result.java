package anthony.tikax.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
//实体类，统一响应结果
public class Result<T> {
    private Integer code;//响应码
    private String message;//响应信息
    private T data;//响应数据

    /**
     * 创建一个表示操作成功的Result对象
     *
     * @param <E> 泛型类型，表示返回数据的类型
     * @param data 操作成功时返回的数据
     * @return 包含成功状态码0、成功消息"success"和返回数据的Result对象
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "success", data);
    }
    public static Result<Object> success() {
        return new Result<>(0, "success", null);
    }

    public static Result<Void> error(String message) {
        return new Result<>(1, message, null);
    }

    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }

}
