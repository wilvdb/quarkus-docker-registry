package dockerregistry.extension.storage;

import java.io.InputStream;

public interface Storage {
    long uploadLayer(String range, String uuid, InputStream inputStream);

    byte[] getLayer(String name, String digest);

    void finishUpload(String uuid, String digest);

    long getLayerSize(String hash);
}
