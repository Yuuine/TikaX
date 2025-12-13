package anthony.tikax.dto.file.response;

import lombok.Data;

import java.util.List;

@Data
public class FileListVO {
    private List<SingleFile> results;
}
