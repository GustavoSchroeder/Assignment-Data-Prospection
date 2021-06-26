package sig.controller.studentLife;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sig.pojo.StudentLife.AppUsage;
import sig.pojo.StudentLife.Dark;
import sig.pojo.StudentLife.DepressionScale;
import sig.pojo.StudentLife.EMASleep;
import sig.pojo.StudentLife.PersonaIntoxicationDigital;
import sig.pojo.StudentLife.SMS;
import sig.pojo.StudentLife.StressLevel;
import sig.pojo.StudentLife.StressScale;
import sig.util.JPAUtil;

/**
 *
 * @author gustavo_schroeder
 */
@ManagedBean
@ViewScoped
public class AnaliseManhattanBean implements Serializable {

    public void listDetailsUsers() {
        List<String> devices = fetchDevices();
        
        String distancias = "";

        System.out.println("device;apps_by_day;sms_by_day;dark_enviroment;"
                + "sleep_quality;sleep_rate;depression_scale;stress_scale;stress_level");
        String finalCalc = "";
        List<PersonaIntoxicationDigital> personas = fetchPersonas();
        for (String device : devices) {
            Map<PersonaIntoxicationDigital, Double> dictionary = new HashMap<>();
            Integer appsByDay = appsOpenByDay(device);
            Integer smsByDay = smsSentByDay(device);
            Integer timeDarkEnviroment = timeDarkEnviromentUse(device);
            Integer[] sleepQuality = timeSleep(device);
            Integer depression = calculateResponsesPHQ9(device);
            Integer stressScale = calculateStressLevel(device);
            Integer stressLevel = fetchStressLevel(device);

            for (PersonaIntoxicationDigital persona : personas) {
                Double appsUse = calcularDistanciaBack(appsByDay, persona.getAppsByDayMin(), persona.getAppsByDayMax());
                Double sms = calcularDistanciaBack(smsByDay, persona.getSmsByDayMin(), persona.getSmsByDayMax());
                Double dark = calcularDistanciaBack(timeDarkEnviroment, persona.getTimeDarkEnviromentMin(), persona.getTimeDarkEnviromentMax());
                Double sleepTime = calcularDistanciaBack(sleepQuality[0], persona.getSleepTimeMin(), persona.getSleepTimeMax());
                Double sleepQualityValue = calcularDistanciaBack(sleepQuality[1], persona.getSleepQualityMin(), persona.getSleepQualityMax());
                Double depressionValue = calcularDistanciaBack(depression, persona.getDepressionMmin(), persona.getDepressionMax());
                Double stressScaleLevel = calcularDistanciaBack(stressScale, persona.getStressScaleMin(), persona.getStressScaleMax());
                Double stressLevelValue = calcularDistanciaBack(stressLevel, persona.getStressLevelMin(), persona.getStressLevelMax());

                Double[] distancia = {
                    18 * appsUse / 577,
                    2 * sms / 37,
                    4 * dark.doubleValue() / 2,
                    1 * sleepTime / 7,
                    1 * sleepQualityValue.doubleValue() / 1,
                    15 * depressionValue.doubleValue() / 12,
                    15 * stressScaleLevel.doubleValue() / 18,
                    1 * stressLevelValue / 2};
                
                Double total = 0.0;
                distancias += "\n" + device;
                for (int i = 0; i < distancia.length; i++) {
                    //System.out.println(distancia[i]);
                    distancias += distancia[i] + ";";
                    total += distancia[i];
                }
                dictionary.put(persona, total);

            }

            dictionary = sortByValue(dictionary);
            Map<PersonaIntoxicationDigital, Double> output = analisarPersonaObject(dictionary);
            List<Map.Entry<PersonaIntoxicationDigital, Double>> list = new ArrayList<>(output.entrySet());

            String max = "";
            Integer trigger = 0;
            for (Entry<PersonaIntoxicationDigital, Double> entry : output.entrySet()) {
                PersonaIntoxicationDigital key = entry.getKey();
                Double value = entry.getValue();
                finalCalc += (device + ";" + key.getPersona() + ";" + value + "\n");
                if (trigger++ == 0) {
                    max = key.getPersona() + ";" + value.toString().replace(".", ",");
                }
            }
            System.out.println(device + ";" + appsByDay + ";" + smsByDay + ";"
                    + timeDarkEnviroment + ";" + sleepQuality[0] + ";"
                    + sleepQuality[1] + ";" + depression + ";" + stressScale + ";"
                    + +stressLevel + ";" + max);
        }
        System.out.println("---------------------------");
        System.out.println(finalCalc);
        System.out.println("---------------------------");
        System.out.println(distancias);
    }

