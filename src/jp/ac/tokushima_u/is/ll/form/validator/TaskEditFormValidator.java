package jp.ac.tokushima_u.is.ll.form.validator;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.TaskEditForm;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Vstar
 */
public class TaskEditFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return ItemEditForm.class.equals(clazz);
    }
    
    private static String[] ALLOW_EXTNAME = new String[]{
        "jpeg",
        "jpg",
        "png",
        "gif",
        "bmp",
        "wmv",
        "mp4",
        "3gp",
        "avi",
        "mpg",
        "flv",
        "mov",
        "rmvb",
        "rm",
        "asf",
        "mp3",
        "wma",
        "ogg",
        "wav",
        "pdf"
    }; 
    private static String[] IMAGE_EXTNAME = new String[]{
        "jpeg",
        "jpg",
        "png",
        "gif",
        "bmp"
    };
    private static long MAX_FILE_SIZE = 50 * 1024 * 1024;

    public void validate(Object target, Errors errors) {
        TaskEditForm form = (TaskEditForm) target;
       if(form.getTitle()==null || form.getTitle().trim().length()<=0)
    	   errors.reject("title", "Titile is empty");
    	   
//        MultipartFile file = form.getImage();
//        if (file == null || file.isEmpty() || file.getSize() == 0 && !form.isFileExist()) {
////            result.rejectValue("image", "itemEditForm.image.empty");
//        } else {
//            if (file.getSize() > MAX_FILE_SIZE) {
//                errors.rejectValue("image", "itemEditForm.image.fileSizeTooBig", new String[]{"50M"}, "Max file size is 50M!");
//            }
//            if (!ArrayUtils.contains(ALLOW_EXTNAME, StringUtils.lowerCase(FilenameUtils.getExtension(file.getOriginalFilename())))) {
//                errors.rejectValue("image", "itemEditForm.image.invalidFormat");
//            }
//        }
    
    }
}
