package jp.ac.tokushima_u.is.ll.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Vstar
 */
public class FilenameUtil {

//    public static String getExtName(String fileName) {
//        return FilenameUtils.getExtension(fileName);
//    }
    
    public static String[] IMAGE_EXTNAME = new String[]{
        "jpeg",
        "jpg",
        "png",
        "gif",
        "bmp"
    };
    
    public static String[] VIDEO_EXTNAME = new String[]{
        "wmv",
        "mp4",
        "ogv",
        "3gp",
        "avi",
        "mpg",
        "flv",
        "mov",
        "rmvb",
        "rm",
        "asf"
    };
    
    public static String[] AUDIO_EXTNAME = new String[]{
    	"wma",
    	"mp3",
    	"ogg",
    	"wav"
    };
    
    public static String[] PDF_EXTNAME = new String[]{
    	"pdf"
    };
    
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String PDF = "pdf";
    public static final String UNKNOWN = "unknown";
    
    public static String checkMediaType(String filename){
    	String extName = StringUtils.lowerCase(FilenameUtils.getExtension(filename));
    	if (ArrayUtils.contains(IMAGE_EXTNAME, extName)) {
            return IMAGE;
        }else if(ArrayUtils.contains(VIDEO_EXTNAME, extName)){
        	return VIDEO;
        }else if(ArrayUtils.contains(AUDIO_EXTNAME, extName)){
        	return AUDIO;
        }else if(ArrayUtils.contains(PDF_EXTNAME, extName)){
        	return PDF;
        }else{
        	return UNKNOWN;
        }
    }
}
