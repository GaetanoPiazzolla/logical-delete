package gae.piaz.logical.delete.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a method within an archived transaction context.
 *
 * <p>When applied to a method, this annotation causes the method to be executed within an {@link
 * DeletedTransactionTemplate}, which automatically sets the SQL restriction mode to access also
 * logically deleted entities.
 *
 * <p>Usage example:
 *
 * <pre>
 * {@code @TransactionalDeleted}
 * public SomeEntity findArchivedEntity(Long id) {
 *     return repository.findById(id);
 * }
 * </pre>
 *
 * @see TransactionalDeletedAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionalDeleted {}
