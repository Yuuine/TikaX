package anthony.tikax.dto.file.request;

import lombok.Data;

@Data
public class FileUploadReq {

    private String fileName;
    private Integer userId;

}
