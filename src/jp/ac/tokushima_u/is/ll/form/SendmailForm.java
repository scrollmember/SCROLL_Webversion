package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

/**
 *
 * @author Vstar
 */
public class SendmailForm implements Serializable{

	private static final long serialVersionUID = -5157788254960827374L;
	private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
