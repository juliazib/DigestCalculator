import java.util.HashMap;
import java.util.Map;

public class DigestCatalog {

  private final Map<String, Map<DigestType, String>> entries;

  public DigestCatalog() {
    entries = new HashMap<>();
  }

  public void addDigest(String fileName, DigestType digestType, String digest) {
    entries.computeIfAbsent(fileName, key -> new HashMap<>()).put(digestType, digest);
  }

  public String getDigest(String fileName, DigestType digestType) {
    Map<DigestType, String> fileDigests = entries.get(fileName);

    if (fileDigests == null) {
      return null;
    }

    return fileDigests.get(digestType);
  }
}