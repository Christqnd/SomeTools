/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenprojectqr;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Deportivo Cuenca
 */
public class ReadQRCodeFromFile {

    public static void main(String... args) {

        logInit();

         File folder = new File("C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS");

        

        PrintWriter writer=null;
        try {
            File directory = new File(folder.getPath()+ "\\ResultFilesMap\\");
                if (!directory.exists()) {
                    directory.mkdir();
                }
            writer = new PrintWriter(folder.getPath()+"\\ResultFilesMap\\result.txt", "UTF-8");
                
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadQRCodeFromFile.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadQRCodeFromFile.class.getName()).log(Level.SEVERE, null, ex);
             return;
        }


        for (final File fileEntry : folder.listFiles()) {
            try {
                //System.out.println(fileEntry.getPath());    
                String qrCodeFileName = fileEntry.getPath();
                File file = new File(qrCodeFileName);
                String codigo ="";

                QRCodeReader qrCodeReader = new QRCodeReader();
                if (!fileEntry.getName().equals("FolderFailScannCodeQR") || !fileEntry.getName().equals("ResultFilesMap")) {
                    String encodedContent = qrCodeReader.readQRCode(file);
                    if (encodedContent.equals("FailScannCodeQR")) {
                        File directory = new File(fileEntry.getParent() + "\\FolderFailScannCodeQR\\");
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                        FileUtils.copyFile(new File(fileEntry.getPath()), new File(fileEntry.getParent() + "\\FolderFailScannCodeQR\\F_" + fileEntry.getName()));
                    }else{
                        codigo=getCodigoAbono(encodedContent);
                    }

                    System.out.println(fileEntry.getPath() + "\t" + encodedContent + "\t"+codigo);
                    logger.info(fileEntry.getPath() + "\t" + encodedContent + "\t"+codigo);
                    writer.println(fileEntry.getPath() + "\t" + encodedContent + "\t"+codigo);



                }
            } catch (Exception e) {
            }
        }













            writer.close();
        /*
 
String inputImagePath = "C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS\\1576.png";

               try{
     
                String qrCodeFileName = inputImagePath;
                File file = new File(inputImagePath);

                QRCodeReader qrCodeReader = new QRCodeReader();
                String encodedContent = qrCodeReader.readQRCode(file);

                System.out.println(inputImagePath +"\t"+ encodedContent);
                }
                catch(Exception e){
                System.out.println("e:"+e.getMessage());
                }

   inputImagePath = "C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS\\2563.png";

               try{
     
                String qrCodeFileName = inputImagePath;
                File file = new File(inputImagePath);

                QRCodeReader qrCodeReader = new QRCodeReader();
                String encodedContent = qrCodeReader.readQRCode(file);

                System.out.println(inputImagePath +"\t"+ encodedContent);
                }
                catch(Exception e){
                System.out.println("e:"+e.getMessage());
                }

   inputImagePath = "C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS\\3216.png";

               try{
     
                String qrCodeFileName = inputImagePath;
                File file = new File(inputImagePath);

                QRCodeReader qrCodeReader = new QRCodeReader();
                String encodedContent = qrCodeReader.readQRCode(file);

                System.out.println(inputImagePath +"\t"+ encodedContent);
                }
                catch(Exception e){
                System.out.println("e:"+e.getMessage());
                }

   inputImagePath = "C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\CODIGOS DE CUENCA TODOS\\3889.png";

               try{
     
                String qrCodeFileName = inputImagePath;
                File file = new File(inputImagePath);

                QRCodeReader qrCodeReader = new QRCodeReader();
                String encodedContent = qrCodeReader.readQRCode(file);

                System.out.println(inputImagePath +"\t"+ encodedContent);
                }
                catch(Exception e){
                System.out.println("e:"+e.getMessage());
                }

         */
    }

    public static String getCodigoAbono(String resultqrurl) {
        String codigoAbono = "";
        String htmlSource = "";
        String resultqr = resultqrurl;
        try {
            URL url = new URL(resultqr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            htmlSource = content.toString();
            System.out.println("e:SOURCE - " + htmlSource);

            String bodyString = recuperaBodyContenido(htmlSource);

            System.out.println("******* " + bodyString.toString());


            //codigoAbono = bodyString.replaceAll("SOCIO DEPORTIVO CUENCA 2022 #", "");
            codigoAbono = bodyString.substring(bodyString.indexOf("#")+1, bodyString.length());
            Reader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
                System.out.println("############# Error Status : " + status);
            } else {
                System.out.println("############# Info Status : " + status);
            }

            con.disconnect();

        } catch (Exception e) {
            System.out.println("############# " + e.getMessage());
        }
        return codigoAbono;
    }

    private static String recuperaBodyContenido(String html) {
        String inicio = "<p class=\"item center\">";
        String fin = "</p>";
        String retorno = "";

        try {
            retorno = html.substring(
                    html.lastIndexOf(inicio) + inicio.length(),
                    html.lastIndexOf(fin)
            );
        } catch (Exception e) {
            e.printStackTrace();
            retorno = "";
        }

        return retorno;
    }



        
    private static Logger logger = Logger.getLogger("MyLog");  
    public static void logInit() {  


        FileHandler fh;  

        try {  

            // This block configure the logger with handler and formatter  
            fh = new FileHandler(Paths.get("").toAbsolutePath().toString() +"/CDC_"+ (new SimpleDateFormat("yyyyMMdd")).format(new Date()) +"_log.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

           // logger.info(cadena);  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

    }
}
