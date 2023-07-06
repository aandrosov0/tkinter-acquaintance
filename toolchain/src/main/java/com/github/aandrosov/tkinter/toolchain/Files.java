package com.github.aandrosov.tkinter.toolchain;

import javax.xml.ws.spi.http.HttpExchange;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public class Files {

    public static File getFileOrMake(Path path) throws IOException {
        try {
            java.nio.file.Files.createFile(path);
        } catch(FileAlreadyExistsException ignored) {}

        return path.toFile();
    }

    public static String getFilePropertyOrTerminate(File file, String propertyName) {
        Properties properties = new Properties();

        try(InputStream inputStream = java.nio.file.Files.newInputStream(file.toPath())) {
            properties.load(inputStream);
            String value = (String) properties.get(propertyName);

            if(value == null) {
                System.err.println("You must specify \"" + propertyName + "\" in " + file.getAbsolutePath());
                System.exit(-1);
            }

            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compareHeader(InputStream inputStream, byte[] header) throws IOException {
        byte[] bytes = new byte[header.length];

        if(inputStream.read(bytes) == -1) {
            return false;
        }

        return Arrays.equals(bytes, header);
    }

    public static boolean isPng(InputStream inputStream) throws IOException {
        return compareHeader(inputStream, new byte[]{(byte)137, 80, 78, 71, 13, 10, 26, 10});
    }

    public static boolean isJpeg(InputStream inputStream) throws IOException {
        return compareHeader(inputStream, new byte[]{(byte)255, (byte)216, (byte)255});
    }
}
