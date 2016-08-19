package me.quanli.report.sql;

public class SqlException extends RuntimeException {

    private static final long serialVersionUID = -8953365764098204668L;

    public SqlException(String message) {
        super(message);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlException(Throwable cause) {
        super(cause);
    }

}
