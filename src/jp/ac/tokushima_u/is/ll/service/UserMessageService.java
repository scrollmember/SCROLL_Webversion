package jp.ac.tokushima_u.is.ll.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.dao.MessageDao;
import jp.ac.tokushima_u.is.ll.dao.UsersDao;
import jp.ac.tokushima_u.is.ll.entity.MymessageList;
import jp.ac.tokushima_u.is.ll.entity.UserMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMessageService {

	@Autowired
	private UserService userService;
	
	private HibernateDao<UserMessage, String> userMessageDao;
	
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        userMessageDao = new HibernateDao<UserMessage, String>(sessionFactory, UserMessage.class);
    }
	
    @Autowired
    private MessageDao messageDao;
    
	public void send(String sendFrom, String sendTo, String content) {
		Users sendFromUser = userService.getById(sendFrom);
		Users sendToUser = userService.getById(sendTo);
		UserMessage msg = new UserMessage();
		msg.setContent(content);
		msg.setSendFrom(sendFromUser);
		msg.setSendTo(sendToUser);
		msg.setCreateTime(new Date());
		msg.setReadFlag(false);
		userMessageDao.save(msg);
	}

	@Transactional(readOnly = true)
	public String checkMessage(String uid) {
		Users sendTo = userService.getById(uid);
		if(sendTo==null) return "";
		Map<String, Object> param = new HashMap<String, Object>(0);
		param.put("sendTo", sendTo);
		Long count = userMessageDao.findUnique("select count(*) from UserMessage msg where msg.sendTo=:sendTo and msg.readFlag=false", param);
		return String.valueOf(count);
	}

	@Transactional(readOnly = true)
	public List<UserMessage> searchAll(String uid) {
		Users sendTo = userService.getById(uid);
		Map<String, Object> param = new HashMap<String, Object>(0);
		param.put("sendTo", sendTo);
		return userMessageDao.find("from UserMessage msg where msg.sendTo=:sendTo order by msg.createTime desc", param);
	}

	@Transactional(readOnly = true)
	public UserMessage getById(String msgId) {
		return userMessageDao.get(msgId);
	}

	public void changeToRead(String id) {
		Date date = new Date();
		UserMessage msg = userMessageDao.get(id);
		msg.setReadFlag(true);
		msg.setReadTime(date);
		userMessageDao.save(msg);
	}
	
	public List<MymessageList> searchMymail(String id){
		
		
		return messageDao.searchmylist(id);
		
	}
}
