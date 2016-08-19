package me.quanli.report.sql;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

public class SqlHelper {

    private static final Log LOGGER = LogFactory.getLog(SqlHelper.class);

    public static List<String> getColumnNames(String sql) {
        LOGGER.info("start parsing...");
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new SqlException("error occurs when parsing sql", e);
        }
        if (!(statement instanceof Select)) {
            throw new SqlException("statement is not a select statement");
        }
        Select select = (Select) statement;
        GetAliasesVisitor gav = new GetAliasesVisitor();
        select.getSelectBody().accept(gav);
        return gav.getAliases();
    }

}
