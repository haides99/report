package me.quanli.commons.dao;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public abstract class HibernateDao implements Dao {

    private static final Log LOGGER = LogFactory.getLog(HibernateDao.class);

    private SessionFactory sessionFactory;

    private ThreadLocal<Session> localSession = new ThreadLocal<Session>();

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public Session currentSession() {
        Session session = localSession.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            localSession.set(session);
        }
        return session;
    }

    private boolean closeSessionIfNecessary() {
        Session session = localSession.get();
        if (session != null && session.isOpen() && !session.getTransaction().isActive()) {
            session.close();
            return true;
        }
        return false;
    }

    public void doInTx(Tx callback) {
        Transaction tx = currentSession().getTransaction();
        boolean isAlreadyActive = tx.isActive();
        if (!isAlreadyActive) {
            tx.begin();
        }
        try {
            callback.exec();
            if (!isAlreadyActive) {
                tx.commit();
            }
        } catch (Exception e) {
            if (!isAlreadyActive) {
                LOGGER.info("rollback for " + ExceptionUtils.getStackTrace(e));
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> void save(final T entity) {
        doInTx(new Tx() {
            public void exec() {
                currentSession().saveOrUpdate(entity);
            }
        });
    }

    /**
     * 一次连接,保存多个实体
     *
     * @param entitys
     */
    public <T> void saveAll(final Collection<T> entitys) {
        doInTx(new Tx() {
            public void exec() {
                for (T entity : entitys) {
                    currentSession().saveOrUpdate(entity);
                }
            }
        });
    }

    public <T> void update(final T entity) {
        doInTx(new Tx() {
            public void exec() {
                currentSession().update(entity);
            }
        });
    }

    public void delete(final Object entity) {
        doInTx(new Tx() {
            public void exec() {
                currentSession().delete(entity);
            }
        });
    }

    /**
     * 一次连接,删除多个实体
     *
     * @param entitys
     */
    public <T> void deleteAll(final Collection<T> entitys) {
        doInTx(new Tx() {
            public void exec() {
                for (Object entity : entitys) {
                    currentSession().delete(entity);
                }
            }
        });
    }

    public int bulkUpdate(final String hql, final Object... values) {
        final int[] cnt = new int[]{0};
        doInTx(new Tx() {
            public void exec() {
                Query query = currentSession().createQuery(hql);
                setQueryParam(query, values);
                cnt[0] = query.executeUpdate();
            }
        });
        return cnt[0];
    }

    public int bulkUpdateBySQL(final String sql, final Object... values) {
        final int[] cnt = new int[]{0};
        doInTx(new Tx() {
            public void exec() {
                Query query = currentSession().createSQLQuery(sql);
                setQueryParam(query, values);
                cnt[0] = query.executeUpdate();
            }
        });
        return cnt[0];
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        if (id == null) {
            return null;
        }
        try {
            return (T) currentSession().get(clazz, id);
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> T findSingle(String hql, Object... values) {
        try {
            Query query = currentSession().createQuery(hql);
            setQueryParam(query, values);
            return (T) query.uniqueResult();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> find(String hql, Object... values) {
        try {
            Query query = currentSession().createQuery(hql);
            setQueryParam(query, values);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> findWithLimit(String hql, Integer offset, Integer limit, Object... values) {
        try {
            Query query = currentSession().createQuery(hql);
            setQueryParam(query, values);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public long findCount(String hql, Object... values) {
        try {
            Query countQuery = currentSession().createQuery(toCountHQL(hql));
            setQueryParam(countQuery, values);
            Object obj = countQuery.uniqueResult();
            return getCount(obj);
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> T findSingleBySQL(String sql, Object... values) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, values);
            T ret = (T) query.uniqueResult();
            return ret;
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> findBySQL(String sql, Object... values) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, values);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public long findCountBySQL(String sql, Object... values) {
        try {
            Query countQuery = currentSession().createQuery(toCountSQL(sql));
            setQueryParam(countQuery, values);
            Object obj = countQuery.uniqueResult();
            return getCount(obj);
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> findBySQLWithLimit(String sql, Integer offset, Integer limit, Object... values) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, values);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> findBySQL(String sql, Map<String, Object> parameters) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, parameters);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public <T> List<T> findBySQLWithLimit(String sql, Integer offset, Integer limit, Map<String, Object> parameters) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, parameters);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    /**
     * 按照sql对应的字段,查询key,Value对应的值
     *
     * @param sql
     * @param values
     * @return
     */
    public List<Map<String, Object>> findMapListBySQL(String sql, Object... values) {
        try {
            Query query = currentSession().createSQLQuery(sql);
            setQueryParam(query, values);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } finally {
            closeSessionIfNecessary();
        }
    }

    public long getCount(Object obj) {
        if (obj instanceof Object[]) {
            return getCount(((Object[]) obj)[0]);
        }
        if (obj instanceof List) {
            return getCount(((List<?>) obj).get(0));
        }
        if (obj instanceof Number) {
            Number count = (Number) obj;
            return count.longValue();
        }
        return 0;
    }

    public void setQueryParam(Query query, Object... values) {
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
    }

    public void setQueryParam(Query query, Map<String, Object> parameters) {
        if (parameters == null)
            return;
        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    public String toCountHQL(String hql) {
        if (null == hql || hql.trim().equals(""))
            return "";
        String formatQl = hql;
        String pStr = "^\\s*((s|S)(e|E)(l|L)(e|E)(c|C)(t|T))?(.*?)(f|F)(r|R)(o|O)(m|M)\\s";
        String pOrderStr = "\\s*(o|O)(r|R)(d|D)(e|E)(r|R)\\s+(b|B)(y|Y).*$";
        Pattern p = Pattern.compile(pStr, Pattern.DOTALL);
        Matcher m = p.matcher(hql);
        if (m.find()) {
            StringBuffer countHeader = new StringBuffer("SELECT COUNT(*)");
            if (m.group(8) != null && !m.group(8).trim().equals("")) {
                countHeader.append(", " + m.group(8).trim());
            }
            countHeader.append(" FROM ");
            formatQl = formatQl.replaceFirst(pStr, countHeader.toString());
        }
        formatQl = formatQl.replaceFirst(pOrderStr, "");
        return formatQl;
    }

    public String toCountDistinctHQL(String indexString, String hql) {
        if (null == hql || hql.trim().equals(""))
            return "";
        String formatQl = hql;
        String pStr = "^\\s*((s|S)(e|E)(l|L)(e|E)(c|C)(t|T))?(.*?)(f|F)(r|R)(o|O)(m|M)\\s";
        String pOrderStr = "\\s*(o|O)(r|R)(d|D)(e|E)(r|R)\\s+(b|B)(y|Y).*$";
        Pattern p = Pattern.compile(pStr, Pattern.DOTALL);
        Matcher m = p.matcher(hql);
        if (m.find()) {
            StringBuffer countHeader = new StringBuffer(indexString);
            countHeader.append(" FROM ");
            formatQl = formatQl.replaceFirst(pStr, countHeader.toString());
        }
        formatQl = formatQl.replaceFirst(pOrderStr, "");
        return formatQl;
    }

    public String toCountSQL(String sql) {
        if (null == sql || sql.trim().equals(""))
            return "";
        return "SELECT COUNT(*) FROM (" + sql + ") as COUNT";
    }

}