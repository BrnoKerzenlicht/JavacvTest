package spi;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class JavaSerializer implements ObjectSerializer {

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream arrayOutputStream;
        try {
            arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(obj);
            objectOutput.flush();
            objectOutput.close();
        }
        catch (Exception e) {
            throw new Exception("JAVA serialize error " + e.getMessage());
        }
        return arrayOutputStream.toByteArray();
    }

    @Override
    public String getSchemeName() {
        return "javaSerializer";
    }
}