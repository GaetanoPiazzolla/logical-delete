package gae.piaz.logical.delete;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gae.piaz.logical.delete.controller.AuthorController;
import gae.piaz.logical.delete.domain.Author;
import gae.piaz.logical.delete.domain.repository.AuthorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FetchingDeletedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(strings = { "/basic", "/effective" })
    void testFetchDeletedAuthor(String endpoint) throws Exception {

        Author author = createAndSaveAuthor();
        authorRepository.delete(author);

        String response = mockMvc.perform(get("/api/authors" + endpoint + "/deleted/" + author.getId()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        AuthorController.AuthorDTO dto = objectMapper.readValue(response, AuthorController.AuthorDTO.class);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.firstName());
        Assertions.assertNotNull(dto.lastName());
    }

    private Author createAndSaveAuthor() {
        Author author = new Author();
        author.setFirstName(Instancio.of(String.class)
            .create());
        author.setLastName(Instancio.of(String.class)
            .create());
        author.setDatabaseVersion(0);
        author = authorRepository.save(author);
        return author;
    }
}
