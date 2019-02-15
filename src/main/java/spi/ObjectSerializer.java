package spi;

public interface ObjectSerializer {
    byte[] serialize(Object obj) throws Exception;
    String getSchemeName();
}