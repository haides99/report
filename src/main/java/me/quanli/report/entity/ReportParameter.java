package me.quanli.report.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Report_Parameter")
public class ReportParameter {

    public static final Integer TYPE_INTEGER = 1;

    public static final Integer TYPE_STRING = 2;

    public static final Integer TYPE_FLOAT = 3;

    public static final Integer TYPE_BOOLEAN = 4;

    public static final Integer TYPE_DATETIME = 5;

    private Integer id;

    private Integer reportId;

    private String name;

    private String description;

    private Integer indexNo;

    private Integer type;

    private String defaultValue;

    private String listSqlText;

    private String listSqlDefault;

    private Integer multiSelection;

    private List<List<Object>> selectOptions;

    private List<Object> defaultSelectOptionKeys;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "Report_Id")
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "Index_No")
    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    @Column(name = "Type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "Default_Value")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "List_Sql_Text")
    public String getListSqlText() {
        return listSqlText;
    }

    public void setListSqlText(String listSqlText) {
        this.listSqlText = listSqlText;
    }

    @Column(name = "List_Sql_Default")
    public String getListSqlDefault() {
        return listSqlDefault;
    }

    public void setListSqlDefault(String listSqlDefault) {
        this.listSqlDefault = listSqlDefault;
    }

    @Column(name = "Multi_Selection")
    public Integer getMultiSelection() {
        return multiSelection;
    }

    public void setMultiSelection(Integer multiSelection) {
        this.multiSelection = multiSelection;
    }

    @Transient
    public List<List<Object>> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<List<Object>> selectOptions) {
        this.selectOptions = selectOptions;
    }

    @Transient
    public List<Object> getDefaultSelectOptionKeys() {
        return defaultSelectOptionKeys;
    }

    public void setDefaultSelectOptionKeys(List<Object> defaultSelectOptionKeys) {
        this.defaultSelectOptionKeys = defaultSelectOptionKeys;
    }

}
