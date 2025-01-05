package gae.piaz.logical.delete.domain.repository;

import gae.piaz.logical.delete.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "SELECT * FROM authors WHERE id = ?", nativeQuery = true)
    Optional<Author> findByIdIncludingDeleted(Integer id);

}
