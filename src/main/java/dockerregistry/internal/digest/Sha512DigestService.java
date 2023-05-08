package dockerregistry.internal.digest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.security.MessageDigest;

@ApplicationScoped
@Named("sha512DigestService")
public class Sha512DigestService extends DigestService {

    private static final String ALGORITHM = "sha512";

    @Inject
    @Named("sha512")
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
