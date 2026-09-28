// Julia Gomes Zibordi (2320934)
// Marcos Paulo Marinho Vieira (2320466)

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

  public boolean hasDigestForAnotherFile(String fileName, DigestType digestType, String digest) {
    for (Map.Entry<String, Map<DigestType, String>> entry : entries.entrySet()) {

      String otherFileName = entry.getKey();

      if (otherFileName.equals(fileName)) {
        continue;
      }

      String otherDigest = entry.getValue().get(digestType);

      if (digest.equalsIgnoreCase(otherDigest)) {
        return true;
      }
    }

    return false;
  }
}