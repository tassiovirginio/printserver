package br.edu.ifto.dno;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PrintPS {

    public static void main(String args[]) throws FileNotFoundException, PrintException {
//        FileInputStream textStream = new FileInputStream("/home/tassio/Desenvolvimento/teste.txt");
        FileInputStream textStream = new FileInputStream("/home/tassio/Documentos/SOLICITAÇÃO DE DIARIAS E PASSAGENS_tassio.pdf");

        DocFlavor myFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
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