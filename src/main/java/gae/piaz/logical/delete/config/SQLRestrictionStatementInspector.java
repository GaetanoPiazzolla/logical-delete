package gae.piaz.logical.delete.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * This class is used to customize the @SQLRestriction that we have on entities, removing the
 * filters in certain conditions.
 */
@Slf4j
public class SQLRestrictionStatementInspector implements StatementInspector {

    private static final Pattern archivedPattern =
            Pattern.compile("(\\b\\w+\\.)?deleted_at\\s+is\\s+null", Pattern.CASE_INSENSITIVE);

    public enum DeletedMode {
        NORMAL, // Keep default behavior - deleted_at is null
        DELETED, // Show only logically deleted - deleted_at is not null
        ALL // Query both deleted and not deleted
    }

    private static final ThreadLocal<DeletedMode> currentDeletedMode =
            ThreadLocal.withInitial(() -> DeletedMode.NORMAL);

    public static void setDeletedMode(DeletedMode mode) {
        log.trace("Switching DeleteMode to {}", mode);
        currentDeletedMode.set(mode);
    }

    public static void clear() {
        currentDeletedMode.remove();
    }

    @Override
    public String inspect(String sql) {
        if (currentDeletedMode.get() == DeletedMode.DELETED) {
            sql = sql.replaceAll("(?i)deleted_at\\s+is\\s+null", "deleted_at is not null");
        } else if (currentDeletedMode.get() == DeletedMode.ALL) {
            Matcher archivedMatcher = archivedPattern.matcher(sql);
            sql = archivedMatcher.replaceAll("1=1");
        }
        // normal
        return sql;
    }
}
