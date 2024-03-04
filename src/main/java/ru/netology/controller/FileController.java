package ru.netology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.DTO.FileResponse;
import ru.netology.service.CloudService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class FileController {

    private final CloudService cloudService;

    @GetMapping("/get")
    public ResponseEntity<byte[]> get(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(cloudService.getFile(authToken, fileName));
    }

    @PostMapping("/file")
    public ResponseEntity<?> saveFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, MultipartFile multipartfile) {
        cloudService.putFile(authToken, fileName, multipartfile);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        cloudService.deleteFile(authToken, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/put")
    public ResponseEntity<?> editFileName(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename,
                                          String newFileName) {
        cloudService.editFileName(authToken, filename, newFileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/list")
    List<FileResponse> getAllFiles(@RequestHeader("auth-token") String authToken) {
        return cloudService.getAllFiles(authToken);
    }
}
