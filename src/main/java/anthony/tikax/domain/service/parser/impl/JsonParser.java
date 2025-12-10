package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;

public class JsonParser implements DocumentParser {
    @Override
    public String supportedMimeType() {
        return "text/json";
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        return "json";
    }
}
