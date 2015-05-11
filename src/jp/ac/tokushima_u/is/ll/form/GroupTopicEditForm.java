package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

/**
 *
 * @author Vstar
 */
public class GroupTopicEditForm implements Serializable {

    private static final long serialVersionUID = -2776369358155332833L;
    private String topicId;
    private String title;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
