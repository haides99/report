package me.quanli.report.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.quanli.report.entity.ReportParameter;
import me.quanli.report.service.ParameterService;

@RestController
@RequestMapping("/report")
public class ParameterController extends CustomizedRestController {

    @Resource
    private ParameterService parameterService;

    @RequestMapping(value = "/report/{reportId}/parameters", method = RequestMethod.GET)
    public List<ReportParameter> fetchReportParameters(@PathVariable Integer reportId) {
        return parameterService.fetchReportParameters(reportId);
    }

    @RequestMapping(value = "/report/{reportId}/parameters", method = RequestMethod.PUT)
    public List<ReportParameter> updateReportParameters(@PathVariable Integer reportId, HttpServletRequest request) {
        Map<String, String> params = readForm(request);
        return parameterService.updateReportParameters(reportId, params.get("parameterArrJson"));
    }

}
