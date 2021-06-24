package sig.pojo.StudentLife;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author gustavo_schroeder
 */
@Entity
public class PersonaIntoxicationDigital implements Serializable {

    private String persona;
    private Integer appsByDayMin;
    private Integer appsByDayMax;
    private Integer smsByDayMin;
    private Integer smsByDayMax;
    private Integer timeDarkEnviromentMin;
    private Integer timeDarkEnviromentMax;
    private Integer sleepTimeMin;
    private Integer sleepTimeMax;
    private Integer sleepQualityMin;
    private Integer sleepQualityMax;
    private Integer stressScaleMin;
    private Integer stressScaleMax;
    private Integer depressionMmin;
    private Integer depressionMax;
    private Integer stressLevelMin;
    private Integer stressLevelMax;

    public PersonaIntoxicationDigital() {
    }

    @Id
    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public Integer getAppsByDayMin() {
        return appsByDayMin;
    }

    public void setAppsByDayMin(Integer appsByDayMin) {
        this.appsByDayMin = appsByDayMin;
    }

    public Integer getAppsByDayMax() {
        return appsByDayMax;
    }

    public void setAppsByDayMax(Integer appsByDayMax) {
        this.appsByDayMax = appsByDayMax;
    }

    public Integer getSmsByDayMin() {
        return smsByDayMin;
    }

    public void setSmsByDayMin(Integer smsByDayMin) {
        this.smsByDayMin = smsByDayMin;
    }

    public Integer getSmsByDayMax() {
        return smsByDayMax;
    }

    public void setSmsByDayMax(Integer smsByDayMax) {
        this.smsByDayMax = smsByDayMax;
    }

    public Integer getTimeDarkEnviromentMin() {
        return timeDarkEnviromentMin;
    }

    public void setTimeDarkEnviromentMin(Integer timeDarkEnviromentMin) {
        this.timeDarkEnviromentMin = timeDarkEnviromentMin;
    }

    public Integer getTimeDarkEnviromentMax() {
        return timeDarkEnviromentMax;
    }

    public void setTimeDarkEnviromentMax(Integer timeDarkEnviromentMax) {
        this.timeDarkEnviromentMax = timeDarkEnviromentMax;
    }

    public Integer getSleepTimeMin() {
        return sleepTimeMin;
    }

    public void setSleepTimeMin(Integer sleepTimeMin) {
        this.sleepTimeMin = sleepTimeMin;
    }

    public Integer getSleepTimeMax() {
        return sleepTimeMax;
    }

    public void setSleepTimeMax(Integer sleepTimeMax) {
        this.sleepTimeMax = sleepTimeMax;
    }

    public Integer getSleepQualityMin() {
        return sleepQualityMin;
    }

    public void setSleepQualityMin(Integer sleepQualityMin) {
        this.sleepQualityMin = sleepQualityMin;
    }

    public Integer getSleepQualityMax() {
        return sleepQualityMax;
    }

    public void setSleepQualityMax(Integer sleepQualityMax) {
        this.sleepQualityMax = sleepQualityMax;
    }

    public Integer getStressScaleMin() {
        return stressScaleMin;
    }

    public void setStressScaleMin(Integer stressScaleMin) {
        this.stressScaleMin = stressScaleMin;
    }

    public Integer getDepressionMax() {
        return depressionMax;
    }

    public void setDepressionMax(Integer depressionMax) {
        this.depressionMax = depressionMax;
    }

    public Integer getStressScaleMax() {
        return stressScaleMax;
    }

    public void setStressScaleMax(Integer stressScaleMax) {
        this.stressScaleMax = stressScaleMax;
    }

    public Integer getDepressionMmin() {
        return depressionMmin;
    }

    public void setDepressionMmin(Integer depressionMmin) {
        this.depressionMmin = depressionMmin;
    }

    public Integer getStressLevelMin() {
        return stressLevelMin;
    }

    public void setStressLevelMin(Integer stressLevelMin) {
        this.stressLevelMin = stressLevelMin;
    }

    public Integer getStressLevelMax() {
        return stressLevelMax;
    }

    public void setStressLevelMax(Integer stressLevelMax) {
        this.stressLevelMax = stressLevelMax;
    }

}
