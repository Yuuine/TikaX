package anthony.tikax.domain.service;

import org.springframework.stereotype.Component;

@Component
public class TextTypeDetector {
    public String detect(String filename, String content) {

        if (filename.endsWith(".md")) {
            return "text/markdown";
        }

        if (filename.endsWith(".json")) {
            return "application/json";
        }

        if (filename.endsWith(".yml") || filename.endsWith(".yaml")) {
            return "text/yaml";
        }

        // 内容探测：JSON  (粗略判断)
        if (content.trim().startsWith("{") || content.trim().startsWith("[")) {
            return "application/json";
        }

        // 内容探测：Markdown (粗略判断)
        if (content.contains("# ") || content.contains("```") || content.contains("- ") || content.contains("* ")) {
            return "text/markdown";
        }

        return "text/plain"; // 默认仍然是文本
    }
}
