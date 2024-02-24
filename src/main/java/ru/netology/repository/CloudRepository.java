package ru.netology.repository;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.entity.CloudFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Transactional
@Repository
public class CloudRepository {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public CloudRepository(UserRepository userRepository,
                           FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    private Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public void login(String authToken, UserDetails userPrincipal) {
        tokenStorage.put(authToken, userPrincipal);
    }

    public Optional<UserDetails> logout(String authToken) {
        return ofNullable(tokenStorage.remove(authToken));
    }

    public Optional<CloudFile> uploadFile(CloudFile cloudFile, String authToken) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            cloudFile.setUserId(userId.get());
            return Optional.of(fileRepository.save(cloudFile));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public void deleteFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            fileRepository.removeByUserIdAndName(userId.get(), fileName);
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public Optional<CloudFile> downloadFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            return fileRepository.findByUserIdAndName(userId.get(), fileName);
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public Optional<CloudFile> renameFile(String authToken, String fileName, String newFileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            Optional<CloudFile> cloudFile = fileRepository.findByUserIdAndName(userId.get(), fileName);
            cloudFile.ifPresent(file -> file.setName(newFileName));
            return Optional.of(fileRepository.save(Objects.requireNonNull(cloudFile.orElse(null))));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }


    public Optional<List<CloudFile>> getFiles(String authToken, int limit) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            return ofNullable(fileRepository.findAllByUserIdWithLimit(userId.get(), limit));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    private Optional<Long> getUserId(String authToken) {
        UserDetails user = tokenStorage.get(authToken.substring(7));
        return user != null ? ofNullable(Objects.requireNonNull(userRepository.findByLogin(user.getUsername()).orElse(null)).getId()) : Optional.empty();
    }
}
