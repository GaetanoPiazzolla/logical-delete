package gae.piaz.logical.delete.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect that intercepts methods annotated with {@link TransactionalDeleted} and executes them
 * within an deleted transaction context.
 *
 * <p>This aspect automatically wraps the annotated method execution in an {@link
 * DeletedTransactionTemplate#execute(TransactionCallback)} call, which ensures that the method can
 * access deleted (soft-deleted) entities.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionalDeletedAspect {

    private final DeletedTransactionTemplate deletedTransactionTemplate;

    /**
     * Around advice that executes methods annotated with {@link TransactionalDeleted} within a
     * deleted transaction context.
     *
     * @param joinPoint the method execution join point
     * @param annotation the TransactionalDeleted annotation
     * @return the result of the method execution
     */
    @Around("@annotation(annotation)")
    public Object executeInDeletedTransaction(ProceedingJoinPoint joinPoint, TransactionalDeleted annotation) {

        log.trace("Executing method {} in deleted transaction context", joinPoint.getSignature()
            .toShortString());

        return deletedTransactionTemplate.execute(_ -> {
            try {
                return joinPoint.proceed();
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException("Error executing method in deleted transaction", e);
            }
        });
    }
}
