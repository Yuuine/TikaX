package anthony.tikax.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tikax.text-chunker")
@Data
public class ChunkerProperties {

    private int chunkSize = 512;
    private int overlap = 100;
}
