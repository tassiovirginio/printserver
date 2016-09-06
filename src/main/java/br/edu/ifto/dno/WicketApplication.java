package br.edu.ifto.dno;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.time.Duration;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class WicketApplication extends WebApplication {

    static Logger log = Logger.getLogger(WicketApplication.class.getName());

    private Folder uploadFolder = null;

    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {

        log.info("\n" +
                "********************************************************************\n"+
                "***                      Carregando o Sistema                    ***\n"+
                "********************************************************************");

        getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getDebugSettings().setAjaxDebugModeEnabled(false);
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        getMarkupSettings().setStripWicketTags(true);

        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
        uploadFolder.mkdirs();
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);

    }

    public Folder getUploadFolder() {
        return uploadFolder;
    }


}
