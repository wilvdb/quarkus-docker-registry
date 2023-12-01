package dockerregistry.extension.storage;

import java.io.InputStream;

public interface Storage {
    long upload(String range, String uuid, InputStream inputStream);

    byte[] download(String name, String digest);

    void finishUpload(String uuid, String digest);

    long getLayerSize(String hash);
}
