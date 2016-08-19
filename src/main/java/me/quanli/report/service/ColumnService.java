package me.quanli.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

import me.quanli.commons.dao.Dao.Tx;
import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.ReportColumn;

@Service
public class ColumnService {

    @Resource
    private ReportDao reportDao;

    @Deprecated
    public ReportColumn createColumn(String columnJson) {
        ReportColumn column = JSON.parseObject(columnJson, ReportColumn.class);
        reportDao.save(column);
        return column;
    }

    @Deprecated
    public ReportColumn retrieveColumn(Integer columnId) {
        return reportDao.get(ReportColumn.class, columnId);
    }

    @Deprecated
    public void deleteColumn(Integer columnId) {
        ReportColumn column = reportDao.get(ReportColumn.class, columnId);
        reportDao.delete(column);
    }

    @Deprecated
    public ReportColumn updateColumn(Integer columnId, String columnJson) {
        ReportColumn column = reportDao.get(ReportColumn.class, columnId);
        ReportColumn newColumn = JSON.parseObject(columnJson, ReportColumn.class);

        if (newColumn.getId() != null && !Objects.equals(newColumn.getId(), columnId)) {
            throw new RuntimeException("columnId does not match");
        }
        if (newColumn.getName() != null) {
            column.setName(newColumn.getName());
        }
        if (newColumn.getType() != null) {
            column.setType(newColumn.getType());
        }
        if (newColumn.getDescription() != null) {
            column.setDescription(newColumn.getDescription());
        }
        if (newColumn.getIndexNo() != null) {
            column.setIndexNo(newColumn.getIndexNo());
        }
        if (newColumn.getFloatPrecision() != null) {
            column.setFloatPrecision(newColumn.getFloatPrecision());
        }
        if (newColumn.getWidth() != null) {
            column.setWidth(newColumn.getWidth());
        }
        if (newColumn.getAlignement() != null) {
            column.setAlignement(newColumn.getAlignement());
        }
        reportDao.save(column);
        return column;
    }

    public List<ReportColumn> fetchReportColumns(Integer reportId) {
        return reportDao.find("from ReportColumn where reportId = ? ", reportId);
    }

    public List<ReportColumn> updateReportColumns(final Integer reportId, String columnArrJson) {
        final List<ReportColumn> columns = JSON.parseArray(columnArrJson, ReportColumn.class);
        for (ReportColumn column : columns) {
            column.setReportId(reportId);
            column.setId(null);
        }
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                reportDao.bulkUpdate("delete from ReportColumn where reportId = ? ", reportId);
                for (ReportColumn column : columns) {
                    reportDao.save(column);
                }
            }
        });
        return columns;
    }

    @Deprecated
    public List<ReportColumn> bulkCreateReport(Integer reportId, String columnArrJson) {
        final List<ReportColumn> columns = JSON.parseArray(columnArrJson, ReportColumn.class);
        for (ReportColumn column : columns) {
            column.setReportId(reportId);
            column.setId(null);
        }
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                for (ReportColumn column : columns) {
                    reportDao.save(column);
                }
            }
        });
        return columns;
    }

    @Deprecated
    public List<ReportColumn> bulkUpdateColumn(Integer reportId, String columnArrJson) {
        final List<ReportColumn> columns = JSON.parseArray(columnArrJson, ReportColumn.class);
        for (ReportColumn column : columns) {
            Assert.notNull(column.getId(), "column id is missing");
            if (!Objects.equals(column.getReportId(), column)) {
                throw new RuntimeException("cannot modify report id in column");
            }
        }

        final List<ReportColumn> updatedColumns = new ArrayList<ReportColumn>();
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                for (ReportColumn column : columns) {
                    ReportColumn updatedColumn = updateColumn(column.getId(), JSON.toJSONString(column));
                    updatedColumns.add(updatedColumn);
                }
            }
        });
        return updatedColumns;
    }

}
