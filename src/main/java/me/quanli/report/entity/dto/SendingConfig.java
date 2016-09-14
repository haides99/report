package me.quanli.report.entity.dto;

import java.util.List;

import me.quanli.report.entity.EmailAddress;
import me.quanli.report.entity.ReportSend;

public class SendingConfig {

    private ReportSend reportSend;

    private List<EmailAddress> emailAddresses;

    public ReportSend getReportSend() {
        return reportSend;
    }

    public void setReportSend(ReportSend reportSend) {
        this.reportSend = reportSend;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

}
