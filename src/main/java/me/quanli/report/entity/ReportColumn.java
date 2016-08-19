package me.quanli.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Report_Column")
public class ReportColumn {

    public static final Integer TYPE_INTEGER = 1;

    public static final Integer TYPE_STRING = 2;

    public static final Integer TYPE_FLOAT = 3;

    public static final Integer TYPE_DATETIME = 4;

    public static final Integer TYPE_DATE = 5;

    public static final Integer TYPE_TIMEDURATION = 6;

    public static final Integer STYLE_ALIGN_LEFT = 1;

    public static final Integer STYLE_ALIGN_CENTER = 2;

    public static final Integer STYLE_ALIGN_RIGHT = 3;

    public static final Integer STYLE_ALIGN_FILL = 4;

    private Integer id;

    private Integer reportId;

    private String name;

    private String description;

    private Integer indexNo;

    private Integer type;

    private Integer floatPrecision;

    private Integer width;

    private Integer alignement;

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

    @Column(name = "Float_Precision")
    public Integer getFloatPrecision() {
        return floatPrecision;
    }

    public void setFloatPrecision(Integer floatPrecision) {
        this.floatPrecision = floatPrecision;
    }

    @Column(name = "Width")
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Column(name = "Alignement")
    public Integer getAlignement() {
        return alignement;
    }

    public void setAlignement(Integer alignement) {
        this.alignement = alignement;
    }

}
