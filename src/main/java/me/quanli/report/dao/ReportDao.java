package me.quanli.report.dao;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import me.quanli.commons.dao.HibernateDao;
import me.quanli.commons.dao.SessionFactoryBuilder;
import me.quanli.commons.util.PropertiesUtils;

/**
 * data access object for ReportDB Database
 * 
 * @author Quan Li
 *
 */
@Component
public class ReportDao extends HibernateDao {

    private static final Log LOGGER = LogFactory.getLog(ReportDao.class);

    @PostConstruct
    public void initSessionFactory() {
        String url = PropertiesUtils.getProperty("dataSource.url");
        if (StringUtils.isEmpty(url)) {
            LOGGER.info(getClass() + "由于没有找到配置参数,未装配完整");
            return;
        }
        SessionFactoryBuilder sfb = new SessionFactoryBuilder();
        sfb.setUrl(url);
        sfb.setUsername(PropertiesUtils.getProperty("dataSource.user"));
        sfb.setPassword(PropertiesUtils.getProperty("dataSource.password"));
        sfb.setPackagesToScan(new String[] { "me.quanli.report.**.entity" });
        sfb.setHbm2ddl(PropertiesUtils
                .getProperty("dataSource.hbm2ddl", "none"));
        sfb.setShowSql(PropertiesUtils.getProperty("dataSource.show_sql",
                "false"));
        SessionFactory sf = sfb.buildSessionFactory();
        setSessionFactory(sf);
    }

}