package gae.piaz.logical.delete.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "authors")
@Getter
@Setter
@ToString
@SQLDelete(
        sql =
                "UPDATE authors SET deleted_at = CURRENT_TIMESTAMP, deleted_by = :current_user, "
                        + "deleted_reason = :deleted_reason where id = ? AND database_version = ?")
@SQLRestriction("deleted_at is null")
public class Author extends AbstractEntity {

    private String firstName;

    private String lastName;
}
