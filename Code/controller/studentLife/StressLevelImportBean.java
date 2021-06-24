package sig.controller.studentLife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.primefaces.model.file.UploadedFile;
import sig.pojo.StudentLife.StressLevel;
import sig.util.JPAUtil;

/**
 *
 * @author gustavo_schroeder
 */
@ManagedBean
@ViewScoped
public class StressLevelImportBean implements Serializable {

    private UploadedFile arquivo;

    public void upload() throws IOException {
        EntityManager em = JPAUtil.getEntityManager();
        InputStream is;
        try {
            is = this.arquivo.getInputStream();
        } catch (IOException ex) {
            FacesMessage msg = new FacesMessage("Oooops!",
                    "Ocorreu um erro ao abrir o arquivo :(");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        InputStreamReader input = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(input);

        em.getTransaction().begin();
        String linha;
        while ((linha = reader.readLine()) != null) {
            Integer levelInt = 0;
            String location = "";
            Long responseTime = 0L;
            if (linha.contains("level")) {
                linha = linha.replace(",", "").replace("\"", "").trim();
                String[] aux = linha.split(":");
                levelInt = Integer.parseInt(aux[1].trim());
                linha = reader.readLine().replace("\"", "").trim();
                aux = linha.split(":");
                location = aux[1].trim();
                linha = reader.readLine().replace("\"", "").trim();
                aux = linha.split(":");
                responseTime = Long.parseLong(aux[1].replace(",", "").trim());
                StressLevel stressLevel = new StressLevel(levelInt, location, responseTime);
                em.merge(stressLevel);
            }
        }

        em.getTransaction().commit();
        em.close();

        distributeDevices();
        FacesMessage msg = new FacesMessage("Sucesso!",
                this.arquivo.getFileName() + " foi Importado.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void distributeDevices() {
        List<String> auxList = fetchDevices();
        List<StressLevel> emaSleep = fetchStressLevel();
        Integer quantity = emaSleep.size() / auxList.size();
        Integer i = 0;
        Integer max = 0;
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        for (StressLevel obj : emaSleep) {
            try {
                obj.setDevice(auxList.get(i));
            } catch (IndexOutOfBoundsException e) {
                i = 0;
                obj.setDevice(auxList.get(i));
            }
            em.merge(obj);
            if (++max == quantity.intValue()) {
                max = 0;
                i++;
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    private List<StressLevel> fetchStressLevel() {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM StressLevel i");
        try {
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            em.close();
        }
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

    private Integer diffHours(Date d1, Date d2) {

        Long diff = d2.getTime() - d1.getTime();
        Long diffHours = diff / (60 * 60 * 1000);
        return diffHours.intValue();
    }

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }

}
