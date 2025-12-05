package anthony.tikax.controller;

import anthony.tikax.dto.file.request.FileUploadReq;
import anthony.tikax.entity.Result;
import anthony.tikax.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class FileUploadController {

    private final FileService fileService;

    /**
     * 文件上传
     *
     * @param
     * @return
     */
    @PostMapping("/upload")
    public Result<Object> upload(FileUploadReq fileUploadReq) {

        String text = fileService.fileUpload(fileUploadReq);
        return Result.success(text);
    }
}
