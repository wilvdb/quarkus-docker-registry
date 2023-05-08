package dockerregistry.internal.digest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.security.MessageDigest;

@ApplicationScoped
public class DigestService {

    private static final String ALGORITHM = "sha256";

    @Inject
    MessageDigest messageDigest;

    public String getDigest(byte[] content) {
        return ALGORITHM + ":" + encodeHexString(messageDigest.digest(content));
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
}
