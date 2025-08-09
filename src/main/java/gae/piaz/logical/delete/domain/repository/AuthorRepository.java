package gae.piaz.logical.delete.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gae.piaz.logical.delete.domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "SELECT * FROM authors WHERE id = ? and deleted_at is not null", nativeQuery = true)
    Optional<Author> findByIdDeleted(Integer id);
}
