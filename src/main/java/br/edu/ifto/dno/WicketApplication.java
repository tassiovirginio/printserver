package br.edu.ifto.dno;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.file.Folder;

public class WicketApplication extends WebApplication {

    private Folder uploadFolder = null;

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {
        super.init();

        getResourceSettings().setThrowExceptionOnMissingResource(false);

        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
        uploadFolder.mkdirs();

        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
    }


    public Folder getUploadFolder() {
        return uploadFolder;
    }
}
