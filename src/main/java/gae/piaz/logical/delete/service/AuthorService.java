package gae.piaz.logical.delete.service;

import gae.piaz.logical.delete.config.CustomStatementInspector;
import gae.piaz.logical.delete.domain.repository.AuthorRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CustomStatementInspector customStatementInspector;

    public void deleteAuthorEffective(Integer id, String reason) {
        customStatementInspector.setDeleteReason(reason);
        authorRepository.deleteById(id);
    }

    public void deleteAuthorBasic(Integer id, String reason) {
        authorRepository
                .findById(id)
                .ifPresent(
                        author -> {
                            author.setDeletedReason(reason);
                            author.setDeletedAt(LocalDateTime.now());
                            author.setDeletedBy("get current user");
                            authorRepository.save(author);
                        });
    }
}
