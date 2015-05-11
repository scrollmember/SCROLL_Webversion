/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.ws.service;

import jp.ac.tokushima_u.is.ll.entity.LogLogin;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.LogService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author li
 */
@Service("userRemoteService")
@Transactional
public class UserRemoteService {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    public String checkUser(String email, String password, boolean isLoginRequest){
         Users user = this.userService.validateUser(email, password);
         if(user==null){
             return null;
         }else
        	 if(isLoginRequest){
        		 logService.logUserLogin(user, LogLogin.Device.MOBILE);
        	 }
             return user.getId();
    }
    
    public String checkUser(String email, String password){
    	return this.checkUser(email, password, false);
    }
}
