/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sig.controller.studentLife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;
import sig.pojo.StudentLife.AppUsage;
import sig.util.FileExtensionCheckUtil;
import sig.util.JPAUtil;

/**
 *
 * @author gustavo_schroeder
 */
@ManagedBean
@ViewScoped
public class runningAppImportBean implements Serializable {

    private UploadedFile arquivo;

    public void uploadAppUsage() throws IOException {
        if (FileExtensionCheckUtil.checkFile(this.arquivo.getFileName(), this.arquivo.getSize(), 1000)) {
            return;
        }
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
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
            if (linha.contains("timestamp")) {
                linha = reader.readLine();
                continue;
            }
            linhaq = linha.split(",");

            String device = linhaq[1].trim();
            Long timeStampTime = Long.parseLong(linhaq[2].trim());
            Date data = new Date(timeStampTime * 1000);
            String runningTaskBaseMPackage = linhaq[8].trim();
            String runningTasks = linhaq[9].trim();

            AppUsage appU = new AppUsage(device, timeStampTime, data, runningTaskBaseMPackage, runningTaskBaseMPackage, runningTasks);

            em.merge(appU);

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

        FacesMessage msg = new FacesMessage("Sucesso!",
                this.arquivo.getFileName() + " foi Importado.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }

}
