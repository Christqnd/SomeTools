/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenprojectqr;

import java.io.File;

/**
 *
 * @author Deportivo Cuenca
 */
public class ReadQRCodeFromFile {

    public static void main(String... args) {

        final File folder = new File("C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS");
        for (final File fileEntry : folder.listFiles()) {
            try{
                //System.out.println(fileEntry.getPath());    
                String qrCodeFileName = fileEntry.getPath();
                File file = new File(qrCodeFileName);

                QRCodeReader qrCodeReader = new QRCodeReader();
                String encodedContent = qrCodeReader.readQRCode(file);

                System.out.println(fileEntry.getPath() +"\t"+ encodedContent);
                }
                catch(Exception e){
                }
            }
       
    }

   
}
