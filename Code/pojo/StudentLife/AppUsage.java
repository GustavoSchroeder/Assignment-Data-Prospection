/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AppUsage implements Serializable {
    private Long id;
    private String device;
    private Long timeStampTime;
    private Date data;
    private String runningTaskBaseActivity;
    private String runningTaskBaseMPackage;
    private String runningTasks;

    public AppUsage() {
    }

    public AppUsage(String device, Long timeStampTime, Date data, 
            String runningTaskBaseActivity, String runningTaskBaseMPackage, String runningTasks) {
        this.device = device;
        this.timeStampTime = timeStampTime;
        this.data = data;
        this.runningTaskBaseActivity = runningTaskBaseActivity;
        this.runningTaskBaseMPackage = runningTaskBaseMPackage;
        this.runningTasks = runningTasks;
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

    public Long getTimeStampTime() {
        return timeStampTime;
    }

    public void setTimeStampTime(Long timeStampTime) {
        this.timeStampTime = timeStampTime;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getRunningTaskBaseActivity() {
        return runningTaskBaseActivity;
    }

    public void setRunningTaskBaseActivity(String runningTaskBaseActivity) {
        this.runningTaskBaseActivity = runningTaskBaseActivity;
    }

    public String getRunningTaskBaseMPackage() {
        return runningTaskBaseMPackage;
    }

    public void setRunningTaskBaseMPackage(String runningTaskBaseMPackage) {
        this.runningTaskBaseMPackage = runningTaskBaseMPackage;
    }

    public String getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(String runningTasks) {
        this.runningTasks = runningTasks;
    }
    
}
