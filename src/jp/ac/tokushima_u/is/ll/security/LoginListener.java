package jp.ac.tokushima_u.is.ll.security;

import jp.ac.tokushima_u.is.ll.entity.LogLogin;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.LogService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements
		AuthenticationListener {
	
	@Autowired
	private LogService logService;
	@Autowired
	private UserService userService;
	
	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException info) {
	}

	@Override
	public void onLogout(PrincipalCollection token) {
	}

	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
		Object o = token.getPrincipal();
		if(o instanceof Users){
			Users user = (Users)o;
			userService.updateLastLogin(user.getId());
			logService.logUserLogin(user, LogLogin.Device.WEB);
		}
	}
}
