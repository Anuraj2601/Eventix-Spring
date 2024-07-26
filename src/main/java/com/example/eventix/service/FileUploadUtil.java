<<<<<<< HEAD
package com.example.eventix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class FileUploadUtil {


    private final String uploadDirectory;

    @Autowired
    public FileUploadUtil(@Value("${file.upload.directory}") String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public void saveFile(String fileName, byte[] fileContent) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory, fileName);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, fileContent);
    }

    public void saveDefaultImage(String fileName, byte[] fileContent) throws IOException {
        saveFile(fileName, fileContent);
    }

    public void deleteFile(String fileName) throws IOException{
        Path filePath = Paths.get(uploadDirectory).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch(Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch(Exception ignored) {
        }
        return outputStream.toByteArray();
    }


}
=======
package com.example.eventix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class FileUploadUtil {


    private final String uploadDirectory;

    @Autowired
    public FileUploadUtil(@Value("${file.upload.directory}") String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public void saveFile(String fileName, byte[] fileContent) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory, fileName);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, fileContent);
    }

    public void saveDefaultImage(String fileName, byte[] fileContent) throws IOException {
        saveFile(fileName, fileContent);
    }

    public void deleteFile(String fileName) throws IOException{
        Path filePath = Paths.get(uploadDirectory).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch(Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch(Exception ignored) {
        }
        return outputStream.toByteArray();
    }


}
>>>>>>> 4f912b739ea1ddc3a83484fa6247a275f99e1b3b
