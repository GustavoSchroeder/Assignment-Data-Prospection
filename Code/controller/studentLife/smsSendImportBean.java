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
import sig.pojo.StudentLife.SMS;
import sig.util.FileExtensionCheckUtil;
import sig.util.JPAUtil;

/**
 *
 * @author gustavo_schroeder
 */
@ManagedBean
@ViewScoped
public class smsSendImportBean implements Serializable {

    private UploadedFile arquivo;

    public void uploadSMS() throws IOException {
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
            if (linha.contains("timestamp")) {
                linha = reader.readLine();
                continue;
            }
            linhaq = linha.split(",");

            String device = linhaq[1].trim();
            Long timestampSavedInfo = Long.parseLong(linhaq[2].trim());
            Date dateSaveInfo = new Date(timestampSavedInfo * 1000);
            String messagesAdress = "";
            String messageBody = "";
            Long messageTimeStamp = 0L;
            Date messageDate = new Date();
            try {
                messagesAdress = linhaq[3].trim();
                System.out.println(linha);
            } catch (Exception e) {
            }
            try {
                messageBody = linhaq[4].trim();
            } catch (Exception e) {
            }
            try {
                messageTimeStamp = Long.parseLong(linhaq[5].trim());
            } catch (Exception e) {
            }
            try {
                messageDate = new Date(messageTimeStamp * 1000);
            } catch (Exception e) {
            }

            SMS sms = new SMS(device, timestampSavedInfo, dateSaveInfo, messagesAdress, messageBody, messageTimeStamp, messageDate);

            em.merge(sms);

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
