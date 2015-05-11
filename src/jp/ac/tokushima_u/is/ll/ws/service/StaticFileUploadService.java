package jp.ac.tokushima_u.is.ll.ws.service;

import jp.ac.tokushima_u.is.ll.ws.service.model.StaticFileUploadModel;

/**
 *
 * @author Houbin
 */
public interface StaticFileUploadService {

    void uploadImage(StaticFileUploadModel model, String project);

    void uploadVideo(StaticFileUploadModel model, String project);
}
