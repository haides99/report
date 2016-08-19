package me.quanli.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.Report;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestReport extends AbstractTestCase {

    private static Integer globalReportId;

    @Resource
    private ReportDao reportDao;

    @Test
    public void test_000_setup() {
    }

    @Test
    public void test_001_createReport() throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", "abc");
        json.put("type", 1);
        String responseStr = mockMvc.perform(post("/report/report").param("reportJson", json.toJSONString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println(responseStr);
        globalReportId = JSON.parseObject(responseStr, Report.class).getId();
    }

    @Test
    public void test_002_retrieveReport() throws Exception {
        String responseStr = mockMvc.perform(get("/report/report/" + globalReportId)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseStr);
    }

    @Test
    public void test_003_updateReport() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", globalReportId);
        json.put("name", "defg");
        json.put("description", "aaa");
        String responseStr = mockMvc
                .perform(put("/report/report/" + globalReportId).param("reportJson", json.toJSONString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println(responseStr);
    }

    @Test
    public void test_004_deleteReport() throws Exception {
        String responseStr = mockMvc.perform(delete("/report/report/" + globalReportId)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseStr);
        String responseStr2 = mockMvc.perform(get("/report/report/" + globalReportId).param("estimator", "admin"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println(responseStr2);
    }

    @Test
    public void test_999_cleanup() {
        reportDao.bulkUpdate("delete from Report where id = ? ", globalReportId);
        System.out.println("cleanup done");
    }

}
