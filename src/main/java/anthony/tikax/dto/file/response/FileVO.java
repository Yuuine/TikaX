package anthony.tikax.dto.file.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class FileVO {

    private String fileMd5;
    private String text;
}
