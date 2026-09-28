import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestService {

  public String calculate(Path file, DigestType digestType) throws IOException {
    MessageDigest messageDigest;

    try {
      messageDigest = MessageDigest.getInstance(
          getAlgorithmName(digestType));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(
          "Algoritmo de digest não suportado " + digestType, e);
    }

    try (InputStream input = Files.newInputStream(file)) {

      byte[] buffer = new byte[8192];
      int bytesRead;

      while ((bytesRead = input.read(buffer)) != -1) {
        messageDigest.update(buffer, 0, bytesRead);
      }
    }

    return toHex(messageDigest.digest());
  }

  private String getAlgorithmName(DigestType digestType) {
    switch (digestType) {
      case MD5:
        return "MD5";
      case SHA1:
        return "SHA-1";
      case SHA256:
        return "SHA-256";
      case SHA512:
        return "SHA-512";
      default:
        throw new IllegalStateException();
    }
  }

  private String toHex(byte[] digest) {
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < digest.length; i++) {
      String hex = Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1);
      buf.append((hex.length() < 2 ? "0" : "") + hex);
    }

    return buf.toString();
  }
}