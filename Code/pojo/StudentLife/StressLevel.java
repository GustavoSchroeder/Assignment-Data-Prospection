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
public class StressLevel implements Serializable {

    private Long id;
    private String device;
    private Integer levelInt;
    private String location;
    private Long responseTime;

    public StressLevel() {
    }

    public StressLevel(Integer levelInt, String location, Long responseTime) {
        this.levelInt = levelInt;
        this.location = location;
        this.responseTime = responseTime;
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

    public Integer getLevelInt() {
        return levelInt;
    }

    public void setLevelInt(Integer levelInt) {
        this.levelInt = levelInt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

}
