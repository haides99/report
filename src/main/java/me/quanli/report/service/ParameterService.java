package me.quanli.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import me.quanli.commons.dao.Dao.Tx;
import me.quanli.report.dao.ReportDao;
import me.quanli.report.dao.TargetDao;
import me.quanli.report.entity.ReportParameter;

@Service
public class ParameterService {

    private static final Log LOGGER = LogFactory.getLog(ParameterService.class);

    @Resource
    private ReportDao reportDao;

    @Resource
    private TargetDao targetDao;

    public List<ReportParameter> fetchReportParameters(Integer reportId) {
        List<ReportParameter> parameters = reportDao.find("from ReportParameter where reportId = ? ", reportId);

        try {
            // query and set select options and default key values
            for (ReportParameter parameter : parameters) {
                if (StringUtils.hasText(parameter.getListSqlText())) {
                    SqlRowSet rowSet = targetDao.queryForRowSet(parameter.getListSqlText(),
                            new HashMap<String, Object>());
                    List<List<Object>> selectOptions = new ArrayList<List<Object>>();
                    while (rowSet.next()) {
                        List<Object> option = new ArrayList<Object>();
                        for (int i = 1;; i++) {
                            try {
                                option.add(rowSet.getObject(i));
                            } catch (Exception e) {
                                // FIXME: try to remove exception handle
                                break;
                            }
                        }
                        selectOptions.add(option);
                    }
                    parameter.setSelectOptions(selectOptions);

                    if (StringUtils.hasText(parameter.getListSqlDefault())) {
                        List<Map<String, Object>> defaultOptionMaps = targetDao
                                .queryForList(parameter.getListSqlDefault(), new HashMap<String, Object>());
                        if (defaultOptionMaps.size() > 0 && defaultOptionMaps.get(0).entrySet().size() > 1) {
                            throw new RuntimeException("ListSqlDefault can only query one column");
                        }
                        List<Object> optionKeys = new ArrayList<Object>();
                        for (Map<String, Object> defaultOptionMap : defaultOptionMaps) {
                            for (Entry<String, Object> entry : defaultOptionMap.entrySet()) {
                                optionKeys.add(entry.getValue());
                                break;
                            }
                        }
                        parameter.setDefaultSelectOptionKeys(optionKeys);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return parameters;
    }

    public List<ReportParameter> updateReportParameters(final Integer reportId, String parameterArrJson) {
        final List<ReportParameter> parameters = JSON.parseArray(parameterArrJson, ReportParameter.class);
        for (ReportParameter parameter : parameters) {
            parameter.setReportId(reportId);
            parameter.setId(null);
        }
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                reportDao.bulkUpdate("delete from ReportParameter where reportId = ? ", reportId);
                for (ReportParameter parameter : parameters) {
                    reportDao.save(parameter);
                }
            }
        });
        return parameters;
    }

}
