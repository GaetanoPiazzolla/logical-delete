package gae.piaz.logical.delete.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;

// Referenced in application.yaml
public class CompositeInspector implements StatementInspector {

    private final SqlDeleteStatementInspector sqlDeleteStatementInspector = new SqlDeleteStatementInspector();
    private final SQLRestrictionStatementInspector sqlRestrictionStatementInspector = new SQLRestrictionStatementInspector();

    @Override
    public String inspect(String sql) {
        sql = sqlDeleteStatementInspector.inspect(sql);
        sql = sqlRestrictionStatementInspector.inspect(sql);
        return sql;
    }
}
