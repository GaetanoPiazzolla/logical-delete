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

    @DeleteMapping("right/{id}/{reason}")
    public void deleteAuthorRight(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorRight(id, reason);
    }

    @DeleteMapping("wrong/{id}/{reason}")
    public void deleteAuthorWrong(@PathVariable Integer id, @PathVariable String reason) {
        authorService.deleteAuthorWrong(id, reason);
    }
}
