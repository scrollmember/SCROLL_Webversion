package jp.ac.tokushima_u.is.ll.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;


public class AesUtils {
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES"); // KeyGenerator提供（对称）密钥生成器的功能。使用getInstance
																	// 类方法构造密钥生成器。
			kgen.init(128, new SecureRandom(password.getBytes()));// 使用用户提供的随机源初始化此密钥生成器，使其具有确定的密钥大小。
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 使用SecretKeySpec类来根据一个字节数组构造一个
																		// SecretKey,，而无须通过一个（基于
																		// provider
																		// 的）SecretKeyFactory.
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器 //为创建 Cipher
														// 对象，应用程序调用 Cipher 的
														// getInstance 方法并将所请求转换
														// 的名称传递给它。还可以指定提供者的名称（可选）。
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent); // 按单部分操作加密或解密数据，或者结束一个多部分操作。数据将被加密或解密（具体取决于此
															// Cipher 的初始化方式）。
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
		} catch (InvalidKeyException e) {
//			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
		} catch (BadPaddingException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
		} catch (InvalidKeyException e) {
//			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
		} catch (BadPaddingException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	public static String encryptString(String plainText, String password){
		if(StringUtils.isBlank(plainText) || StringUtils.isBlank(password)){
			return null;
		}
		byte[] result = AesUtils.encrypt(plainText, password);
		if(result!=null){
			return Base64.encodeBase64String(result);
		}else{
			return null;
		}
	}
	
	public static String decryptString(String cipherText, String password){
		if(StringUtils.isBlank(cipherText) || StringUtils.isBlank(password)){
			return null;
		}
		byte[] cipher = null;
		try {
			cipher = Base64.decodeBase64(cipherText);
		} catch (Exception e) {
			return null;
		}
		byte[] result = AesUtils.decrypt(cipher, password);
		if(result!=null){
			return new String(result);
		}else{
			return null;
		}
	}
}
