package me.quanli.report.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.quanli.report.entity.ReportColumn;
import me.quanli.report.service.ColumnService;

@RestController
@RequestMapping("/report")
public class ColumnController extends CustomizedRestController {

    @Resource
    private ColumnService columnService;

    @RequestMapping(value = "/report/{reportId}/columns", method = RequestMethod.GET)
    public List<ReportColumn> fetchReportColumns(@PathVariable Integer reportId) {
        return columnService.fetchReportColumns(reportId);
    }

    @RequestMapping(value = "/report/{reportId}/columns", method = RequestMethod.PUT)
    public List<ReportColumn> updateReportColumns(@PathVariable Integer reportId, HttpServletRequest request) {
        Map<String, String> params = readForm(request);
        return columnService.updateReportColumns(reportId, params.get("columnArrJson"));
    }

}
