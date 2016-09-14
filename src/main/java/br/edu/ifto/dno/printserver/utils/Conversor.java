package br.edu.ifto.dno.printserver.utils;

import fr.opensagres.xdocreport.converter.*;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import org.docx4j.Docx4J;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.odftoolkit.odfdom.type.OdfDataType;
import org.odftoolkit.odfdom.converter.pdf.PdfConverter;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tassio on 14/09/16.
 */
public class Conversor {

    public File converterDOCtoPDF(InputStream fileDOCX) throws Exception{

        String regex = null;
        // Windows:
//        regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
        regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
        // Mac
        // regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
        PhysicalFonts.setRegex(regex);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fileDOCX);

        File file = new File("fileout.pdf");

        OutputStream os = new java.io.FileOutputStream(file);

//        if (!Docx4J.pdfViaFO()) {
            Docx4J.toPDF(wordMLPackage, os);
//        }
        return file;

    }

    public File converterODTtoPDF(InputStream inputStreamODT) throws Exception {

        Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);
        IConverter converter = ConverterRegistry.getRegistry().getConverter(options);
        File file = new File("fileout.pdf");
        OutputStream out = new java.io.FileOutputStream(file);
        converter.convert(inputStreamODT, out, options);


//        OdfTextDocument odfDocument = OdfTextDocument.loadDocument(inputStreamODT);
//        PdfOptions options = PdfOptions.create();
//        File file = new File("fileout.pdf");
//        OutputStream os = new java.io.FileOutputStream(file);
//        PdfConverter.getInstance().convert(odfDocument, os, options);



        return file;
    }
}
