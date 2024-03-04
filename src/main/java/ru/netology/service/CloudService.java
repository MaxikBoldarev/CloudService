package ru.netology.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ru.netology.DTO.FileResponse;
import ru.netology.exeption.RequestErrors;
import ru.netology.exeption.UnauthorizedException;
import ru.netology.model.File;
import ru.netology.model.User;
import ru.netology.repository.AuthorizationRepository;
import ru.netology.repository.CloudRepository;
import ru.netology.repository.UserRepository;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudService {

    CloudRepository fileRepository;
    AuthorizationRepository authorizationRepository;
    UserRepository userRepository;

    @Transactional
    public void putFile(String authToken, String fileName, MultipartFile multipartFile) {
        final User user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized error");
        }

        try {
            fileRepository.save(new File(fileName, multipartFile.getSize(), multipartFile.getContentType(), multipartFile.getBytes(), user));
            log.info("Файл загружен");
        } catch (IOException | RuntimeException e) {
            log.info("Ошибка загрузки файла на ресурс");
            throw new RequestErrors(e.getMessage());
        }

    }

    @Transactional
    public void deleteFile(String authToken, String fileName) throws RequestErrors {
        final User user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized error");
        }

        File file = fileRepository.findByFileName(user, fileName).orElseThrow(() -> {
            log.info("Ошибка в удалении файла");
            return new RequestErrors("Такого файла не существует");
        });
        fileRepository.delete(file);
        log.info("Файл удален");

    }

    @Transactional
    public byte[] getFile(String authToken, String fileName) throws RequestErrors {
        final User user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized error");
        }

        File file = fileRepository.findByFileName(user, fileName).orElseThrow(() -> {
            log.info("Ошибка получения файла из ресурса");
            return new RequestErrors("Такого файла не существует");
        });
        return file.getContent();

    }


    @Transactional
    public List<FileResponse> getAllFiles(String authToken) throws RequestErrors {
        final User user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized error");
        }
        log.info("Cписок получен");
        return fileRepository.findAllByUser(user, Sort.by("filename")).stream()
                .map(f -> new FileResponse(f.getFilename(), f.getSize()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void editFileName(String authToken, String fileName, String newFileName) throws RequestErrors {
        final User user = getUser(authToken);
        if (user == null) {
            log.error("Edit file error");
            throw new UnauthorizedException("Unauthorized error");
        }

        File file = fileRepository.findByFileName(user, fileName).orElseThrow(() -> {
            log.info("Ошибка в изменении имени файла");
            return new RequestErrors("Такого файла не существует");
        });

        file.setFilename(newFileName);
        fileRepository.save(file);
        log.info("Название файла изменено");
    }

    private User getUser(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final String username = authorizationRepository.getUserNameByToken(authToken);
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized error"));
    }
}