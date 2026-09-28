// Julia Gomes Zibordi (2320934)
// Marcos Paulo Marinho Vieira (2320466)

import java.nio.file.Path;

public class ProgramArguments {

  private final DigestType digestType;
  private final Path digestListPath;
  private final Path filesDirectory;

  public ProgramArguments(DigestType digestType, Path digestListPath, Path filesDirectory) {
    this.digestType = digestType;
    this.digestListPath = digestListPath;
    this.filesDirectory = filesDirectory;
  }

  public DigestType getDigestType() {
    return digestType;
  }

  public Path getDigestListPath() {
    return digestListPath;
  }

  public Path getFilesDirectory() {
    return filesDirectory;
  }
}