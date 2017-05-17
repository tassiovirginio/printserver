package br.edu.ifto.dno.printserver;

import br.edu.ifto.dno.printserver.business.ImpressoraBusiness;
import br.edu.ifto.dno.printserver.pages.*;
import de.spqrinfo.cups4j.CupsPrinter;
import de.spqrinfo.cups4j.PrintJob;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Logger;

@Component
public class WicketApplication extends WebApplication {

    static Logger log = Logger.getLogger(WicketApplication.class.getName());

    private Folder uploadFolder = null;

    @Autowired
    private ImpressoraBusiness impressoraBusiness;

    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {

        log.info("\n" +
                "********************************************************************\n" +
                "***                      Carregando o Sistema                    ***\n" +
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

        mountPage("/imprimir/", HomePage.class);
        mountPage("/totalporip/", TotalPorIP.class);
        mountPage("/totalporusuario/", TotalPorUsuario.class);
        mountPage("/ultimasimpressoes/", UltimasImpressoes.class);

        mountPage("/impressoras/", Impressoras.class);

        servidor(4631);


    }

    private void servidor(int porta) {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Escutando na Porta: " + porta);
                try {
                    ServerSocket serverSocket = new ServerSocket(porta);
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

//                    try {
//                        CupsPrinter printer = new CupsPrinter(new URL("http://127.0.0.1:631/printers/PDF"),
//                                "PDFTeste2",false);
//                        PrintJob printJob = (new PrintJob.Builder(inputStream)).userName("Tassio").jobName("Teste2").copies(1).duplex(false).build();
//                        printer.print(printJob);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }

                    while (true) {
                        String cominginText = "";
                        cominginText = in.readLine();
                        System.out.println(cominginText);
                    }

//                    FileOutputStream outputStream =
//                            new FileOutputStream(new File("teste.pdf"));
//
//                    int read = 0;
//                    byte[] bytes = new byte[1024];
//
//                    while ((read = inputStream.read(bytes)) != -1) {
//                        outputStream.write(bytes, 0, read);
//                    }
//                    System.out.println("Arquivo gerado");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }


    public Folder getUploadFolder() {
        return uploadFolder;
    }


}
