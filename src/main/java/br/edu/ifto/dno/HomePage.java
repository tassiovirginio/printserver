package br.edu.ifto.dno;

import br.edu.ifto.dno.entities.Impressao;
import br.edu.ifto.dno.entities.ImpressaoBusiness;
import br.edu.ifto.dno.ldap.PersonRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.Application;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    private FileUploadField fileUploadField;

    private String impressoraSelecionada;

    private String opcaoSides;

    private String login;

    private String senha;

    private Integer copias;

    private String paginas;

    @SpringBean
    private PersonRepo personRepo;

    @SpringBean
    private ImpressaoBusiness impressaoBusiness;

    private Folder getUploadFolder() {
        return ((WicketApplication) Application.get()).getUploadFolder();
    }

    public HomePage(final PageParameters parameters) {
        super(parameters);

        copias = 1;

        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
        add(uploadFeedback);

        final Form<Void> progressUploadForm = new Form<Void>("formUpload") {
            @Override
            protected void onSubmit() {

                Boolean loginOK = false;

                try {
                    loginOK = personRepo.login(login,senha);
                }catch (Exception e){
                    HomePage.this.info("Error: " + e.getLocalizedMessage());
                }


                if(loginOK) {
                    final List<FileUpload> uploads = fileUploadField.getFileUploads();
                    if (uploads != null) {
                        for (FileUpload upload : uploads) {
                            File newFile = new File(getUploadFolder(), upload.getClientFileName());

                            if(!newFile.getName().endsWith(".pdf")){
                                HomePage.this.info("O ARQUIVO TEM QUE SER PDF !!");
                                break;
                            }

                            checkFileExists(newFile);
                            try {
                                newFile.createNewFile();
                                upload.writeTo(newFile);

                                PDDocument doc = PDDocument.load(newFile);
                                int numeroPaginas = doc.getNumberOfPages();

                                //paginas ex: 1-10

                                HomePage.this.info("Usuario: " + login + " enviou impressão para : "
                                        + impressoraSelecionada
                                        + " - "
                                        + upload.getClientFileName()
                                        + " - Paginas do Documento: " + numeroPaginas
                                        + " - Paginas Total: " + numeroPaginas * copias
                                );


                                FileInputStream fileInputStream = new FileInputStream(newFile);
                                Util.enviarArquivoImpressao(fileInputStream, impressoraSelecionada, copias, getSides(opcaoSides));

                                Impressao impressao = new Impressao();
                                impressao.setData(new Date());
                                impressao.setImpressora(impressoraSelecionada);
                                impressao.setUsuario(login);
                                impressao.setCopias(copias);
                                impressao.setPaginasDocumento(numeroPaginas);
                                impressao.setPaginasTotal(numeroPaginas * copias);
                                impressao.setNomeArquivo(newFile.getName());

                                impressaoBusiness.save(impressao);

                            } catch (Exception e) {
                                throw new IllegalStateException("Unable to write file", e);
                            }
                        }
                    }
                }else{
                    HomePage.this.info("Login Incorreto!!!");
                }
            }
        };

        progressUploadForm.setMultiPart(true);
        progressUploadForm.add(fileUploadField = new FileUploadField("fileInput"));
        progressUploadForm.setMaxSize(Bytes.kilobytes(10000));
        progressUploadForm.setFileMaxSize(Bytes.kilobytes(10000));

        progressUploadForm.add(new UploadProgressBar("progress", progressUploadForm, fileUploadField));
        add(progressUploadForm);

        List<String> impressoras = Util.listPrints();
        DropDownChoice ddcImpressoras = new DropDownChoice("listaimpressoras", new PropertyModel(this, "impressoraSelecionada"), impressoras);
        ddcImpressoras.setRequired(true);
        progressUploadForm.add(ddcImpressoras);

        List<String> listaLados = Util.listSides();
        DropDownChoice ddcLados = new DropDownChoice("listaLados", new PropertyModel(this, "opcaoSides"), listaLados);
        ddcLados.setRequired(true);
        progressUploadForm.add(ddcLados);

        progressUploadForm.add(new TextField<String>
                ("login", new PropertyModel<String>(this, "login"))
                .setRequired(true)
        );

        progressUploadForm.add(new PasswordTextField
                ("senha", new PropertyModel<String>(this, "senha"))
                .setRequired(true)
        );

        progressUploadForm.add(new TextField
                ("copias", new PropertyModel<String>(this, "copias"))
                .setRequired(true)
        );

        progressUploadForm.add(new TextField
                ("paginas", new PropertyModel<String>(this, "paginas"))
                .setRequired(true)
        );


    }

    private Sides getSides(String lado){
        if(Sides.ONE_SIDED.toString().equalsIgnoreCase(lado)) return Sides.ONE_SIDED;
        if(Sides.DUPLEX.toString().equalsIgnoreCase(lado)) return Sides.DUPLEX;
        if(Sides.TWO_SIDED_SHORT_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_SHORT_EDGE;
        if(Sides.TWO_SIDED_LONG_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_LONG_EDGE;
        return null;
    }

    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }
}
