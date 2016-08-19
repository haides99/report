package me.quanli.report.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import me.quanli.commons.dao.Dao.Tx;
import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.Report;
import me.quanli.report.entity.ReportColumn;
import me.quanli.report.entity.ReportParameter;

@Service
public class ReportService {

    @Resource
    private ReportDao reportDao;

    @Resource
    private ColumnService columnService;

    @Resource
    private ParameterService parameterService;

    public Report createReport(String reportJson) {
        Report report = JSON.parseObject(reportJson, Report.class);
        reportDao.save(report);
        return report;
    }

    public Report retrieveReport(Integer reportId) {
        return reportDao.get(Report.class, reportId);
    }

    public void deleteReport(Integer reportId) {
        Report report = reportDao.get(Report.class, reportId);
        reportDao.delete(report);
    }

    public Report updateReport(Integer reportId, String reportJson) {
        Report report = reportDao.get(Report.class, reportId);
        Report newReport = JSON.parseObject(reportJson, Report.class);

        if (newReport.getId() != null && !Objects.equals(newReport.getId(), reportId)) {
            throw new RuntimeException("reportId does not match");
        }
        if (newReport.getName() != null) {
            report.setName(newReport.getName());
        }
        if (newReport.getType() != null) {
            report.setType(newReport.getType());
        }
        if (newReport.getDescription() != null) {
            report.setDescription(newReport.getDescription());
        }
        if (newReport.getSqlText() != null) {
            report.setSqlText(newReport.getSqlText());
        }
        if (newReport.getVersion() != null) {
            report.setVersion(newReport.getVersion());
        }
        if (newReport.getComments() != null) {
            report.setComments(newReport.getComments());
        }
        reportDao.save(report);
        return report;
    }

    public Integer createAll(final String reportJson, final String columnArrJson, final String parameterArrJson) {
        final List<Integer> id = new ArrayList<Integer>();
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                Report report = createReport(reportJson);
                columnService.updateReportColumns(report.getId(), columnArrJson);
                parameterService.updateReportParameters(report.getId(), parameterArrJson);
                id.add(report.getId());
            }
        });
        return id.get(0);
    }

    public void updateAll(final Integer reportId, final String reportJson, final String columnArrJson,
            final String parameterArrJson) {
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                updateReport(reportId, reportJson);
                columnService.updateReportColumns(reportId, columnArrJson);
                parameterService.updateReportParameters(reportId, parameterArrJson);
            }
        });
    }

    public Integer getReportCount() {
        BigInteger count = reportDao.findSingleBySQL("select count(*) from Report");
        return count.intValue();
    }

    public List<Report> fetchReport() {
        return reportDao.find("from Report ");
    }

    public List<Report> fetchReport(Integer offset, Integer limit) {
        return reportDao.findWithLimit("from Report ", offset, limit);
    }

    public List<ReportColumn> fetchReportColumns(Integer reportId) {
        return columnService.fetchReportColumns(reportId);
    }

    public List<ReportParameter> fetchReportParameters(Integer reportId) {
        return parameterService.fetchReportParameters(reportId);
    }

}
