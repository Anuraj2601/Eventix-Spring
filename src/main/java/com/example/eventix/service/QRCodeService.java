package com.example.eventix.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

@Service
public class QRCodeService {

    // Directory to save QR codes
    @Value("${qr.code.directory}")
    private String qrCodeDirectory;

    // Base URL for accessing QR codes
    @Value("${qr.code.base.url}")
    private String qrCodeBaseUrl;

    // Generate QR Code and save it as an image file
    public String generateQRCode(String content, int width, int height, String fileName) throws Exception {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Generate QR code
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // Convert the BitMatrix to a BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        // Define the directory and file where the QR code image will be saved
        File outputFile = new File("src/main/resources/static/qr-codes/" + fileName + ".png");

        // Create directories if they do not exist
        outputFile.getParentFile().mkdirs();

        // Save the QR code as an image file
        ImageIO.write(image, "PNG", outputFile);

        // Return the URL for accessing the QR code
        return "http://localhost:8080/qr-codes/" + fileName + ".png";
    }

}
