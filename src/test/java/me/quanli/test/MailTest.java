package me.quanli.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import me.quanli.commons.mail.Attachment;
import me.quanli.commons.mail.MailSender;
import me.quanli.commons.mail.MailSenderConfig;

@RunWith(JUnit4.class)
public class MailTest {

    @Test
    @Ignore
    public void test() {
        MailSenderConfig config = new MailSenderConfig();
        MailSender sender = new MailSender(config);
        List<Attachment> attachments = new ArrayList<Attachment>();
        Attachment attachment = new Attachment("名单.txt", "张三,李四".getBytes());
        attachments.add(attachment);
        sender.send("参会人员名单", "<p>附件是参会人员名单，请查阅</p>", null, attachments,
                new String[] {"525985053@qq.com"}, new String[] {}, false,
                false);
    }

}
