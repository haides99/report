package me.quanli.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REPORT_SEND")
public class ReportSend {

    private Integer id;

    private Integer reportId;

    private Integer emailActive;

    private String emailHost;

    private Integer emailPort;

    private String emailCharset;

    private String emailUser;

    private String emailPassword;

    private String emailProtocol;

    private Integer emailUseSSL;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "REPORT_ID")
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    @Column(name = "EMAIL_ACTIVE")
    public Integer getEmailActive() {
        return emailActive;
    }

    public void setEmailActive(Integer emailActive) {
        this.emailActive = emailActive;
    }

    @Column(name = "EMAIL_HOST")
    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    @Column(name = "EMAIL_PORT")
    public Integer getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(Integer emailPort) {
        this.emailPort = emailPort;
    }

    @Column(name = "EMAIL_CHARSET")
    public String getEmailCharset() {
        return emailCharset;
    }

    public void setEmailCharset(String emailCharset) {
        this.emailCharset = emailCharset;
    }

    @Column(name = "EMAIL_USER")
    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    @Column(name = "EMAIL_PASSWORD")
    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    @Column(name = "EMAIL_PROTOCOL")
    public String getEmailProtocol() {
        return emailProtocol;
    }

    public void setEmailProtocol(String emailProtocol) {
        this.emailProtocol = emailProtocol;
    }

    @Column(name = "EMAIL_USE_SSL")
    public Integer getEmailUseSSL() {
        return emailUseSSL;
    }

    public void setEmailUseSSL(Integer emailUseSSL) {
        this.emailUseSSL = emailUseSSL;
    }

}
