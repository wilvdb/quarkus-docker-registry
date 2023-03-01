package registry.manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.internal.error.exception.BlobUploadInvalidException;
import registry.internal.error.exception.ManifestBlobUnknownException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@ApplicationScoped
public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    private Path path;

    @PostConstruct
    protected void createRegistryDirectory() throws IOException {
        path = Files.createTempDirectory("tmpRegistry");
    }

    public boolean manifestExists(String name, String reference) {
        logger.debug("Check manifest for image {} and reference {}", name, reference);

        var hash = reference.split(":")[0];

        var tagPath = path.resolve(name + "." + reference + ".json").toFile();
        var hashPath = path.resolve(hash + ".json").toFile();
        return tagPath.exists() && hashPath.exists();

    }

    public String saveManifest(String name, String reference, InputStream input) {
        logger.debug("Save manifest for image {} and tag {}", name, reference);

        var tagPath = path.resolve(name + "." + reference + ".json");

        try (var outputChannel = Files.newByteChannel(tagPath, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            logger.debug("Write manifest to file {}", tagPath);
            outputChannel.write(ByteBuffer.wrap(input.readAllBytes()));
        } catch (IOException e) {
            throw new ManifestBlobUnknownException(e);
        }

        var hash = getSha256(name, reference);
        var hashPath = path.resolve(hash + ".json");
        try {
            Files.copy(tagPath, hashPath);
        } catch (IOException e) {
            throw new ManifestBlobUnknownException(e);
        }

        return hash;
    }

    public String getSha256(String name, String reference) {
        logger.debug("Get manifest digest for image {} with reference {}", name, reference);

        try {
            var messageInstance = MessageDigest.getInstance("SHA256");
            var tagPath = path.resolve(name + "." + reference + ".json");

            var content  = Files.readAllBytes(tagPath);

            return encodeHexString(messageInstance.digest(content));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public long getContentLength(String name, String reference) {
        logger.debug("Get manifest content size for image {} with reference {}", name, reference);

        try {
            return Files.size(path.resolve(name + "." + reference + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType(String name, String reference) {
        logger.debug("Get manifest media type for image {} with reference {}", name, reference);

        var manifestPath = path.resolve(name + "." + reference + ".json");
        try(var reader = Files.newBufferedReader(manifestPath); var parser = Json.createParser(reader)) {

            var mediaType = parser.getObject().get("mediaType").toString();
            logger.debug("Manifest media type is {}", mediaType);

            return mediaType;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}