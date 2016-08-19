package me.quanli.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import javax.annotation.Resource;

import junit.framework.TestCase;
import me.quanli.commons.util.PropertiesUtils;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("test")
@WebAppConfiguration
public abstract class AbstractTestCase extends TestCase {

    protected MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private void buildDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(PropertiesUtils.getProperty("dataSource.url"));
        dataSource.setUser(PropertiesUtils.getProperty("dataSource.user"));
        dataSource.setPassword(PropertiesUtils
                .getProperty("dataSource.password"));
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected void buildDatabase() {
        // noop
    }

    protected void executeSqlFile(String fileName) {
        if (jdbcTemplate == null) {
            buildDataSource();
        }
        String query = "";
        try {
            InputStream input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(fileName);
            LineNumberReader fileReader = new LineNumberReader(
                    new InputStreamReader(input));
            query = ScriptUtils.readScript(fileReader,
                    ScriptUtils.DEFAULT_COMMENT_PREFIX,
                    ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getJdbcTemplate().batchUpdate(query.split(";"));
    }

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            buildDataSource();
        }
        return jdbcTemplate;
    }

}
