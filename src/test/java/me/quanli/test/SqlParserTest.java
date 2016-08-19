package me.quanli.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;

import me.quanli.report.sql.SqlHelper;
import net.sf.jsqlparser.JSQLParserException;

@RunWith(JUnit4.class)
public class SqlParserTest {

    @Test
    public void test() throws JSQLParserException {

        String sql = "SELECT sum(1,2), avs as ll, 1+1, (select 1) as c, abc, ase as hiy "
                + " FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
                + " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";

        System.out.println(JSON.toJSONString(SqlHelper.getColumnNames(sql)));

    }

    @Test
    public void testSubList() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        System.out.println(JSON.toJSONString(list.subList(10, 10 + 4)));
    }

}
