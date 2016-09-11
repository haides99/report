package me.quanli.commons.mail;

import me.quanli.commons.util.PropertiesUtils;

public class MailSenderConfigFactory {

    public static MailSenderConfig getDefaultConfig() {
        MailSenderConfig config = new MailSenderConfig();
        config.setHost(PropertiesUtils.getProperty("mail.server.host"));
        config.setPort(Integer
                .valueOf(PropertiesUtils.getProperty("mail.server.port")));
        config.setProtocol(PropertiesUtils.getProperty("mail.protocol"));
        config.setUser(PropertiesUtils.getProperty("mail.user"));
        config.setPassword(PropertiesUtils.getProperty("mail.password"));
        config.setUseSSL(Boolean
                .valueOf(PropertiesUtils.getProperty("mail.useSSL", "true")));
        return config;
    }

}
