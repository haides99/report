package me.quanli.report.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import me.quanli.report.entity.Report;
import me.quanli.report.entity.ReportColumn;

public class ReportExcelBuilder {

    public static XSSFWorkbook buildExcel(Report report, List<Map<String, Object>> dataList,
            List<ReportColumn> columns) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        XSSFDataFormat dateFormat = workbook.createDataFormat();

        if (dataList.isEmpty()) {
            XSSFRow row = sheet.createRow(0);
            XSSFCell cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("Report is empty");
            return workbook;
        }

        int rowIndex = 0;

        // add header
        XSSFRow headerRow = sheet.createRow(rowIndex);
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor((short) 13);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        XSSFCell headerCell = null;
        for (int i = 0; i < columns.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            headerCell.setCellValue(columns.get(i).getDescription());
            headerCell.setCellStyle(headerStyle);
        }
        rowIndex++;

        // create column styles
        Map<ReportColumn, XSSFCellStyle> styleMap = new HashMap<ReportColumn, XSSFCellStyle>();
        for (ReportColumn column : columns) {
            XSSFCellStyle style = workbook.createCellStyle();

            // set date format
            if (Objects.equals(column.getType(), ReportColumn.TYPE_DATE)) {
                style.setDataFormat(dateFormat.getFormat("yyyy-MM-dd"));
            } else if (Objects.equals(column.getType(), ReportColumn.TYPE_DATETIME)) {
                style.setDataFormat(dateFormat.getFormat("yyyy-MM-dd HH:mm:ss"));
            } else if (Objects.equals(column.getType(), ReportColumn.TYPE_TIMEDURATION)) {
                style.setDataFormat(dateFormat.getFormat("[h]:mm:ss"));
            }

            // set alignement
            short alignement = HSSFCellStyle.ALIGN_GENERAL;
            if (Objects.equals(column.getAlignement(), ReportColumn.STYLE_ALIGN_LEFT)) {
                alignement = HSSFCellStyle.ALIGN_LEFT;
            } else if (Objects.equals(column.getAlignement(), ReportColumn.STYLE_ALIGN_CENTER)) {
                alignement = HSSFCellStyle.ALIGN_CENTER;
            } else if (Objects.equals(column.getAlignement(), ReportColumn.STYLE_ALIGN_RIGHT)) {
                alignement = HSSFCellStyle.ALIGN_RIGHT;
            } else if (Objects.equals(column.getAlignement(), ReportColumn.STYLE_ALIGN_FILL)) {
                alignement = HSSFCellStyle.ALIGN_FILL;
            }
            style.setAlignment(alignement);

            // set color
            // XSSFFont font = workbook.createFont();
            // font.setColor(HSSFFont.COLOR_NORMAL);
            // style.setFont(font);

            styleMap.put(column, style);
        }

        for (Map<String, Object> data : dataList) {
            XSSFRow row = sheet.createRow(rowIndex);
            for (int i = 0; i < columns.size(); i++) {
                ReportColumn column = columns.get(i);
                XSSFCell cell = null;
                cell = row.createCell(i);

                // set cell type
                if (Objects.equals(column.getType(), ReportColumn.TYPE_INTEGER)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_STRING)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_FLOAT)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_TIMEDURATION)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }

                // set cell value
                Object value = data.get(column.getName());
                if (value == null) {
                    cell.setCellValue("");
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_INTEGER)) {
                    cell.setCellValue((Integer) value);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_STRING)) {
                    cell.setCellValue(Objects.toString(value, ""));
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_FLOAT)) {
                    cell.setCellValue(Double.parseDouble(String.valueOf(value)));
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_DATETIME)) {
                    cell.setCellValue((Date) value);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_DATE)) {
                    cell.setCellValue((Date) value);
                } else if (Objects.equals(column.getType(), ReportColumn.TYPE_TIMEDURATION)) {
                    if (value != null && value instanceof Long) {
                        long day = (long) value / 86400000l;
                        long remainder = (long) value % 86400000l;
                        cell.setCellValue(
                                day + DateUtil.convertTime(DurationFormatUtils.formatDuration(remainder, "HH:mm:ss")));
                    } else {
                        cell.setCellValue("");
                    }
                }

                cell.setCellStyle(styleMap.get(column));
            }
            rowIndex++;
        }

        /**
         * auto resize cell width
         */
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

}
