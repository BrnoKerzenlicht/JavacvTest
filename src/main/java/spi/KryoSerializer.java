package spi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoSerializer implements ObjectSerializer {

    @Override
    public byte[] serialize(Object obj) throws Exception {
        byte[] bytes;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //获取kryo对象
            Kryo kryo = new Kryo();
            Output output = new Output(outputStream);
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        }
        catch (Exception ex) {
            throw new Exception("kryo serialize error" + ex.getMessage());
        }
        finally {
            try {
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException e) {
            }
        }
        return bytes;
    }

    @Override
    public String getSchemeName() {
        return "kryoSerializer";
    }
}