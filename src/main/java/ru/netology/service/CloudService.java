package ru.netology.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ru.netology.exeption.RequestErrors;
import ru.netology.model.Role;
import ru.netology.repository.CloudRepository;


import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudService {

    private final CloudRepository fileRepository;

    @Transactional
    public void putFile(MultipartFile multipartFile) {

        try {
            fileRepository.save(Role.builder().
                    fileName(multipartFile.getOriginalFilename()).
                    fileType(multipartFile.getContentType()).
                    fileSize(multipartFile.getBytes()).
                    build());
            log.info("Файл загружен");
        } catch (IOException | RuntimeException e) {
            log.info("Ошибка загрузки файла на ресурс");
            throw new RequestErrors(e.getMessage());
        }

    }

    @Transactional
    public void deleteFile(String fileName) throws RequestErrors {

        Role file = fileRepository.findByFileName(fileName).orElseThrow(() -> {
            log.info("Ошибка в удалении файла");
            return new RequestErrors("Такого файла не существует");
        });
        fileRepository.delete(file);
        log.info("Файл удален");

    }

    @Transactional
    public byte[] getFile(String fileName) throws RequestErrors {

        Role file = fileRepository.findByFileName(fileName).orElseThrow(() -> {
            log.info("Ошибка получения файла из ресурса");
            return new RequestErrors("Такого файла не существует");
        });
        return file.getFileSize();

    }


    @Transactional
    public List<Role> getAllFiles() throws RequestErrors {

        List<Role> files = fileRepository.findAll();
        if (files.isEmpty()) {
            log.info("Ошибка в получение списка файлов из ресурса");
            throw new RequestErrors("Список пуст");
        } else {
            log.info("Cписок получен");
            return List.copyOf(files);
        }

    }

    @Transactional
    public void editFileName(String fileName, String newFileName) throws RequestErrors {

        Role file = fileRepository.findByFileName(fileName).orElseThrow(() -> {
            log.info("Ошибка в изменении имени файла");
            return new RequestErrors("Такого файла не существует");
        });

        file.setFileName(newFileName);
        fileRepository.save(file);
        log.info("Название файла изменено");
    }

}