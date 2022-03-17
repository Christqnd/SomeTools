/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenprojectqr;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Graphics2D;
import java.awt.Image;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Deportivo Cuenca
 */
public class QRCodeReader {
    
 public String readQRCode(String base64Image) {
        String encodedContent = null;
        try {
            byte[] imageBytes = Base64.decode(base64Image);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            encodedContent = readQRCode(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedContent;
    }

    public String readQRCode(File qrCodeFile) {
        String encodedContent = null;
        int intento=1;
        int scale=0;
        do{
                try {
                    if (intento==1){
                    scale=Image.SCALE_SMOOTH;
                    }else if(intento==2){
                    scale=Image.SCALE_REPLICATE;
                    }else if(intento==3){
                    scale=Image.SCALE_AREA_AVERAGING;
                    }else if(intento==4){
                    scale=Image.SCALE_FAST;
                    }else{
                    scale=Image.SCALE_DEFAULT;
                    }
                Image image = ImageIO.read(qrCodeFile);
                Image newImage = image.getScaledInstance(500, 500, scale);
                BufferedImage bufferedImage = convertToBufferedImage(newImage);
        //File outputfile = new File("C:\\Proyect\\Docs\\CODIGOS DE CUENCA TODOS\\image.png");
        //ImageIO.write(bufferedImage, "png", outputfile);
                    encodedContent = readQRCode(bufferedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                  //  System.out.println("error readQRCode File:"+e.getMessage());
                    encodedContent= "FailScannCodeQR";
                }

            if(encodedContent.equals("FailScannCodeQR")){intento++;} else {intento=100;}
        }while(intento<=5 );

        return encodedContent;
    }

public static BufferedImage convertToBufferedImage(Image image)
{
    BufferedImage newImage = new BufferedImage(
        image.getWidth(null), image.getHeight(null),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
}

    public String readQRCode(BufferedImage bufferedImage) {
        String encodedContent = null;
        try {
            BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            MultiFormatReader multiFormatReader = new MultiFormatReader();

            Result result = multiFormatReader.decode(binaryBitmap);
            encodedContent = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            //System.out.println("error readQRCode:"+e.getMessage());
            return "FailScannCodeQR";
        }
        return encodedContent;
    }
}
