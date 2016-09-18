package me.quanli.report.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import me.quanli.commons.dao.Dao.Tx;
import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.EmailAddress;
import me.quanli.report.entity.ReportSend;
import me.quanli.report.entity.dto.SendingConfig;

@Service
public class SendingService {

    @Resource
    private ReportDao reportDao;

    public SendingConfig getSendingConfig(Integer reportId) {
        SendingConfig sendingConfig = new SendingConfig();

        List<ReportSend> reportSendList = reportDao
                .find("from ReportSend where reportId = ? ", reportId);
        List<EmailAddress> emailAddresses = reportDao
                .find("from EmailAddress where reportId = ? ", reportId);
        if (reportSendList.size() > 0) {
            sendingConfig.setReportSend(reportSendList.get(0));
        }
        sendingConfig.setEmailAddresses(emailAddresses);

        return sendingConfig;
    }

    public void updateSendingConfig(final Integer reportId,
            String sendingConfigJson) {
        final SendingConfig sendingConfig = JSON.parseObject(sendingConfigJson,
                SendingConfig.class);
        final ReportSend reportSend = sendingConfig.getReportSend();
        reportSend.setReportId(reportId);
        reportSend.setId(null);
        for (EmailAddress emailAddress : sendingConfig.getEmailAddresses()) {
            emailAddress.setReportId(reportId);
            emailAddress.setId(null);
        }
        reportDao.doInTx(new Tx() {
            @Override
            public void exec() {
                reportDao.bulkUpdate(
                        "delete from ReportSend where reportId = ? ", reportId);
                reportDao.save(reportSend);

                reportDao.bulkUpdate(
                        "delete from EmailAddress where reportId = ? ",
                        reportId);
                for (EmailAddress emailAddress : sendingConfig
                        .getEmailAddresses()) {
                    reportDao.save(emailAddress);
                }
            }
        });
    }

}
