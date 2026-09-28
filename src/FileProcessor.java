// Julia Gomes Zibordi (2320934)
// Marcos Paulo Marinho Vieira (2320466)

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {
  private final DigestService digestService;

  public FileProcessor(DigestService digestService) {
    this.digestService = digestService;
  }

  public Map<Path, String> process(Path directory, DigestType digestType) throws IOException {
    Map<Path, String> digests = new HashMap<>();

    List<Path> files;

    try (Stream<Path> stream = Files.list(directory)) {
      files = stream.filter(Files::isRegularFile).collect(Collectors.toList());
    }

    for (Path file : files) {
      String digest = digestService.calculate(file, digestType);
      digests.put(file, digest);
    }

    return digests;
  }
}