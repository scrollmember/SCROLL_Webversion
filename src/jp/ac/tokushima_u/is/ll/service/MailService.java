package jp.ac.tokushima_u.is.ll.service;

import java.io.IOException;
import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import jp.ac.tokushima_u.is.ll.form.EmailModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * @author houbin
 */
@Service
public class MailService {
	
	@Autowired
	private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender mailSender;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    @Qualifier("notificationSender")
    private JavaMailSender notificationSender;
//    @Autowired
//    private TaskExecutor taskExecutor;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    private Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendSysMail(String sendTo, String subject, String templateName, ModelMap model) throws MessagingException, IOException, TemplateException {
        final EmailModel email = new EmailModel();
        email.setAddress(sendTo);
        email.setFrom(propertyService.getSystemMailAddress());
        email.setReplyTo(email.getFrom());
        email.setSubject(subject);
        email.setHtml(false);
        Configuration cfg = freeMarkerConfigurer.getConfiguration();
        Template t = cfg.getTemplate(templateName + ".ftl");
        StringWriter writer = new StringWriter();
        t.process(model, writer);
        email.setContent(writer.toString());
        jmsTemplate.send(propertyService.getJmsQueueNameSendMail(), new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(email);
			}
        });
//        sendMail(email);
    }

    public void sendMail(EmailModel email) throws MessagingException, IOException {
        if (email.getAddresses() == null || email.getAddresses().length == 0) {
            logger.error("No mail address");
            return;
        }

        startSendMail(email);
    }

    private void startSendMail(EmailModel email) throws MessagingException, IOException {
        MimeMessage mime = mailSender.createMimeMessage();
        boolean isMultipart = false;
        if (email.getAttachment() != null && email.getAttachment().length > 0) {
            isMultipart = true;
        }
        MimeMessageHelper helper = new MimeMessageHelper(mime, isMultipart, "utf-8");
        helper.setFrom(email.getFrom());//发件人
        helper.setTo(email.getAddress());//收件人

        if (StringUtils.hasLength(email.getCc())) {
            String cc[] = email.getCc().split(";");
            helper.setCc(cc);//抄送
        }

        if (StringUtils.hasLength(email.getBcc())) {
            String bcc[] = email.getBcc().split(";");
            helper.setBcc(bcc);
        }

        helper.setReplyTo(email.getReplyTo());//回复到
        helper.setSubject(email.getSubject());//邮件主题
        helper.setText(email.getContent(), email.isHtml());//true表示设定html格式
        if (isMultipart) {
            for (MultipartFile file : email.getAttachment()) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String fileName = file.getOriginalFilename();
                try {
                    fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
                } catch (Exception e) {
                }
                helper.addAttachment(fileName, new ByteArrayResource(file.getBytes()));
            }
        }

        mailSender.send(mime);
    }

	public void sendNotificationMail(EmailModel email) throws MessagingException, IOException{
        if (email.getAddresses() == null || email.getAddresses().length == 0) {
            logger.error("No mail address");
            return;
        }
        startSendNoficationMail(email);
	}
	
    private void startSendNoficationMail(EmailModel email) throws MessagingException, IOException {
        MimeMessage mime = mailSender.createMimeMessage();
        boolean isMultipart = false;
        if (email.getAttachment() != null && email.getAttachment().length > 0) {
            isMultipart = true;
        }
        MimeMessageHelper helper = new MimeMessageHelper(mime, isMultipart, "utf-8");
        helper.setFrom(email.getFrom());//发件人
        helper.setTo(email.getAddress());//收件人

        if (StringUtils.hasLength(email.getCc())) {
            String cc[] = email.getCc().split(";");
            helper.setCc(cc);//抄送
        }

        if (StringUtils.hasLength(email.getBcc())) {
            String bcc[] = email.getBcc().split(";");
            helper.setBcc(bcc);
        }

        helper.setReplyTo(email.getReplyTo());//回复到
        helper.setSubject(email.getSubject());//邮件主题
        helper.setText(email.getContent(), email.isHtml());//true表示设定html格式
        if (isMultipart) {
            for (MultipartFile file : email.getAttachment()) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String fileName = file.getOriginalFilename();
                try {
                    fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
                } catch (Exception e) {
                }
                helper.addAttachment(fileName, new ByteArrayResource(file.getBytes()));
            }
        }

        notificationSender.send(mime);
    }
}
