package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser implements DocumentParser {
    @Override
    public String supportedMimeType() {
        return "";
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        return "";
    }
}
