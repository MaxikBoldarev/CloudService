package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.exeption.RequestErrors;
import ru.netology.model.Role;
import ru.netology.model.User;
import ru.netology.repository.CloudRepository;
import ru.netology.service.CloudService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest(classes = CloudService.class)
class RoleServiceTest {

    @Mock
    CloudRepository repository;
    @InjectMocks
    CloudService cloudService;
    static byte[] BYTES = {6, 6, 6};
    static final String FILENAME_1 = "file1";
    static final String FILENAME_2 = "file2";


    @Test
    void getFile() {
        byte[] savedFile = {1, 2, 3};
        when(repository.findByFileName("file")).thenReturn(Optional.of(new Role(1L, "file", "type", "path", savedFile, new User())));
        byte[] file = cloudService.getFile("file");
        assertArrayEquals(savedFile, file);
    }

    @Test
    void getFiles() {
        List<Role> respond = new ArrayList<>();
        respond.add(new Role(1L, "file", "path", "type", BYTES, new User()));
        respond.add(new Role(2L, "file2", "path", "type", BYTES, new User()));

        List<Role> request = new ArrayList<>();
        request.add(new Role(1L, "file", "path", "type", BYTES, new User()));
        request.add(new Role(2L, "file2", "path", "type", BYTES, new User()));
        when(repository.findAll()).thenReturn(request);
        List<Role> result = cloudService.getAllFiles();
        assertThat(respond, is(result));
    }

    @Test
    void removeFile() {
        when(repository.findByFileName(FILENAME_1)).thenReturn(Optional.empty());
        Assertions.assertThrows(RequestErrors.class, () -> cloudService.deleteFile(FILENAME_1));
    }

    @Test
    void updateFile() {
        when(repository.findByFileName(FILENAME_1)).thenReturn(Optional.empty());
        Assertions.assertThrows(RequestErrors.class, () -> cloudService.editFileName(FILENAME_1, FILENAME_2));
    }
}