    public Map<PersonaIntoxicationDigital, Double> analisarPersonaObject(Map<PersonaIntoxicationDigital, Double> dictionary) {
        Map<PersonaIntoxicationDigital, Double> dictionaryFilter = new HashMap<>();
        Map<PersonaIntoxicationDigital, Double> percentuais = new HashMap<>();
        Map<PersonaIntoxicationDigital, Double> outputMap = new HashMap<>();
        Integer max = 4;
        Boolean containsZero = Boolean.FALSE;
        for (Entry<PersonaIntoxicationDigital, Double> entry : dictionary.entrySet()) {
            if (max == 0) {
                break;
            }
            PersonaIntoxicationDigital key = entry.getKey();
            Double value = entry.getValue();
            dictionaryFilter.put(key, value);
            max--;
            if (value.intValue() == 0) {
                if (Objects.equals(containsZero, Boolean.FALSE)) {
                    percentuais.put(key, 1.0);
                } else {
                    percentuais.put(key, 0.0);
                }
                containsZero = Boolean.TRUE;
            }
        }

        if (!containsZero) {
            for (Entry<PersonaIntoxicationDigital, Double> entry : dictionaryFilter.entrySet()) {
                PersonaIntoxicationDigital key = entry.getKey();
                Double value = entry.getValue();
                percentuais.put(key, 1 / value);
            }
        }

        for (Entry<PersonaIntoxicationDigital, Double> entry : percentuais.entrySet()) {
            PersonaIntoxicationDigital key = entry.getKey();
            Double value = entry.getValue();
            outputMap.put(key, value / somarProbObject(percentuais));
        }

        outputMap = sortByValue(outputMap);
        for (Entry<PersonaIntoxicationDigital, Double> entry : outputMap.entrySet()) {
            PersonaIntoxicationDigital key = entry.getKey();
        }
        //System.out.println(documento + out + number);
        return outputMap;
    }

