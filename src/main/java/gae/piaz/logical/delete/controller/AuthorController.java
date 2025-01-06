package gae.piaz.logical.delete.controller;

import gae.piaz.logical.delete.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @DeleteMapping("basic/{id}/{reason}")
    public void deleteAuthorBasic(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorBasic(id, reason);
    }

    @DeleteMapping("effective/{id}/{reason}")
    public void deleteAuthorEffective(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorEffective(id, reason);
    }
}
