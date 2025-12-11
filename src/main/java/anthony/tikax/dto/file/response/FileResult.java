package anthony.tikax.dto.file.response;

import lombok.Data;

@Data
public class FileResult {

    private Boolean state;
    private String fileMd5;
    private String fileName;
    private String message;
    private String text;

}
