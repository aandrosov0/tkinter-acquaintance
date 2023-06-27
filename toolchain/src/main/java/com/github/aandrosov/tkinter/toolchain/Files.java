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
}
