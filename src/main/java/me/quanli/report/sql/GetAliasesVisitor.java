package me.quanli.report.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

public class GetAliasesVisitor implements ExpressionVisitor, SelectVisitor, SelectItemVisitor {

    private static final Log LOGGER = LogFactory.getLog(GetAliasesVisitor.class);

    private int index = 0;

    private List<String> aliases = new ArrayList<String>();

    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        if (selectExpressionItem.getAlias() != null) {
            aliases.add(selectExpressionItem.getAlias().getName());
        } else {
            LOGGER.info("no alias for column " + index);
            selectExpressionItem.getExpression().accept(this);
        }
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        index = 0;
        aliases = new ArrayList<String>();
        for (SelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
            index++;
        }
    }

    @Override
    public void visit(SetOperationList setOpList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(WithItem withItem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(NullValue nullValue) {
        LOGGER.info("NullValue in column " + index);
        aliases.add(getExpressionString(nullValue));
    }

    @Override
    public void visit(Function function) {
        LOGGER.info("Function in column " + index);
        aliases.add(getExpressionString(function));
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        LOGGER.info("SignedExpression in column " + index);
        aliases.add(getExpressionString(signedExpression));
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        LOGGER.info("JdbcParameter in column " + index);
        aliases.add(getExpressionString(jdbcParameter));
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        LOGGER.info("JdbcNamedParameter in column " + index);
        aliases.add(getExpressionString(jdbcNamedParameter));
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        LOGGER.info("DoubleValue in column " + index);
        aliases.add(getExpressionString(doubleValue));
    }

    @Override
    public void visit(LongValue longValue) {
        LOGGER.info("LongValue in column " + index);
        aliases.add(getExpressionString(longValue));
    }

    @Override
    public void visit(DateValue dateValue) {
        LOGGER.info("DateValue in column " + index);
        aliases.add(getExpressionString(dateValue));
    }

    @Override
    public void visit(TimeValue timeValue) {
        LOGGER.info("TimeValue in column " + index);
        aliases.add(getExpressionString(timeValue));
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        LOGGER.info("TimestampValue in column " + index);
        aliases.add(getExpressionString(timestampValue));
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        LOGGER.info("Parenthesis in column " + index);
        aliases.add(getExpressionString(parenthesis));
    }

    @Override
    public void visit(StringValue stringValue) {
        LOGGER.info("StringValue in column " + index);
        aliases.add(getExpressionString(stringValue));
    }

    @Override
    public void visit(Addition addition) {
        LOGGER.info("Addition in column " + index);
        aliases.add(getExpressionString(addition));
    }

    @Override
    public void visit(Division division) {
        LOGGER.info("Division in column " + index);
        aliases.add(getExpressionString(division));
    }

    @Override
    public void visit(Multiplication multiplication) {
        LOGGER.info("Multiplication in column " + index);
        aliases.add(getExpressionString(multiplication));
    }

    @Override
    public void visit(Subtraction subtraction) {
        LOGGER.info("Subtraction in column " + index);
        aliases.add(getExpressionString(subtraction));
    }

    @Override
    public void visit(AndExpression andExpression) {
        LOGGER.info("AndExpression in column " + index);
        aliases.add(getExpressionString(andExpression));
    }

    @Override
    public void visit(OrExpression orExpression) {
        LOGGER.info("OrExpression in column " + index);
        aliases.add(getExpressionString(orExpression));
    }

    @Override
    public void visit(Between between) {
        LOGGER.info("Between in column " + index);
        aliases.add(getExpressionString(between));
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        LOGGER.info("EqualsTo in column " + index);
        aliases.add(getExpressionString(equalsTo));
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        LOGGER.info("GreaterThan in column " + index);
        aliases.add(getExpressionString(greaterThan));
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        LOGGER.info("GreaterThanEquals in column " + index);
        aliases.add(getExpressionString(greaterThanEquals));
    }

    @Override
    public void visit(InExpression inExpression) {
        LOGGER.info("InExpression in column " + index);
        aliases.add(getExpressionString(inExpression));
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        LOGGER.info("IsNullExpression in column " + index);
        aliases.add(getExpressionString(isNullExpression));
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        LOGGER.info("LikeExpression in column " + index);
        aliases.add(getExpressionString(likeExpression));
    }

    @Override
    public void visit(MinorThan minorThan) {
        LOGGER.info("MinorThan in column " + index);
        aliases.add(getExpressionString(minorThan));
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        LOGGER.info("MinorThanEquals in column " + index);
        aliases.add(getExpressionString(minorThanEquals));
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        LOGGER.info("NotEqualsTo in column " + index);
        aliases.add(getExpressionString(notEqualsTo));
    }

    @Override
    public void visit(Column tableColumn) {
        LOGGER.info("using column name as alias for column " + index);
        aliases.add(tableColumn.getColumnName());
    }

    @Override
    public void visit(SubSelect subSelect) {
        throw new SqlException("sub select must have an alias, statement: " + subSelect.toString());
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        LOGGER.info("CaseExpression in column " + index);
        aliases.add(getExpressionString(caseExpression));
    }

    @Override
    public void visit(WhenClause whenClause) {
        LOGGER.info("WhenClause in column " + index);
        aliases.add(getExpressionString(whenClause));
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        LOGGER.info("ExistsExpression in column " + index);
        aliases.add(getExpressionString(existsExpression));
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        LOGGER.info("AllComparisonExpression in column " + index);
        aliases.add(getExpressionString(allComparisonExpression));
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        LOGGER.info("AnyComparisonExpression in column " + index);
        aliases.add(getExpressionString(anyComparisonExpression));
    }

    @Override
    public void visit(Concat concat) {
        LOGGER.info("Concat in column " + index);
        aliases.add(getExpressionString(concat));
    }

    @Override
    public void visit(Matches matches) {
        LOGGER.info("Matches in column " + index);
        aliases.add(getExpressionString(matches));
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        LOGGER.info("BitwiseAnd in column " + index);
        aliases.add(getExpressionString(bitwiseAnd));
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        LOGGER.info("BitwiseOr in column " + index);
        aliases.add(getExpressionString(bitwiseOr));
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        LOGGER.info("BitwiseXor in column " + index);
        aliases.add(getExpressionString(bitwiseXor));
    }

    @Override
    public void visit(CastExpression cast) {
        LOGGER.info("CastExpression in column " + index);
        aliases.add(getExpressionString(cast));
    }

    @Override
    public void visit(Modulo modulo) {
        LOGGER.info("Modulo in column " + index);
        aliases.add(getExpressionString(modulo));
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        LOGGER.info("AnalyticExpression in column " + index);
        aliases.add(getExpressionString(aexpr));
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        LOGGER.info("ExtractExpression in column " + index);
        aliases.add(getExpressionString(eexpr));
    }

    @Override
    public void visit(IntervalExpression iexpr) {
        LOGGER.info("IntervalExpression in column " + index);
        aliases.add(getExpressionString(iexpr));
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
        LOGGER.info("OracleHierarchicalExpression in column " + index);
        aliases.add(getExpressionString(oexpr));
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
        LOGGER.info("RegExpMatchOperator in column " + index);
        aliases.add(getExpressionString(rexpr));
    }

    private String getExpressionString(Object expression) {
        return expression.toString();
    }

}
