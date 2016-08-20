package me.quanli.report.controller;

import me.quanli.commons.table.PageQuery;
import me.quanli.report.entity.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/8/20.
 */
@Component
public class ReportPageQuery extends PageQuery {

    @Override
    protected String buildQuery(Map<String, String> params,
                                List<Object> paramValues) {
        return "from Report";
    }

    @Override
    protected Object toRowDatas(List<Object> datas) {
        int currentNo = localStart.get() == null ? 0 : localStart.get()
                .intValue();

        List<Object> rows = new ArrayList<Object>();
        for (Object data : datas) {
            Report report = (Report) data;
            List<Object> row = new ArrayList<Object>();
            row.add(++currentNo);
            row.add(report.getName());
            row.add(report.getDescription());
            row.add(report.getComments());
            row.add(report.getId());
            row.add(report.getId());
            rows.add(row);
        }
        return rows;
    }

}
