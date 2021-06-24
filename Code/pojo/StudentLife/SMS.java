package sig.pojo.StudentLife;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author gustavo_schroeder
 */

@Entity
public class SMS implements Serializable {
    private Long id;
    private String device;
    private Long timeStampCollection;
    private Date dateCollection;
    private String messagesAdress;
    private String messageBody;
    private Long messageTimeStamp;
    private Date messageDate;

    public SMS(String device, Long timeStampCollection, 
            Date dateCollection, String messagesAdress, String messageBody, 
            Long messageTimeStamp, Date messageDate) {
        this.device = device;
        this.timeStampCollection = timeStampCollection;
        this.dateCollection = dateCollection;
        this.messagesAdress = messagesAdress;
        this.messageBody = messageBody;
        this.messageTimeStamp = messageTimeStamp;
        this.messageDate = messageDate;
    }

    public SMS() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Long getTimeStampCollection() {
        return timeStampCollection;
    }

    public void setTimeStampCollection(Long timeStampCollection) {
        this.timeStampCollection = timeStampCollection;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDateCollection() {
        return dateCollection;
    }

    public void setDateCollection(Date dateCollection) {
        this.dateCollection = dateCollection;
    }

    public String getMessagesAdress() {
        return messagesAdress;
    }

    public void setMessagesAdress(String messagesAdress) {
        this.messagesAdress = messagesAdress;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getMessageTimeStamp() {
        return messageTimeStamp;
    }

    public void setMessageTimeStamp(Long messageTimeStamp) {
        this.messageTimeStamp = messageTimeStamp;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }    
    
    
}
