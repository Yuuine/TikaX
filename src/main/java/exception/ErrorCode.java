package exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //用户相关异常
    USERNAME_ALREADY_EXIST(1001, "用户名已存在"),
    REGISTER_FAILED(1002, "注册失败"),
    LOGIN_FAILED(1003, "登录失败"),
    USERNAME_OR_PASSWORD_ERROR(1004, "用户名或密码错误"),
    USERNAME_OR_PASSWORD_NOT_NULL(1005, "用户名和密码不能为空"),

    //文件上传相关异常
    FILE_UPLOAD_NULL(2001, "上传文件为空"),
    FILE_SIZE_TOO_LARGE(2002, "文件大小超出限制"),
    FILE_TYPE_NOT_SUPPORT(2003, "文件类型不支持"),
    FILE_UPLOAD_FAILED(2004, "文件上传失败"),

    //minio存储服务相关异常
    MINIO_UPLOAD_ERROR(3001, "MinIO上传失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
