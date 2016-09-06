package br.edu.ifto.dno;

import org.apache.wicket.Application;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.util.List;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    private FileUploadField fileUploadField;

    private String impressoraSelecionada;

    private Folder getUploadFolder() {
        return ((WicketApplication) Application.get()).getUploadFolder();
    }

    public HomePage(final PageParameters parameters) {
        super(parameters);

        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
        add(uploadFeedback);

        final Form<Void> progressUploadForm = new Form<Void>("formUpload") {
            @Override
            protected void onSubmit() {
                final List<FileUpload> uploads = fileUploadField.getFileUploads();
                if (uploads != null) {
                    for (FileUpload upload : uploads) {
                        File newFile = new File(getUploadFolder(), upload.getClientFileName());

                        checkFileExists(newFile);
                        try {
                            newFile.createNewFile();
                            upload.writeTo(newFile);
                            HomePage.this.info("saved file: " + upload.getClientFileName());

                            FileInputStream fileInputStream = new FileInputStream(newFile);

                            Util.enviarArquivoImpressao(fileInputStream,impressoraSelecionada,1, Sides.ONE_SIDED);

                        } catch (Exception e) {
                            throw new IllegalStateException("Unable to write file", e);
                        }
                    }
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

    }

    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }
}
