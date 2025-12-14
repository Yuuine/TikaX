package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkdownParser implements DocumentParser {
    @Override
    public List<String> supportedMimeTypes() {
        return List.of("text/markdown");
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        return "markdown";
    }
}
