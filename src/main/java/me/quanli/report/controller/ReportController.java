package me.quanli.report.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.quanli.commons.mail.Attachment;
import me.quanli.commons.mail.MailSender;
import me.quanli.commons.mail.MailSenderConfig;
import me.quanli.commons.table.PageData;
import me.quanli.commons.util.ExcelUtils;
import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.EmailAddress;
import me.quanli.report.entity.Report;
import me.quanli.report.entity.ReportColumn;
import me.quanli.report.entity.ReportSend;
import me.quanli.report.service.ExecuteService;
import me.quanli.report.service.ReportService;
import me.quanli.report.sql.SqlHelper;
import me.quanli.report.view.ReportExcelBuilder;

@RestController
@RequestMapping("/report")
public class ReportController extends CustomizedRestController {

    @Resource
    private ReportDao reportDao;

    @Resource
    private ReportService reportService;

    @Resource
    private ExecuteService executeService;

    @Resource
    private ReportPageQuery reportPageQuery;

    @RequestMapping("/reportPageQuery")
    @ResponseBody
    public Object orderPageQuery(HttpServletRequest request) {
        return reportPageQuery.queryPage(getParams(request), reportDao);
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public Report createReport(String reportJson) {
        return reportService.createReport(reportJson);
    }

    @RequestMapping(value = "/report/{reportId}", method = RequestMethod.GET)
    public Report retrieveReport(@PathVariable Integer reportId) {
        return reportService.retrieveReport(reportId);
    }

    @RequestMapping(value = "/report/{reportId}", method = RequestMethod.DELETE)
    public void deleteReport(@PathVariable Integer reportId) {
        reportService.deleteReport(reportId);
    }

    @RequestMapping(value = "/report/{reportId}", method = RequestMethod.PUT)
    public Report updateReport(@PathVariable Integer reportId,
            HttpServletRequest request) {
        Map<String, String> params = readForm(request);
        return reportService.updateReport(reportId, params.get("reportJson"));
    }

    @RequestMapping(value = "/report/createAll", method = RequestMethod.POST)
    public Integer createAll(String reportJson, String columnArrJson,
            String parameterArrJson) {
        return reportService.createAll(reportJson, columnArrJson,
                parameterArrJson);
    }

    @RequestMapping(value = "/report/{reportId}/updateAll", method = RequestMethod.PUT)
    public void updateAll(@PathVariable Integer reportId,
            HttpServletRequest request) {
        Map<String, String> params = readForm(request);
        reportService.updateAll(reportId, params.get("reportJson"),
                params.get("columnArrJson"), params.get("parameterArrJson"));
    }

    @RequestMapping(value = "/analyseSql", method = RequestMethod.POST)
    public Object analyseSql(String sql) {
        return SqlHelper.getColumnNames(sql);
    }

    @RequestMapping("/fetchReport")
    public PageData<Report> fetchReport(Long offset, Long limit) {
        List<Report> data = reportService.fetchReport(offset, limit);
        Long tatalCount = reportService.getReportCount();

        PageData<Report> result = new PageData<Report>();
        result.setTotal(tatalCount);
        result.setData(data);
        result.setOffset(offset);
        result.setLimit(limit);
        return result;
    }

    @RequestMapping("/fetchReportInPage")
    @Deprecated
    public me.quanli.report.controller.PageData<Report> fetchReportInPage(
            Integer pageNo, Integer pageSize) {
        List<Report> data = reportService.fetchReport(
                Long.valueOf(pageNo - 1) * pageSize, Long.valueOf(pageSize));
        Integer tatalCount = reportService.getReportCount().intValue();

        me.quanli.report.controller.PageData<Report> result = new me.quanli.report.controller.PageData<Report>();
        result.setTotal(tatalCount);
        result.setData(data);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        return result;
    }

    @RequestMapping("/getExecutionResult")
    public PageData<Map<String, Object>> getExecutionResult(Integer reportId,
            String params, Long offset, Long limit) throws IOException {
        Report report = reportDao.get(Report.class, reportId);
        if (report == null) {
            throw new RuntimeException("report not found");
        }
        JSONObject requestParams = JSON.parseObject(params);

        List<Map<String, Object>> allData = executeService.executeReport(report,
                requestParams);
        Long fromIndex = offset;
        Long toIndex = offset + limit;
        fromIndex = fromIndex > allData.size() ? allData.size() : fromIndex;
        toIndex = toIndex > allData.size() ? allData.size() : toIndex;

        List<Map<String, Object>> data = allData.subList(fromIndex.intValue(),
                toIndex.intValue());

        PageData<Map<String, Object>> result = new PageData<Map<String, Object>>();
        result.setTotal(new Long(allData.size()));
        result.setData(data);
        result.setOffset(offset);
        result.setLimit(limit);
        return result;
    }

    @RequestMapping("/getExecutionResultInPage")
    @Deprecated
    public me.quanli.report.controller.PageData<Map<String, Object>> getExecutionResultInPage(
            Integer reportId, String params, Integer pageNo, Integer pageSize)
            throws IOException {
        Report report = reportDao.get(Report.class, reportId);
        if (report == null) {
            throw new RuntimeException("report not found");
        }
        JSONObject requestParams = JSON.parseObject(params);

        List<Map<String, Object>> allData = executeService.executeReport(report,
                requestParams);
        Integer fromIndex = (pageNo - 1) * pageSize;
        Integer toIndex = fromIndex + pageSize;
        fromIndex = fromIndex > allData.size() ? allData.size() : fromIndex;
        toIndex = toIndex > allData.size() ? allData.size() : toIndex;

        List<Map<String, Object>> data = allData.subList(fromIndex, toIndex);

        me.quanli.report.controller.PageData<Map<String, Object>> result = new me.quanli.report.controller.PageData<Map<String, Object>>();
        result.setTotal(allData.size());
        result.setData(data);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        return result;
    }

    /**
     * download report result in xlsx format
     *
     * @param reportId
     * @param params
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadExecutionResultInXlsx")
    public void downloadExecutionResultInXlsx(Integer reportId, String params,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Report report = reportDao.get(Report.class, reportId);
        if (report == null) {
            throw new RuntimeException("report not found");
        }
        JSONObject requestParams = JSON.parseObject(params);

        // generate file name
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        String filename = request.getParameter("fileName");
        if (StringUtils.isBlank(filename)) {
            filename = "Report_" + report.getName() + "_"
                    + df.format(new Date()) + ".xlsx";
        }
        filename = ExcelUtils.encodeFilename(filename, request);

        // generate .xlsx file
        List<Map<String, Object>> dataList = executeService
                .executeReport(report, requestParams);
        List<ReportColumn> columns = reportDao
                .find("from ReportColumn where reportId = ? ", report.getId());
        XSSFWorkbook workbook = ReportExcelBuilder.buildExcel(report, dataList,
                columns);

        try (OutputStream outputStream = response.getOutputStream()) {
            // write response at last to make sure exceptions in above code got
            // returned
            response.setHeader("Content-disposition",
                    "attachment;filename=" + filename);
            workbook.write(outputStream);
        }
    }

    @RequestMapping("/sendEmailWithExecutionResultInXlsx")
    public String sendEmailWithExecutionResultInXlsx(Integer reportId,
            String params) throws IOException {
        Report report = reportDao.get(Report.class, reportId);
        if (report == null) {
            throw new RuntimeException("report not found");
        }
        JSONObject requestParams = JSON.parseObject(params);

        // generate file name
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        String filename = "Report_" + report.getName() + "_"
                + df.format(new Date()) + ".xlsx";

        // generate .xlsx file
        List<Map<String, Object>> dataList = executeService
                .executeReport(report, requestParams);
        List<ReportColumn> columns = reportDao
                .find("from ReportColumn where reportId = ? ", report.getId());
        XSSFWorkbook workbook = ReportExcelBuilder.buildExcel(report, dataList,
                columns);

        ReportSend reportSend = reportDao.findSingle(
                "from ReportSend where reportId = ? ", report.getId());
        if (reportSend == null) {
            throw new RuntimeException("cannot find send config");
        }
        List<EmailAddress> emailAddresses = reportDao
                .find("from SendEmail where reportId = ? ", report.getId());

        MailSenderConfig config = new MailSenderConfig();
        config.setHost(reportSend.getEmailHost());
        config.setPort(reportSend.getEmailPort());
        config.setUser(reportSend.getEmailUser());
        config.setPassword(reportSend.getEmailPassword());
        config.setProtocol(reportSend.getEmailProtocol());
        config.setUseSSL(reportSend.getEmailUseSSL() == 1);

        MailSender sender = new MailSender(config);
        List<Attachment> attachments = new ArrayList<Attachment>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] fileBytes = baos.toByteArray();
        Attachment attachment = new Attachment(filename, fileBytes);
        attachments.add(attachment);

        List<String> toRecipients = new ArrayList<String>();
        for (EmailAddress emailAddress : emailAddresses) {
            toRecipients.add(emailAddress.getEmailAddress());
        }

        sender.send("Report", "<p>report</p>", null, attachments,
                toRecipients.toArray(new String[toRecipients.size()]),
                new String[] {}, false, false);
        return "OK";
    }

}
