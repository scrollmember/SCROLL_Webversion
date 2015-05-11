package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

/**
 *
 * @author houbin
 */
public class ItemCommentForm implements Serializable {

    private static final long serialVersionUID = 6717175380492348527L;
    private String tag;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
