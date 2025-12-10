package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;

public class YamlParser implements DocumentParser {
    @Override
    public String supportedMimeType() {
        return "text/yaml";
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        return "yaml";
    }
}
