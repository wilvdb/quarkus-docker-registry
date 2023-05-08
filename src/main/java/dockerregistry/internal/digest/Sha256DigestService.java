package dockerregistry.internal.digest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.security.MessageDigest;

@ApplicationScoped
@Named("sha256DigestService")
public class Sha256DigestService extends DigestService {

    private static final String ALGORITHM = "sha256";

    @Inject
    @Named("sha256")
    MessageDigest messageDigest;

    @Override
    protected byte[] digest(byte[] content) {
        return messageDigest.digest(content);
    }

    @Override
    protected String algorithm() {
        return ALGORITHM;
    }
}
