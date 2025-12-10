package anthony.tikax.domain.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档解析器注册表
 * 自动感知所有解析器实现类
 * 无需修改 FileParser
 * 新增文件类型时新增一个解析器类 → 自动注册
 */
@Slf4j
@Component
public class DocumentParserRegistry {

    private final Map<String, DocumentParser> parserMap = new HashMap<>();
    private final List<DocumentParser> parserList;

    /**
     * 构造函数，用于初始化文档解析器注册表。
     * 将传入的解析器列表注册到内部映射中，并记录日志信息。
     *
     * @param parsers 文档解析器列表，不能为空
     */
    public DocumentParserRegistry(List<DocumentParser> parsers) {

        this.parserList = parsers;

        for (DocumentParser parser : parsers) {
            for (String mime : parser.supportedMimeTypes()) {
                parserMap.put(mime, parser);
                log.info("[ParserRegistry] 注册解析器: {} -> {}", mime, parser.getClass().getSimpleName());
            }

            log.info("[ParserRegistry] 共加载解析器数量: {}", parserMap.size());
        }
    }


    /**
     * 根据MIME类型获取对应的文档解析器
     *
     * @param mimeType MIME类型
     * @return 对应的文档解析器，如果不存在则返回null
     */
    public DocumentParser getParser(String mimeType) {

        if (mimeType == null) return null;

        // 1. 精准匹配
        if (parserMap.containsKey(mimeType)) {
            return parserMap.get(mimeType);
        }

        // 2. 通配符匹配
        for (DocumentParser parser : parserList) {
            for (String supported : parser.supportedMimeTypes()) {

                if (supported.endsWith("/*")) {
                    String prefix = supported.substring(0, supported.indexOf("/*"));
                    if (mimeType.startsWith(prefix + "/")) {
                        log.info("[ParserRegistry] 通配符匹配: {} → {}", supported, mimeType);
                        return parser;
                    }
                }
            }
        }
        log.warn("[ParserRegistry] 未找到解析器: {}", mimeType);
        return null;
    }

    /**
     * 判断是否包含指定MIME类型的解析器
     *
     * @param mimeType MIME类型
     * @return 如果包含返回true，否则返回false
     */
    public boolean contains(String mimeType) {
        if (parserMap.containsKey(mimeType)) return true;

        for (DocumentParser parser : parserList) {
            for (String supported : parser.supportedMimeTypes()) {

                if (supported.endsWith("/*")) {
                    String prefix = supported.substring(0, supported.indexOf("/*"));
                    if (mimeType.startsWith(prefix + "/")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
