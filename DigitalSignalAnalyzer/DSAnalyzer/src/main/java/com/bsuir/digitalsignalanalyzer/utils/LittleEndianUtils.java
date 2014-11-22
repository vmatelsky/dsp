package com.bsuir.digitalsignalanalyzer.utils;


import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianUtils {
    public static float readFloat(DataInputStream is) throws IOException {
        byte[] floatRepresentation = new byte[4];
        is.read(floatRepresentation);

        ByteBuffer byteBuffer = ByteBuffer.wrap(floatRepresentation);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getFloat();
    }

    public static int readInt(DataInputStream is) throws IOException {
        byte[] intRepresentation = new byte[4];
        is.read(intRepresentation);

        ByteBuffer byteBuffer = ByteBuffer.wrap(intRepresentation);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getInt();
    }
}
