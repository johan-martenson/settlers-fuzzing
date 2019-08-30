package org.appland.settlers.fuzzing;

import org.appland.settlers.model.Point;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ArgumentsHandler {

    private final InputStream inputStream;

    public ArgumentsHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    int getIntFor3Chars() throws SettlersModelDriverException, IOException {

        byte[] bytes = new byte[3];

        inputStream.read(bytes, 0, 3);

        String remaining = new String(bytes, "ISO-8859-1");

        if (remaining.length() < 3) {
            throw new SettlersModelDriverException();
        }

        int result = Integer.parseInt(remaining.substring(0, 3));

        return result;
    }

    int getIntFor2Chars() throws SettlersModelDriverException, IOException {

        byte[] bytes = new byte[3];

        inputStream.read(bytes, 0, 2);

        String remaining = new String(bytes, "ISO-8859-1");

        if (remaining.length() < 2) {
            throw new SettlersModelDriverException();
        }

        int result = Integer.parseInt(remaining.substring(0, 2));

        return result;
    }
    int getIntFor1Chars() throws SettlersModelDriverException, IOException {
        byte[] bytes = new byte[3];

        inputStream.read(bytes, 0, 1);

        String remaining = new String(bytes, "ISO-8859-1");

        if (remaining.length() < 1) {
            throw new SettlersModelDriverException();
        }

        int result = Integer.parseInt(remaining.substring(0, 1));

        return result;
    }

    public String getChar() throws IOException {
        byte[] bytes = new byte[1];

        inputStream.read(bytes, 0, 1);

        return new String(bytes, "ISO-8859-1");
    }

    public Point getPointForChars() throws IOException, SettlersModelDriverException {
        return new Point(
            getIntFor3Chars(),
            getIntFor3Chars()
        );
    }
}
