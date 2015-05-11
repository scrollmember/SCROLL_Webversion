package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

/**
 *
 * @author Vstar
 */
public class GroupPostEditForm implements Serializable {

    private static final long serialVersionUID = -3422263457606769205L;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
