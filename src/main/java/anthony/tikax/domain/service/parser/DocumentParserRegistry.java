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

    /**
     * 构造函数，初始化文档解析器注册表
     *
     * @param parsers 文档解析器列表，Spring会自动收集所有DocumentParser的实现类
     */
    public DocumentParserRegistry(List<DocumentParser> parsers) {
        // Spring 会自动收集所有 DocumentParser 的实现类
        for (DocumentParser parser : parsers) {
            String type = parser.supportedMimeType();
            parserMap.put(type, parser);

            log.info("[ParserRegistry] 注册解析器: {} -> {}", type, parser.getClass().getSimpleName());
        }

        log.info("[ParserRegistry] 共加载解析器数量: {}", parserMap.size());
    }

    /**
     * 根据MIME类型获取对应的文档解析器
     *
     * @param mimeType MIME类型
     * @return 对应的文档解析器，如果不存在则返回null
     */
    public DocumentParser getParser(String mimeType) {
        return parserMap.get(mimeType);
    }

    /**
     * 判断是否包含指定MIME类型的解析器
     *
     * @param mimeType MIME类型
     * @return 如果包含返回true，否则返回false
     */
    public boolean contains(String mimeType) {
        return parserMap.containsKey(mimeType);
    }
}