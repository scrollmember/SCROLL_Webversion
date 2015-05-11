package jp.ac.tokushima_u.is.ll.service.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;

import jp.ac.tokushima_u.is.ll.form.EmailModel;
import jp.ac.tokushima_u.is.ll.service.MailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmsMailListener implements MessageListener {
	
	private static Logger logger = LoggerFactory.getLogger(JmsMailListener.class);
	
	@Autowired
	private MailService mailService;
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage msg = (ObjectMessage) message;
			try {
				if(msg.getObject() instanceof EmailModel){
					EmailModel email = (EmailModel)msg.getObject();
					mailService.sendMail(email);
				}
			} catch (JMSException e) {
				logger.error("Exception on receiving mail", e);
			} catch (MessagingException e) {
				logger.error("Exception on sending mail", e);
			} catch (IOException e) {
				logger.error("Exception on sending mail", e);
			}
		}
	}
}
