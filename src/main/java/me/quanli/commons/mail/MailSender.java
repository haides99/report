package me.quanli.commons.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class MailSender {

    private MailSenderConfig config;

    private Session mailSession;

    private static final Log LOGGER = LogFactory.getLog(MailSender.class);

    public MailSender(MailSenderConfig config) {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap
                .getDefaultCommandMap();
        mc.addMailcap(
                "text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap(
                "text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap(
                "text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap(
                "multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap(
                "message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        this.config = config;
        this.mailSession = getMailSession(config);
    }

    private Session getMailSession(MailSenderConfig config) {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", Integer.toString(config.getPort()));
        if (config.usingSSL()) {
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
        } else {
            props.put("mail.smtp.auth", "true");
        }
        return Session.getDefaultInstance(props, null);
    }

    public boolean send(String subject, String content, String[] toRecipients,
            String[] ccRecipients) {
        return send(subject, content, null, null, toRecipients, ccRecipients,
                false, true);
    }

    public boolean send(String subject, String content,
            List<Attachment> attachments, String[] toRecipients,
            String[] ccRecipients, Boolean resend) {
        return send(subject, content, null, attachments, toRecipients,
                ccRecipients, false, resend);
    }

    public boolean send(String subject, String content,
            Hashtable<String, byte[]> imgBytesMap, String[] toRecipients,
            String[] ccRecipients, Boolean resend) {
        return send(subject, content, imgBytesMap, null, toRecipients,
                ccRecipients, false, resend);
    }

    /**
     * 发送消息到指定用户，含附件
     * 
     * @param subject
     *            标题
     * @param content
     *            内容
     * @param imgBytes
     *            图片字节流集合
     * @param attachments
     *            附件
     * @param toRecipients
     *            接收者
     * @param ccRecipients
     *            抄送
     * @param isMeeting
     *            是否是会议邀请
     * @param resend
     *            发送失败是否剔除Invalid Address重新发送
     * @return 发送成功与否
     */
    public boolean send(String subject, String content,
            Hashtable<String, byte[]> imgBytesMap, List<Attachment> attachments,
            String[] toRecipients, String[] ccRecipients, Boolean isMeeting,
            Boolean resend) {
        Transport transport = null;

        if (ArrayUtils.isEmpty(toRecipients)) {
            return false;
        }

        MimeMessage message = new MimeMessage(mailSession);
        try {
            message.setFrom(new InternetAddress(config.getUser()));
            message.setSubject(subject, "utf-8");
            for (String recipient : toRecipients) {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipient));
            }
            if (!ArrayUtils.isEmpty(ccRecipients)) {
                for (String recipient : ccRecipients) {
                    message.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(recipient));
                }
            }

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            
            String encodedContent;
            try {
                encodedContent = MimeUtility.encodeText(content);
            } catch (UnsupportedEncodingException e1) {
                encodedContent = content;
            }

            mimeBodyPart.setContent(encodedContent,
                    isMeeting ? "text/calendar;method=REQUEST;charset=UTF-8"
                            : "text/html;charset=utf8");
            multipart.addBodyPart(mimeBodyPart);

            if (imgBytesMap != null) {
                for (Entry<String, byte[]> entry : imgBytesMap.entrySet()) {
                    MimeBodyPart imgBodyPart = new MimeBodyPart();
                    DataHandler dh = new DataHandler(new ByteArrayDataSource(
                            entry.getValue(), "application/octet-stream"));
                    imgBodyPart.setDataHandler(dh);
                    imgBodyPart.setHeader("Content-ID", entry.getKey());
                    multipart.addBodyPart(imgBodyPart);
                }
            }

            if (attachments != null && !attachments.isEmpty()) {
                // 处理附件
                for (Attachment attachment : attachments) {
                    MimeBodyPart attachmentMimeBodyPart = new MimeBodyPart();
                    String encodedFileName;
                    try {
                        encodedFileName = MimeUtility
                                .encodeText(attachment.getFileName());
                    } catch (UnsupportedEncodingException e) {
                        encodedFileName = attachment.getFileName();
                    }
                    ByteArrayDataSource datasource = new ByteArrayDataSource(
                            attachment.getContent(),
                            "application/octet-stream");
                    attachmentMimeBodyPart.setFileName(encodedFileName);
                    attachmentMimeBodyPart
                            .setDataHandler(new DataHandler(datasource));
                    multipart.addBodyPart(attachmentMimeBodyPart);
                }
            }

            // Multipart加入到信件
            message.setContent(multipart);
            if (config.usingSSL()) {
                transport = mailSession.getTransport("smtps");
            } else {
                transport = mailSession.getTransport("smtp");
            }

            transport.connect(config.getHost(), config.getPort(),
                    config.getUser(), config.getPassword());

            MailcapCommandMap mc = (MailcapCommandMap) CommandMap
                    .getDefaultCommandMap();
            mc.addMailcap(
                    "text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap(
                    "text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap(
                    "text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap(
                    "multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap(
                    "message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);

            transport.sendMessage(message, message.getAllRecipients());

            return true;
        } catch (SendFailedException e) {
            if (e.getInvalidAddresses() == null) {
                LOGGER.error(e, e);
            } else {
                List<Address> invalidAddresses = Arrays
                        .asList(e.getInvalidAddresses());
                String wrongMessage = "---------------- invalidAddress:"
                        + StringUtils.collectionToDelimitedString(
                                invalidAddresses, ";", "'", "'")
                        + " ------------------";
                LOGGER.error(wrongMessage, e);
                if (resend != null && resend) {
                    try {
                        List<Address> newToRecipients = new ArrayList<Address>();
                        List<Address> newCCRecipients = new ArrayList<Address>();

                        Address[] oldToRecipients = message
                                .getRecipients(Message.RecipientType.TO);
                        Address[] oldCCRecipients = message
                                .getRecipients(Message.RecipientType.CC);

                        for (int i = 0; i < oldToRecipients.length; i++) {
                            if (!invalidAddresses
                                    .contains(oldToRecipients[i])) {
                                newToRecipients.add(oldToRecipients[i]);
                            }
                        }
                        message.setRecipients(Message.RecipientType.TO,
                                newToRecipients.toArray(
                                        new Address[newToRecipients.size()]));

                        for (int i = 0; i < oldCCRecipients.length; i++) {
                            if (!invalidAddresses
                                    .contains(oldCCRecipients[i])) {
                                newCCRecipients.add(oldCCRecipients[i]);
                            }
                        }
                        message.setRecipients(Message.RecipientType.CC,
                                newCCRecipients.toArray(
                                        new Address[newCCRecipients.size()]));

                        transport.sendMessage(message,
                                message.getAllRecipients());
                        return true;
                    } catch (MessagingException e1) {
                        LOGGER.error(e1, e1);
                    }
                }
            }
        } catch (NoSuchProviderException e) {
            LOGGER.error(e, e);
        } catch (MessagingException e) {
            LOGGER.error(e, e);
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    LOGGER.error(e, e);
                }
            }
        }

        return false;
    }

}
