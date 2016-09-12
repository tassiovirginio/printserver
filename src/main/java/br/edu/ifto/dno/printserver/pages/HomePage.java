package br.edu.ifto.dno.printserver.pages;

import br.edu.ifto.dno.printserver.WicketApplication;
import br.edu.ifto.dno.printserver.business.ImpressaoBusiness;
import br.edu.ifto.dno.printserver.entities.Impressao;
import br.edu.ifto.dno.printserver.pages.base.Base;
import br.edu.ifto.dno.printserver.utils.PrintUtil;
import br.edu.ifto.dno.printserver.utils.LdapUtil;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HomePage extends Base {
    private static final long serialVersionUID = 1L;

    private FileUploadField fileUploadField;

    private String impressoraSelecionada;

    private String opcaoSides;

    private String login;

    private String senha;

    private Integer copias;

    private String paginas;

    @SpringBean
    private LdapUtil ldapUtil;

    @SpringBean
    private ImpressaoBusiness impressaoBusiness;

    @SpringBean
    private Boolean ldapLigado;

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
                    if(ldapLigado) {
                        loginOK = ldapUtil.login(login, senha);
                    }else{
                        loginOK = true;
                        login = "LDAP-OFF";
                    }
                } catch (Exception e) {
                    HomePage.this.info("Error: " + e.getLocalizedMessage());
                }


                if (loginOK) {
                    final List<FileUpload> uploads = fileUploadField.getFileUploads();
                    if (uploads != null) {
                        for (FileUpload upload : uploads) {
                            File newFile = new File(getUploadFolder(), upload.getClientFileName());

                            if (!newFile.getName().endsWith(".pdf")) {
                                HomePage.this.info("O ARQUIVO TEM QUE SER PDF !!");
                                break;
                            }

                            checkFileExists(newFile);
                            try {
                                newFile.createNewFile();
                                upload.writeTo(newFile);

                                PDDocument doc_ = PDDocument.load(newFile);
//                                int numeroPaginas = doc.getNumberOfPages();

                                PDDocument docFinal = getPages(doc_, paginas);
                                int numeroPaginas = docFinal.getNumberOfPages();


                                if (docFinal.getNumberOfPages() > 0) {

                                    HomePage.this.info("Usuario: " + login + " enviou impressão para : "
                                            + impressoraSelecionada
                                            + " - "
                                            + upload.getClientFileName()
                                            + " - Paginas do Documento: " + numeroPaginas
                                            + " - Paginas Total: " + numeroPaginas * copias
                                    );


                                    docFinal.save(newFile);

                                    FileInputStream fileInputStream = new FileInputStream(newFile);

                                    PrintUtil.enviarArquivoImpressao(fileInputStream, impressoraSelecionada, copias, getSides(opcaoSides));

                                    Impressao impressao = new Impressao();
                                    impressao.setData(new Date());
                                    impressao.setImpressora(impressoraSelecionada);
                                    impressao.setUsuario(login);
                                    impressao.setCopias(copias);
                                    impressao.setPaginasDocumento(numeroPaginas);
                                    impressao.setPaginasTotal(numeroPaginas * copias);
                                    impressao.setNomeArquivo(newFile.getName());
                                    String remoteAddress = ((WebClientInfo) Session.get().getClientInfo())
                                            .getProperties()
                                            .getRemoteAddress();
                                    impressao.setIpOrigem(remoteAddress);

                                    impressaoBusiness.save(impressao);
                                }else{
                                    HomePage.this.info("Erro");
                                }

                                docFinal.close();

                            } catch (Exception e) {
                                throw new IllegalStateException("Unable to write file", e);
                            }
                        }
                    }
                } else {
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

        List<String> impressoras = PrintUtil.listPrints();
        DropDownChoice ddcImpressoras = new DropDownChoice("listaimpressoras", new PropertyModel(this, "impressoraSelecionada"), impressoras);
        ddcImpressoras.setRequired(true);
        progressUploadForm.add(ddcImpressoras);

        List<String> listaLados = PrintUtil.listSides();
        DropDownChoice ddcLados = new DropDownChoice("listaLados", new PropertyModel(this, "opcaoSides"), listaLados);
        ddcLados.setRequired(true);
        progressUploadForm.add(ddcLados);

        progressUploadForm.add(new TextField<String>
                ("login", new PropertyModel<String>(this, "login"))
                .setRequired(true).setVisible(ldapLigado)
        );

        progressUploadForm.add(new PasswordTextField
                ("senha", new PropertyModel<String>(this, "senha"))
                .setRequired(true).setVisible(ldapLigado)
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

    private Sides getSides(String lado) {
        if (Sides.ONE_SIDED.toString().equalsIgnoreCase(lado)) return Sides.ONE_SIDED;
        if (Sides.DUPLEX.toString().equalsIgnoreCase(lado)) return Sides.DUPLEX;
        if (Sides.TWO_SIDED_SHORT_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_SHORT_EDGE;
        if (Sides.TWO_SIDED_LONG_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_LONG_EDGE;
        return null;
    }

    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }

    private PDDocument getPages(PDDocument doc, String rangers) {

        PDDocument documentoFinal = new PDDocument();

        if(rangers.trim().isEmpty()){
            return doc;
        }

        String[] lista = rangers.split(",");

        for (String x : lista) {
            if (x.contains("-")) {
                String[] y = x.split("-");

                PDDocument pdDocument2 = getPages(doc, Integer.parseInt(y[0]), Integer.parseInt(y[1]));

                for (int xs = 0; xs < pdDocument2.getNumberOfPages(); xs++) {
                    documentoFinal.addPage(pdDocument2.getPage(xs));
                }

            } else {
                int numeroPagina = Integer.parseInt(x);
                PDPage page = doc.getPage(numeroPagina - 1);
                documentoFinal.addPage(page);
            }
        }

        return documentoFinal;
    }

    private PDDocument getPages(PDDocument doc, int start, int end) {
        PageExtractor pageExtractor = new PageExtractor(doc, start, end);
        PDDocument docReturn = new PDDocument();
        try {
            docReturn = pageExtractor.extract();
        } catch (Exception e) {
            e.printStackTrace();
            HomePage.this.info("Ultrapassou o numero de pagians do documento.");
        }
        return docReturn;
    }

}
