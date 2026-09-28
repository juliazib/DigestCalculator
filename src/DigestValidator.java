// Julia Gomes Zibordi (2320934)
// Marcos Paulo Marinho Vieira (2320466)

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DigestValidator {

  private final DigestCatalog catalog;

  public DigestValidator(DigestCatalog catalog) {
    this.catalog = catalog;
  }

  public List<DigestResult> validate(Map<Path, String> calculatedDigests, DigestType digestType) {
    List<DigestResult> results = new ArrayList<>();

    for (Map.Entry<Path, String> entry : calculatedDigests.entrySet()) {

      Path file = entry.getKey();
      String calculatedDigest = entry.getValue();
      String fileName = file.getFileName().toString();

      DigestStatus status;

      if (hasCollisionWithAnotherFile(file, calculatedDigest, calculatedDigests)
          || catalog.hasDigestForAnotherFile(fileName, digestType, calculatedDigest)) {

        status = DigestStatus.COLISION;

      } else {

        String expectedDigest = catalog.getDigest(fileName, digestType);

        if (expectedDigest == null) {
          status = DigestStatus.NOT_FOUND;

        } else if (calculatedDigest.equalsIgnoreCase(expectedDigest)) {
          status = DigestStatus.OK;

        } else {
          status = DigestStatus.NOT_OK;
        }
      }

      results.add(new DigestResult(fileName, digestType, calculatedDigest, status));
    }

    return results;
  }

  private boolean hasCollisionWithAnotherFile(Path file, String digest, Map<Path, String> calculatedDigests) {
    for (Path otherFile : calculatedDigests.keySet()) {

      if (otherFile.equals(file)) {
        continue;
      }

      String otherDigest = calculatedDigests.get(otherFile);

      if (digest.equalsIgnoreCase(otherDigest)) {
        return true;
      }
    }

    return false;
  }
}