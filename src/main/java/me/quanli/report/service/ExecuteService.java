package me.quanli.report.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import me.quanli.report.dao.ReportDao;
import me.quanli.report.dao.TargetDao;
import me.quanli.report.entity.Report;
import me.quanli.report.entity.ReportParameter;

@Service
public class ExecuteService {

    @Resource
    private ReportDao reportDao;

    @Resource
    private TargetDao targetDao;

    public List<Map<String, Object>> executeReport(Report report, JSONObject requestParams, Integer offset,
            Integer limit) {
        List<Map<String, Object>> data = executeReport(report, requestParams);
        return data.subList(offset, offset + limit);
    }

    public List<Map<String, Object>> executeReport(Report report, JSONObject requestParams) {
        List<ReportParameter> parameters = reportDao.find("from ReportParameter where reportId = ? ", report.getId());
        Map<String, Object> queryParamMap = new HashMap<String, Object>();
        for (ReportParameter parameter : parameters) {
            if (!requestParams.containsKey(parameter.getName())) {
                throw new RuntimeException("parameter " + parameter.getName() + " is missing");
            }
            Object value;
            if (Objects.equals(parameter.getType(), ReportParameter.TYPE_INTEGER)) {
                if (StringUtils.hasText(parameter.getListSqlText())
                        && Objects.equals(parameter.getMultiSelection(), 1)) {
                    JSONArray valueArray = getJsonArray(parameter, requestParams);
                    value = Arrays.asList(valueArray.toArray(new Integer[] {}));
                } else {
                    value = requestParams.getInteger(parameter.getName());
                }
            } else if (Objects.equals(parameter.getType(), ReportParameter.TYPE_STRING)) {
                if (StringUtils.hasText(parameter.getListSqlText())
                        && Objects.equals(parameter.getMultiSelection(), 1)) {
                    JSONArray valueArray = getJsonArray(parameter, requestParams);
                    value = Arrays.asList(valueArray.toArray(new String[] {}));
                } else {
                    value = requestParams.getString(parameter.getName());
                }
            } else if (Objects.equals(parameter.getType(), ReportParameter.TYPE_DATETIME)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    if (StringUtils.hasText(parameter.getListSqlText())
                            && Objects.equals(parameter.getMultiSelection(), 1)) {
                        JSONArray valueArray = getJsonArray(parameter, requestParams);
                        List<Date> valueList = new ArrayList<Date>();
                        for (int i = 0; i < valueArray.size(); i++) {
                            valueList.add(sdf.parse(valueArray.getString(i)));
                        }
                        value = valueList;
                    } else {
                        value = sdf.parse(requestParams.getString(parameter.getName()));
                    }
                } catch (ParseException e) {
                    throw new RuntimeException("datetime format error: " + requestParams.getString(parameter.getName()),
                            e);
                }
            } else if (Objects.equals(parameter.getType(), ReportParameter.TYPE_FLOAT)) {
                if (StringUtils.hasText(parameter.getListSqlText())
                        && Objects.equals(parameter.getMultiSelection(), 1)) {
                    JSONArray valueArray = getJsonArray(parameter, requestParams);
                    value = Arrays.asList(valueArray.toArray(new Float[] {}));
                } else {
                    value = requestParams.getFloat(parameter.getName());
                }
            } else if (Objects.equals(parameter.getType(), ReportParameter.TYPE_BOOLEAN)) {
                if (StringUtils.hasText(parameter.getListSqlText())
                        && Objects.equals(parameter.getMultiSelection(), 1)) {
                    JSONArray valueArray = getJsonArray(parameter, requestParams);
                    value = Arrays.asList(valueArray.toArray(new Boolean[] {}));
                } else {
                    value = requestParams.getBoolean(parameter.getName());
                }
            } else {
                throw new RuntimeException("parameter type " + parameter.getType() + " is not supported");
            }
            queryParamMap.put(parameter.getName(), value);
        }

        List<Map<String, Object>> result = targetDao.queryForList(report.getSqlText(), queryParamMap);
        return result;
    }

    private JSONArray getJsonArray(ReportParameter parameter, JSONObject requestParams) {
        JSONArray valueArray = requestParams.getJSONArray(parameter.getName());
        if (valueArray.size() == 0) {
            throw new RuntimeException("parameter " + parameter.getName() + " must have at least one value");
        }
        return valueArray;
    }

}
