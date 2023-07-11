package com.github.aandrosov.tkinter.toolchain;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
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

    public static boolean compareHeader(InputStream inputStream, int[] header) throws IOException {
        for(int i = 0, readByte; i < header.length; i++) {
            if(((readByte = inputStream.read()) == -1) || (readByte != header[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPng(File file) throws IOException {
        try(InputStream inputStream = java.nio.file.Files.newInputStream(file.toPath())) {
            return compareHeader(inputStream, new int[]{0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a});
        }
    }

    public static boolean isJpeg(File file) throws IOException {
        try(InputStream inputStream = java.nio.file.Files.newInputStream(file.toPath())) {
            return compareHeader(inputStream, new int[]{0xff, 0xd8, 0xff});
        }
    }
}
