package gae.piaz.logical.delete.config;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A specialized TransactionTemplate that executes operations in deleted mode context. This template
 * automatically handles switching between normal and deleted modes, ensuring proper cleanup even in
 * case of exceptions.
 *
 * <p>Usage example:
 *
 * <pre>
 * deletedTransactionTemplate.execute(status -> {
 *     return repository.findDeletedEntities();
 * });
 * </pre>
 */
public class DeletedTransactionTemplate extends TransactionTemplate {

    /**
     * Creates a new DeletedTransactionTemplate.
     *
     * @param transactionManager the transaction manager to use for this template
     */
    public DeletedTransactionTemplate(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    /**
     * Executes the given action in a deleted mode transaction context. The deleted mode is
     * automatically set before execution and reset afterward. Note: Prefer the optional variant for
     * non collections.
     *
     * @param action the callback object that specifies the transactional action
     * @return a result object returned by the callback, or null if none
     */
    @Override
    public <T> T execute(@NonNull TransactionCallback<T> action) {
        return executeWithDeletedMode(() -> super.execute(action));
    }

    /**
     * Executes the given action in a deleted mode transaction context. The deleted mode is
     * automatically set before execution and reset afterward.
     *
     * @param action the callback object that specifies the transactional action
     * @return an Optional with the results of the execution
     */
    public <T> Optional<T> executeOptional(@NonNull TransactionCallback<T> action) {
        return Optional.ofNullable(executeWithDeletedMode(() -> super.execute(action)));
    }

    private <T> T executeWithDeletedMode(Supplier<T> supplier) {
        SQLRestrictionStatementInspector.setDeletedMode(SQLRestrictionStatementInspector.DeletedMode.DELETED);
        try {
            return supplier.get();
        } finally {
            SQLRestrictionStatementInspector.setDeletedMode(SQLRestrictionStatementInspector.DeletedMode.NORMAL);
        }
    }
}
