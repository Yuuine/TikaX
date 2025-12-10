package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YamlParser implements DocumentParser {
    @Override
    public List<String> supportedMimeTypes() {
        return List.of(
                "text/x-yaml",
                "text/x-yml",
                "text/yml",
                "text/yaml");
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        return "yaml";
    }
}
