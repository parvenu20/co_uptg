package com.pay.tg;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Test {


    public static void main(String[] args) throws Exception {
        ITesseract instance = new Tesseract();
        instance.setDatapath("E:\\Code\\common\\tg\\src\\main\\resources\\chidata");
        instance.setLanguage("chi_sim");
        File imageLocation = new File("E:\\Downloads\\file_0.jpg");
        String doOCR = instance.doOCR(imageLocation);
        System.out.println(doOCR);
    }

}
