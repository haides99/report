package me.quanli.report.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.quanli.report.entity.dto.SendingConfig;
import me.quanli.report.service.SendingService;

@RestController
@RequestMapping("/report")
public class SendingController {

    @Resource
    private SendingService sendingService;

    @RequestMapping(value = "/report/{reportId}/sendingConfig", method = RequestMethod.GET)
    public SendingConfig getSendingConfig(@PathVariable Integer reportId) {
        return sendingService.getSendingConfig(reportId);
    }

}
