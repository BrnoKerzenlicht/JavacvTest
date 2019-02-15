package spi;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Test {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3");
        ServiceLoader<ObjectSerializer> serviceLoader = ServiceLoader.load(ObjectSerializer.class);
        Iterator<ObjectSerializer> iterator = serviceLoader.iterator();
        iterator.forEachRemaining(serializer -> {
            try {
                System.out.println(serializer.getSchemeName() + ":" + Arrays.toString(serializer.serialize(list)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
