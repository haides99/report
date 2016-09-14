package me.quanli.report.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import me.quanli.report.dao.ReportDao;
import me.quanli.report.entity.EmailAddress;
import me.quanli.report.entity.ReportSend;
import me.quanli.report.entity.dto.SendingConfig;

@Service
public class SendingService {

    private static final Log LOGGER = LogFactory.getLog(SendingService.class);

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
    
    public void updateSendingConfig(final Integer reportId, String sendingConfigJson) {
        
    }

}
