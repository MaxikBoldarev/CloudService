package ru.netology.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.model.JwtRequest;
import ru.netology.model.Role;
import ru.netology.service.AuthService;
import ru.netology.service.CloudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CloudController {


    private final CloudService cloudService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final JwtRequest loginRequest) throws AuthException, JsonProcessingException {
        authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @PostMapping("/file")
    public ResponseEntity<?> saveFile(@RequestBody MultipartFile multipartfile) {
        cloudService.putFile(multipartfile);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get")
    public ResponseEntity<byte[]> get(@PathVariable String fileName) {
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(cloudService.getFile(fileName));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        cloudService.deleteFile(fileName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Role>> fileList() {
        List<Role> list = cloudService.getAllFiles();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping("/put")
    public ResponseEntity<Role> editFileName(@RequestParam("filename") String fileName, String newFileName) {
        cloudService.editFileName(fileName, newFileName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
