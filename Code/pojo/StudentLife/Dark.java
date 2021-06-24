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
public class Dark implements Serializable {

    private Long id;
    private Long startLong;
    private Long endLong;
    private Date startDate;
    private Date endDate;
    private Integer hours;
    private String device;

    public Dark() {
    }

    public Dark(Long start, Long end, 
            Date startDate, Date endDate, Integer hours) {
        this.startLong = start;
        this.endLong = end;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = hours;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartLong() {
        return startLong;
    }

    public void setStartLong(Long startLong) {
        this.startLong = startLong;
    }

    public Long getEndLong() {
        return endLong;
    }

    public void setEndLong(Long endLong) {
        this.endLong = endLong;
    }
    
    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
