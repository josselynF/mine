package com.mine.project.controller;

import com.mine.project.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
@CrossOrigin("*")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/upload")
    public String uploadFile(@RequestPart(value="file") MultipartFile file) {
        fileService.uploadFile(file);
        return  "El archivo "+file.getOriginalFilename()+" fue cargado correctamente a S3";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/list")
    public List<String> listFiles() {
        return fileService.getObjectsFromS3();
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestParam("key") String key) {
        InputStreamResource resource  = new InputStreamResource(fileService.downloadFile(key));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+key+"\"").body(resource);
    }
}
