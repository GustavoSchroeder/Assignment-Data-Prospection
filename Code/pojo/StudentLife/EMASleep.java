package sig.pojo.StudentLife;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author gustavo_schroeder
 */
@Entity
public class EMASleep implements Serializable {

    private Long id;
    private String device;
    private Integer hour;
    private String location;
    private Integer rate;
    private Long responseTime;
    private Integer social;

    public EMASleep() {
    }

    public EMASleep(Integer hour, String location, Integer rate, Long responseTime, Integer social) {
        this.hour = hour;
        this.location = location;
        this.rate = rate;
        this.responseTime = responseTime;
        this.social = social;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getSocial() {
        return social;
    }

    public void setSocial(Integer social) {
        this.social = social;
    }
}
