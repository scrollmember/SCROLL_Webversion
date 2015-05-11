package jp.ac.tokushima_u.is.ll.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class HashUtils {
	
	public static String md5Hex(String filename){
		
		if(StringUtils.isBlank(filename))return "";
		File file = new File(filename);
		if(file.exists() && file.isFile()){
			try {
				return DigestUtils.md5Hex(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
	}
	
	public static String shaHex(String filename){
		
		if(StringUtils.isBlank(filename))return "";
		File file = new File(filename);
		if(file.exists() && file.isFile()){
			try {
				return DigestUtils.shaHex(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
	}
	
	public static String shaHex(File file){
		try {
			return DigestUtils.shaHex(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
