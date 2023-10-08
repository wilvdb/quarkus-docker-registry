package dockerregistry.internal.digest;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Dependent
public class DigestConfiguration {

    @Produces
    @Named("sha256")
    public MessageDigest sha256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Produces
    @Named("sha512")
    public MessageDigest sha512MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
