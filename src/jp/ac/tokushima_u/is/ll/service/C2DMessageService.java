package jp.ac.tokushima_u.is.ll.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.c2dm.C2DMessaging;
import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.C2DMCode;
import jp.ac.tokushima_u.is.ll.entity.C2DMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.util.SerializeUtil;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("c2dmessageService")
@Transactional
public class C2DMessageService {
	private HibernateDao<C2DMessage, String> c2DMessageDao;
	
	@Autowired
	private C2DMService c2dmService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.c2DMessageDao = new HibernateDao<C2DMessage,String>(sessionFactory, C2DMessage.class);
	}
	
	@SuppressWarnings("unchecked")
	public void doSend(){
		C2DMessaging messaging = C2DMessaging.getInstance(c2dmService);
		DetachedCriteria dc = DetachedCriteria.forClass(C2DMessage.class);
		dc.add(Restrictions.eq("disabled", 0));
		List<C2DMessage> messages = this.c2DMessageDao.find(dc);
		for(C2DMessage message:messages){
			try{
				HashMap<String,String[]> params = (HashMap<String,String[]>)SerializeUtil.deSerialize(message.getParams());
				boolean flg = message.getIsDelayIdle() == 1? true:false;
				flg = messaging.sendNoRetry(message.getRegisterId(), message.getCollapse(), params, flg);
				if(flg){
					message.setDisabled(1);
					message.setUpdateTime(new Date());
					this.c2DMessageDao.save(message);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void addMessage(C2DMessage message, Users user){
		List<C2DMCode> codes = this.c2dmService.findCodeList(user);
		if(codes!=null&&codes.size()>0){
			for(C2DMCode code:codes){
				try{
					C2DMessage c2dmessage = new C2DMessage();
					c2dmessage.setRegisterId(code.getRegisterId());
					c2dmessage.setCollapse(message.getCollapse());
					c2dmessage.setCreateTime(new Date());
					c2dmessage.setUpdateTime(new Date());
					c2dmessage.setIsDelayIdle(message.getIsDelayIdle());
					c2dmessage.setDisabled(0);
					c2dmessage.setAuthor(user);
					c2dmessage.setParams(message.getParams());
					this.c2DMessageDao.save(c2dmessage);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
//		this.c2DMessageDao.save(message);
	}
}
