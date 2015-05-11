package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name = "M_MAIL")
public class Mail implements Serializable {

    private static final long serialVersionUID = 3860165061809007813L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mailid;
    @Column(length = 100)
    private String messageid;
    @Column(length = 100)
    private String sender;
    @Column(length = 100)
    private String receiver;
    @Column(length = 200)
    private String subject;
    @Column(length = 2000)
    private String content;
    @Column
    private Integer testtypeid;
    @Column
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date sendDate;
    @ManyToOne(cascade = {})
    @JoinColumn(name = "userid")
    private Users author;
    @OneToMany(mappedBy = "mail")
    private List<FileData> attachedfiles = new ArrayList<FileData>();

    public List<FileData> getAttachedfiles() {
        return attachedfiles;
    }

    public void setAttachedfiles(List<FileData> attachedfiles) {
        this.attachedfiles = attachedfiles;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMailid() {
        return mailid;
    }

    public void setMailid(Long mailid) {
        this.mailid = mailid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getTesttypeid() {
        return testtypeid;
    }

    public void setTesttypeid(Integer testtypeid) {
        this.testtypeid = testtypeid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mailid != null ? mailid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mail)) {
            return false;
        }
        Mail other = (Mail) object;
        if ((this.mailid == null && other.mailid != null) || (this.mailid != null && !this.mailid.equals(other.mailid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.Mail[id=" + mailid + "]";
    }
}
