package com.github.aandrosov.tkinter.toolchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Streams {

    public static byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for(int readByte; (readByte = inputStream.read()) != -1; byteArrayOutputStream.write(readByte));
        return byteArrayOutputStream.toByteArray();
    }
}
