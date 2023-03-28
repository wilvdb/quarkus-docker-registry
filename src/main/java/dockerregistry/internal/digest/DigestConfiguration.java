package dockerregistry.internal.digest;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Dependent
public class DigestConfiguration {

    @Produces
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
