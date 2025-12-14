package anthony.tikax.controller;

import anthony.tikax.dto.file.response.FileListVO;
import anthony.tikax.dto.file.response.FileVO;
import anthony.tikax.entity.Result;
import anthony.tikax.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<FileVO> upload(
            @RequestParam("userId") Integer userId,
            @RequestParam("files") List<MultipartFile> files
    ) {

        FileVO fileVO = fileService.fileUpload(userId, files);
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

    @GetMapping("/fileList")
    public Result<FileListVO> fileList(
            @RequestParam("userId") Integer userId
    ) {

        FileListVO fileListVO = fileService.fileList(userId);
        return Result.success(fileListVO);
    }
}
