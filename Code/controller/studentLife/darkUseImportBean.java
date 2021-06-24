package sig.controller.studentLife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;
import sig.pojo.StudentLife.Dark;
import sig.util.FileExtensionCheckUtil;
import sig.util.JPAUtil;

/**
 *
 * @author gustavo_schroeder
 */
@ManagedBean
@ViewScoped
public class darkUseImportBean implements Serializable {

    private UploadedFile arquivo;

    public void uploadDarkUse() throws IOException {
        if (FileExtensionCheckUtil.checkFile(this.arquivo.getFileName(), this.arquivo.getSize(), 1000)) {
            return;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
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
        String linha;
        try {
            linha = reader.readLine();
            linha = reader.readLine();
        } catch (IOException ex) {
            FacesMessage msg = new FacesMessage("Oooops!",
                    "Não conseguimos abrir o arquivo, você tem certeza o arquivo contém informação?");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        String linhaq[];
        em.getTransaction().begin();
        Integer i = 0;
        while (linha != null) {
            if (linha.contains("start") || linha.contains("end")) {
                linha = reader.readLine();
                continue;
            }
            linhaq = linha.split(",");

            Long timeInicial = Long.parseLong(linhaq[0].trim());
            Long timeFinal = Long.parseLong(linhaq[1].trim());
            Date inicialDate = new Date(timeInicial * 1000);
            Date finalDate = new Date(timeFinal * 1000);

            Dark dark = new Dark(timeInicial, timeFinal, inicialDate, finalDate, diffHours(inicialDate, finalDate));

            em.merge(dark);

            i++;
            if ((i % 2000) == 0.0) {
                em.flush();
                em.clear();
            }
            System.out.println(i);

            try {
                linha = reader.readLine();
            } catch (IOException ex) {
            }
        }
        em.getTransaction().commit();
        em.close();

        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(reader);
        try {
            input.close();
        } catch (IOException ex) {
        }

        distributeDevices();

        FacesMessage msg = new FacesMessage("Sucesso!",
                this.arquivo.getFileName() + " foi Importado.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void distributeDevices() {
        List<String> auxList = fetchDevices();
        List<Dark> darkList = fetchDarkTime();
        Integer quantity = darkList.size() / auxList.size();
        Integer i = 0;
        Integer max = 0;
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        for (Dark dark : darkList) {
            try {
                dark.setDevice(auxList.get(i));
            } catch (IndexOutOfBoundsException e) {
                i--;
                dark.setDevice(auxList.get(i));
            }
            em.merge(dark);
            if (++max == quantity.intValue()) {
                max = 0;
                i++;
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    private List<Dark> fetchDarkTime() {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("SELECT i FROM Dark i");
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
