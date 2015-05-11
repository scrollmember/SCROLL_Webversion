package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author houbin
 */
public class EmailModel implements Serializable {

    private static final long serialVersionUID = 7536601668866673866L;
    private String from;
    private String replyTo;
    private String address;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private boolean html = true;
    private MultipartFile[] attachment = new MultipartFile[0];

    public String[] getAddresses() {
        if (!StringUtils.hasLength(this.address)) {
            return null;
        }
        address = address.trim();
        address.replaceAll("；", ";");
        address.replaceAll(" ", ";");
        address.replaceAll(",", ";");
        address.replaceAll("|", ";");
        return address.split(";");
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MultipartFile[] getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile[] attachment) {
        this.attachment = attachment;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
