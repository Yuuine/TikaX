package anthony.tikax.controller;

import anthony.tikax.domain.spi.MinioService;
import anthony.tikax.dto.file.response.FileVO;
import anthony.tikax.entity.Result;
import anthony.tikax.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final MinioService minioService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<FileVO> upload(
            @RequestParam("userId") Integer userId,
            @RequestParam("file") MultipartFile file
    ) {

        FileVO fileVO = fileService.fileUpload(userId, file);
        return Result.success(fileVO);
    }

    @DeleteMapping("/delete")
    public Result<String> delete(
            @RequestParam("userId") Integer userId,
            @RequestParam("fileName") String fileName
    ) {
        fileService.deleteFile(userId, fileName);
        return Result.success("删除成功");
    }

    @GetMapping("/download")
    public Result<String> download(
            @RequestParam("userId") Integer userId,
            @RequestParam("fileName") String fileName
    ) {

        String url = fileService.getPresignedUrl(userId, fileName);
        return Result.success(url);
    }
}