    public Double somarProbObject(Map<PersonaIntoxicationDigital, Double> percentuais) {
        Double output = 0.0;
        for (Entry<PersonaIntoxicationDigital, Double> entry : percentuais.entrySet()) {
            output += entry.getValue();
        }
        return output;
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public Double calcularDistanciaBack(Integer a, Integer b, Integer c) {
        Double d;
        if (a > c) {
            d = a.doubleValue() - c;
        } else if (a < b) {
            d = b.doubleValue() - a;
        } else {
            d = 0.0;
        }
        return d;
    }

    private List<PersonaIntoxicationDigital> fetchPersonas() {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM PersonaIntoxicationDigital i");
        try {
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    private Integer appsOpenByDay(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM AppUsage i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<AppUsage> appsUsage = query.getResultList();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Integer> dictionary = new HashMap<>();
            for (AppUsage appUsage : appsUsage) {
                if (null == dictionary.get(sdf.format(appUsage.getData()))) {
                    dictionary.put(sdf.format(appUsage.getData()), 0);
                }
                dictionary.put(sdf.format(appUsage.getData()), dictionary.get(sdf.format(appUsage.getData())) + 1);
            }
            Double med = 0.0;
            for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
                Integer value = entry.getValue();
                med += value;
            }
            return ((Double) (med / dictionary.size())).intValue();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    private Integer smsSentByDay(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM SMS i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<SMS> smsSent = query.getResultList();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Integer> dictionary = new HashMap<>();
            for (SMS appUsage : smsSent) {
                if (null == dictionary.get(sdf.format(appUsage.getDateCollection()))) {
                    dictionary.put(sdf.format(appUsage.getDateCollection()), 0);
                }
                dictionary.put(sdf.format(appUsage.getDateCollection()), dictionary.get(sdf.format(appUsage.getDateCollection())) + 1);
            }
            Double med = 0.0;
            for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
                Integer value = entry.getValue();
                med += value;
            }
            return ((Double) (med / dictionary.size())).intValue();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    private Integer fetchStressLevel(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM StressLevel i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<StressLevel> stressLevel = query.getResultList();
            Double med = 0.0;
            for (StressLevel stress : stressLevel) {
                med += stress.getLevelInt();
            }
            return ((Double) (med / stressLevel.size())).intValue();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    private Integer timeDarkEnviromentUse(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM Dark i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<Dark> dark = query.getResultList();

            Double med = 0.0;
            for (Dark darkTime : dark) {
                med += darkTime.getHours();
            }
            return ((Double) (med / dark.size())).intValue();
//            Integer max = 0;
//            for (Dark darkTime : dark) {
//                if (max < darkTime.getHours()) {
//                    max = darkTime.getHours();
//                }
//            }
//            return max;
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    private Integer[] timeSleep(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM EMASleep i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<EMASleep> emaSleep = query.getResultList();

            Double med = 0.0;
            Double medQuality = 0.0;
            for (EMASleep emaSleepObj : emaSleep) {
                med += emaSleepObj.getHour();
                medQuality += emaSleepObj.getRate();
            }
            medQuality = (medQuality / (Integer) emaSleep.size());
            Integer medInt = medQuality.intValue();
            if (medInt > 1000.0) {
                medInt = 0;
            }
            Integer[] outputObj = {((Double) (med / emaSleep.size())).intValue(), medInt};
            return outputObj;

        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    //https://www.pcpcc.org/sites/default/files/resources/instructions.pdf
    public Integer calculateResponsesPHQ9(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM DepressionScale i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<DepressionScale> list = query.getResultList();
            return calculateSeverityCutpointsPQ9(list.get(0));
        } catch (Exception e) {
            return -1;
        } finally {
            em.close();
        }
    }

    private Integer calculateSeverityCutpointsPQ9(DepressionScale depressionScale) {
        Integer score = transformPHQ9Responde(depressionScale.getQuestion1())
                + transformPHQ9Responde(depressionScale.getQuestion2()) + transformPHQ9Responde(depressionScale.getQuestion3())
                + transformPHQ9Responde(depressionScale.getQuestion4()) + transformPHQ9Responde(depressionScale.getQuestion5())
                + transformPHQ9Responde(depressionScale.getQuestion6()) + transformPHQ9Responde(depressionScale.getQuestion7())
                + transformPHQ9Responde(depressionScale.getQuestion8()) + transformPHQ9Responde(depressionScale.getQuestion9());
        return score;
    }

    private Integer transformPHQ9Responde(String response) {
        switch (response) {
            case "Several days":
                return 0;
            case "More than half the days":
                return 1;
            case "Not at all":
                return 2;
            case "Nearly every day":
                return 3;
        }
        return 0;
    }

    private List<String> fetchDevices() {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT DISTINCT(i.device) FROM AppUsage i");
        try {
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    //https://das.nh.gov/wellness/docs/percieved%20stress%20scale.pdf
    public Integer calculateStressLevel(String device) {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM StressScale i WHERE i.device = :device");
        query.setParameter("device", device);
        try {
            List<StressScale> list = query.getResultList();
            return calculateStressLevelSeverity(list.get(0));
        } catch (Exception e) {
            return -1;
        } finally {
            em.close();
        }
    }

    private Integer calculateStressLevelSeverity(StressScale stressLevel) {
        Integer score = calculateStressScaleLevel(stressLevel.getQuestion1(), 1)
                + calculateStressScaleLevel(stressLevel.getQuestion2(), 1) + calculateStressScaleLevel(stressLevel.getQuestion3(), 3)
                + calculateStressScaleLevel(stressLevel.getQuestion4(), 4) + calculateStressScaleLevel(stressLevel.getQuestion5(), 5)
                + calculateStressScaleLevel(stressLevel.getQuestion6(), 6) + calculateStressScaleLevel(stressLevel.getQuestion7(), 7)
                + calculateStressScaleLevel(stressLevel.getQuestion8(), 8) + calculateStressScaleLevel(stressLevel.getQuestion9(), 9)
                + calculateStressScaleLevel(stressLevel.getQuestion9(), 10);
        return score;
    }

    private Integer calculateStressScaleLevel(String response, Integer question) {
        switch (question) {
            case 4:
            case 5:
            case 7:
            case 8:
                switch (response) {
                    case "Very often":
                        return 0;
                    case "Fairly often":
                        return 1;
                    case "Sometime":
                        return 2;
                    case "Almost never":
                        return 3;
                    case "Never":
                        return 4;
                }
            default:
                switch (response) {
                    case "Very often":
                        return 4;
                    case "Fairly often":
                        return 3;
                    case "Sometime":
                        return 2;
                    case "Almost never":
                        return 1;
                    case "Never":
                        return 0;
                }
        }
        return 0;
    }

}
