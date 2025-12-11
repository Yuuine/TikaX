package anthony.tikax.aspect;

import anthony.tikax.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 统一日志切面
 * 1. 自动记录 Service 和 Controller 方法的入参、出参、耗时、异常
 * 2. 敏感字段自动脱敏（密码、身份证、手机号等）
 * 3. 日志格式清晰，便于排查
 */
@Component
@Slf4j
@Aspect
public class LogAspect {

    /**
     * 定义切入点表达式：匹配 anthony.tikax 包下 service 和 controller 子包中的所有方法
     */
    @Pointcut("execution(* anthony.tikax..service..*.*(..)) || " +
            "execution(* anthony.tikax..controller..*.*(..))")
    public void logPointcut() {
    }

    /**
     * 环绕通知实现日志记录逻辑
     *
     * @param joinPoint 连接点对象，用于获取目标方法信息和参数
     * @return 目标方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // 打印入参（自动脱敏）
        log.info("调用方法: {}, 参数: {}", methodName, maskSensitive(args));

        try {
            Object result = joinPoint.proceed();

            // 打印出参（只打印简单对象或 JSON 字符串，防止大对象日志爆炸）
            String resultStr = toSimpleString(result);
            long cost = System.currentTimeMillis() - start;

            log.info("方法执行完成: {}, 耗时: {}ms, 返回: {}", methodName, cost, resultStr);
            return result;
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            log.error("方法执行异常: {}, 耗时: {}ms", methodName, cost, e);
            throw e; // 抛出让全局异常处理
        }
    }

    /**
     * 对输入参数进行敏感信息脱敏处理
     *
     * @param args 原始参数数组
     * @return 脱敏后的参数字符串表示
     */
    private String maskSensitive(Object[] args) {
        if (args == null || args.length == 0) return "[]";

        return Arrays.toString(Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) return "null";
                    if (arg instanceof String s) {
                        if (s.matches("^\\d{6}\\*{4}\\d{4}$") || s.matches(".*password.*") ||
                                s.matches("^\\d{17}[0-9X]$")) { // 手机号、密码、身份证
                            return maskString(s);
                        }
                    }
                    return arg.toString();
                })
                .toArray());
    }

    /**
     * 将字符串中间部分替换为星号以达到脱敏效果
     *
     * @param str 待脱敏字符串
     * @return 脱敏后字符串
     */
    private String maskString(String str) {
        if (str == null || str.length() <= 8) return "****";
        int len = str.length();
        return str.substring(0, 3) + "*".repeat(len - 6) + str.substring(len - 3);
    }

    /**
     * 将返回对象转换为简洁字符串形式，避免输出过大的日志内容
     *
     * @param obj 需要转换的对象
     * @return 简洁字符串表示
     */
    private String toSimpleString(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        try {
            return JsonUtil.toJson(obj);
        } catch (Exception e) {
            return obj.getClass().getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
        }
    }

}
