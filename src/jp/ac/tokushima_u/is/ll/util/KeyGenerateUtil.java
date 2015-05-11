package jp.ac.tokushima_u.is.ll.util;

import java.util.UUID;

import org.apache.shiro.crypto.hash.Sha1Hash;

public class KeyGenerateUtil {
	
	public static String generateFullUUID(){
		return UUID.randomUUID().toString();
	}
	
	public static String generateIdUUID(){
		return KeyGenerateUtil.generateFullUUID().replace("-", "");
	}
	
	public static String generateRandomSha(){		
		return new Sha1Hash(KeyGenerateUtil.generateFullUUID(), "LearningLog").toHex();

	}
	
	public static void main(String[] args){
		System.out.println(KeyGenerateUtil.generateRandomSha());
	}
}
