package gae.piaz.logical.delete.config;

import java.util.UUID;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class CustomStatementInspector implements StatementInspector {

    private static final ThreadLocal<String> deleteReason = ThreadLocal.withInitial(() -> "none");

    public void clear() {
        deleteReason.remove();
    }

    public void setDeleteReason(String reason) {
        deleteReason.set(reason);
    }

    @Override
    public String inspect(String sql) {
        if (sql.contains(":current_user")) {
            String userId = getCurrentUserId();
            sql = sql.replace(":current_user", "'" + userId + "'");
        }
        if (sql.contains(":deleted_reason")) {
            String reason = deleteReason.get();
            sql = sql.replace(":deleted_reason", "'" + reason + "'");
        }
        return sql;
    }

    private String getCurrentUserId() {
        // if using spring security, get the current user id from the security context
        return UUID.randomUUID().toString();
    }
}
