package br.edu.ifto.dno;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PrintPS {

    private static DocFlavor myFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;


    public ArrayList<String> listPrints(){
        ArrayList<String> lista = new ArrayList<String>();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(myFormat,null);
        for(PrintService ps:printServices){
            lista.add(ps.getName());
        }
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
                if(impressora.trim().equalsIgnoreCase(ps.getName())){
                    job = ps.createPrintJob();
                    System.out.println("Imprimindo em: " + ps.getName());
                    break;
                }
            }
            job.print(myDoc, aset);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws FileNotFoundException, PrintException {
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

        System.out.println("Printing to default printer: " + printService.getName());

//        DocPrintJob job = printService.createPrintJob();

//        job.print(myDoc, aset);

    }
}