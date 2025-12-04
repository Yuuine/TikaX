package anthony.tikax.controller;

import anthony.tikax.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class FileUploadController {
    /**
     * 文件上传
     * @param file
     * @return
     */

    @PostMapping("/upload")
    public Result<Object> upload(MultipartFile file) {

        return Result.success();
    }
}
