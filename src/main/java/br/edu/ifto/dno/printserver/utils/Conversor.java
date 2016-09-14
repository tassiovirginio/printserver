package br.edu.ifto.dno.printserver.utils;

import org.docx4j.Docx4J;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by tassio on 14/09/16.
 */
public class Conversor {

    public File converterDOCtoPDF(File fileDOCX) throws Exception{

        File fileout = new File("fileout.pdf");

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fileDOCX);

        OutputStream os = new java.io.FileOutputStream(fileout);

//        if (!Docx4J.pdfViaFO()) {
            Docx4J.toPDF(wordMLPackage, os);
//        }

        return fileout;

    }
}
