package gae.piaz.logical.delete.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gae.piaz.logical.delete.config.SqlDeleteStatementInspector;
import gae.piaz.logical.delete.config.TransactionalDeleted;
import gae.piaz.logical.delete.domain.Author;
import gae.piaz.logical.delete.domain.repository.AuthorRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public void deleteAuthorEffective(Integer id, String reason) {
        SqlDeleteStatementInspector.setDeleteReason(reason);
        authorRepository.deleteById(id);
    }

    public void deleteAuthorBasic(Integer id, String reason) {
        authorRepository.findById(id)
            .ifPresent(author -> {
                author.setDeletedReason(reason);
                author.setDeletedAt(LocalDateTime.now());
                author.setDeletedBy("get current user");
                authorRepository.save(author);
            });
    }

    public Author fetchDeletedAuthorBasic(Integer id) {
        Optional<Author> deletedAuthOpt = authorRepository.findByIdDeleted(id);
        return deletedAuthOpt.orElseThrow();
    }

    @TransactionalDeleted
    public Author fetchDeletedAuthorEffective(Integer id) {
        return authorRepository.findById(id)
            .orElseThrow();
    }
}
