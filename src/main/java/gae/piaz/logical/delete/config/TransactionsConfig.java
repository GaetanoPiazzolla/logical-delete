package gae.piaz.logical.delete.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionsConfig {

    @Bean
    public DeletedTransactionTemplate deletedTransactionTemplate(PlatformTransactionManager transactionManager) {
        return new DeletedTransactionTemplate(transactionManager);
    }

}
