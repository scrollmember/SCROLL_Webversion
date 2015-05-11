package jp.ac.tokushima_u.is.ll.form.validator;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Vstar
 */
public class ImageFileValidator implements Validator {

    private static String[] ALLOW_TYPES = new String[]{
        "image/pjpeg",
        "image/jpeg",
        "image/x-png",
        "image/png",
        "image/gif"
    };

    private static long MAX_FILE_SIZE = 5*1024*1024;

    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (target == null) {
            return;
        }
        MultipartFile file = (MultipartFile)target;
        if (file.getSize() > MAX_FILE_SIZE){
            errors.reject("image.fileSizeTooBig", new String[]{"5M"}, "Max file size is 5M!");
        }
        if (!ArrayUtils.contains(ALLOW_TYPES, file.getContentType())){
            errors.reject("image.invalidFormat");
        }
    }
}
