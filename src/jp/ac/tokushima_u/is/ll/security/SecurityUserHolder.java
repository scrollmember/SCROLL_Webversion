package jp.ac.tokushima_u.is.ll.security;

import jp.ac.tokushima_u.is.ll.entity.Users;

import org.apache.shiro.SecurityUtils;

/**
 *
 * @author houbin
 */
public class SecurityUserHolder {

    public static Users getCurrentUser() {
    	try{
    		Users user = (Users) SecurityUtils.getSubject().getPrincipal();
    		return user;
    	}catch(Exception e){
    		return null;
    	}
    }
}
