package gae.piaz.logical.delete.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gae.piaz.logical.delete.domain.Author;
import gae.piaz.logical.delete.service.AuthorService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    public record AuthorDTO(String firstName, String lastName) {

    }

    @DeleteMapping("basic/{id}/{reason}")
    public void deleteAuthorBasic(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorBasic(id, reason);
    }

    @DeleteMapping("effective/{id}/{reason}")
    public void deleteAuthorEffective(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorEffective(id, reason);
    }

    @GetMapping("basic/deleted/{id}")
    public AuthorDTO getDeletedAuthorBasic(@PathVariable Integer id) {
        Author author = authorService.fetchDeletedAuthorBasic(id);
        return new AuthorDTO(author.getFirstName(), author.getLastName());
    }

    @GetMapping("effective/deleted/{id}")
    public AuthorDTO getDeletedAuthorEffective(@PathVariable Integer id) {
        Author author = authorService.fetchDeletedAuthorEffective(id);
        return new AuthorDTO(author.getFirstName(), author.getLastName());
    }

}
