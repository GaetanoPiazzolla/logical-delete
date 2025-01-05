package gae.piaz.logical.delete;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gae.piaz.logical.delete.domain.Author;
import gae.piaz.logical.delete.domain.repository.AuthorRepository;
import java.util.Optional;
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

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LogicalDeleteTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private AuthorRepository authorRepository;

    @ParameterizedTest
    @ValueSource(strings = {"/right", "/wrong"})
    void testDeleteAuthor(String endpoint) throws Exception {

        Author author = createAndSaveAuthor();
        String deleteReason = "He does not write anymore.";

        mockMvc.perform(
                        delete(
                                "/api/authors"
                                        + endpoint
                                        + "/"
                                        + author.getId()
                                        + "/"
                                        + deleteReason))
                .andExpect(status().isOk());

        // query filtered by @SQLRestriction
        Optional<Author> authorOptional = authorRepository.findById(author.getId());
        Assertions.assertTrue(authorOptional.isEmpty());

        // native query, no filter
        Optional<Author> deletedAuthOpt = authorRepository.findByIdIncludingDeleted(author.getId());
        Assertions.assertTrue(deletedAuthOpt.isPresent());

        Assertions.assertNotNull(deletedAuthOpt.get());
        Assertions.assertNotNull(deletedAuthOpt.get().getDeletedAt());
        Assertions.assertNotNull(deletedAuthOpt.get().getDeletedBy());
        Assertions.assertNotNull(deletedAuthOpt.get().getDeletedReason());
    }

    private Author createAndSaveAuthor() {
        Author author = new Author();
        author.setFirstName(Instancio.of(String.class).create());
        author.setLastName(Instancio.of(String.class).create());
        author.setDatabaseVersion(0);
        author = authorRepository.save(author);
        return author;
    }
}
