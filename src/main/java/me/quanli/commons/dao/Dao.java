package me.quanli.commons.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Dao {

    public <T> void save(T entity);

    public <T> void update(T entity);

    public void delete(Object entity);

    public int bulkUpdate(String hql, Object... values);

    public int bulkUpdateBySQL(final String sql, final Object... values);

    public <T> T get(Class<T> clazz, Serializable id);

    public <T> T findSingle(String hql, Object... values);

    public <T> List<T> find(String hql, Object... values);

    public <T> List<T> findWithLimit(String hql, Integer offset, Integer limit, Object... values);

    public long findCount(String hql, Object... values);

    public <T> T findSingleBySQL(String sql, Object... values);

    public <T> List<T> findBySQL(String sql, Object... values);

    public <T> List<T> findBySQL(String sql, Map<String, Object> parameters);

    public long findCountBySQL(String sql, Object... values);

    public <T> List<T> findBySQLWithLimit(String sql, Integer offset, Integer limit, Object... values);

    public <T> List<T> findBySQLWithLimit(String sql, Integer offset, Integer limit, Map<String, Object> parameters);

    public void doInTx(Tx tx);

    public static interface Tx {

        void exec();

    }

}
