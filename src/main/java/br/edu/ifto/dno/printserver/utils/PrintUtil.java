package br.edu.ifto.dno.printserver.utils;

import br.edu.ifto.dno.printserver.pages.HomePage;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Component;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Component
public class PrintUtil {

    private DocFlavor myFormat;

    public PrintUtil(){
        myFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
    }

    public PDDocument getPages(PDDocument doc, String rangers) throws IOException {

        PDDocument documentoFinal = new PDDocument();

        if(rangers == null || rangers.trim().isEmpty()){
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

    public Sides getSides(String lado) {
        if (Sides.ONE_SIDED.toString().equalsIgnoreCase(lado)) return Sides.ONE_SIDED;
        if (Sides.DUPLEX.toString().equalsIgnoreCase(lado)) return Sides.DUPLEX;
        if (Sides.TWO_SIDED_SHORT_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_SHORT_EDGE;
        if (Sides.TWO_SIDED_LONG_EDGE.toString().equalsIgnoreCase(lado)) return Sides.TWO_SIDED_LONG_EDGE;
        return null;
    }

    private PDDocument getPages(PDDocument doc, int start, int end) throws IOException {
        PageExtractor pageExtractor = new PageExtractor(doc, start, end);
        PDDocument docReturn = new PDDocument();
        docReturn = pageExtractor.extract();
        return docReturn;
    }

    public ArrayList<String> listPrints(){
        ArrayList<String> listaImrpessoras = new ArrayList<String>();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(myFormat,null);
        for(PrintService ps:printServices){
            listaImrpessoras.add(ps.getName());
        }
        return listaImrpessoras;
    }

    public ArrayList<String> listSides(){
        ArrayList<String> lista = new ArrayList<String>();
        lista.add(Sides.ONE_SIDED.toString());
        lista.add(Sides.TWO_SIDED_LONG_EDGE.toString());
        lista.add(Sides.TWO_SIDED_SHORT_EDGE.toString());
        return lista;
    }

    public void enviarArquivoImpressao(FileInputStream documento, String impressora, int copias, Sides lados){
        try {
            Doc myDoc = new SimpleDoc(documento, myFormat, null);
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

            aset.add(new Copies(copias));
            aset.add(lados);

            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(myFormat,null);
            DocPrintJob job = null;

            for(PrintService ps:printServices){
                if(impressora.trim().equalsIgnoreCase(ps.getName().trim())){
                    job = ps.createPrintJob();
                    break;
                }
            }
            job.print(myDoc, aset);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void main(String args[]) throws FileNotFoundException, PrintException {
//        FileInputStream textStream = new FileInputStream("/home/tassio/Desenvolvimento/teste.txt");
        FileInputStream textStream = new FileInputStream("/home/tassio/Documentos/SOLICITAÇÃO DE DIARIAS E PASSAGENS_tassio.pdf");

        Doc myDoc = new SimpleDoc(textStream, myFormat, null);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        aset.add(new Copies(1));
        aset.add(Sides.ONE_SIDED);

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(myFormat,null);

        for(PrintService ps:printServices){
            System.out.println(ps.getName());
        }

//        System.out.println("Printing to default printer: " + printService.getName());

//        DocPrintJob job = printService.createPrintJob();

//        job.print(myDoc, aset);

    }
}