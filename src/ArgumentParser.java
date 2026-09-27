import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgumentParser {

  private static final String USAGE = "Uso: DigestCalculator <Tipo_Digest> " + "<Caminho_ArqListaDigest> "
      + "<Caminho_da_Pasta_dos_Arquivos>";

  public ProgramArguments parse(String[] args) {

    if (args.length < 3) {
      throw new IllegalArgumentException("Quantidade de argumentos inválida.");
    }

    DigestType digestType = parseDigestType(args[0]);

    Path digestListPath = Paths.get(args[1]);
    Path filesDirectory = Paths.get(args[2]);

    validatePaths(digestListPath, filesDirectory);

    return new ProgramArguments(digestType, digestListPath, filesDirectory);
  }

  private DigestType parseDigestType(String value) {
    try {
      return DigestType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Tipo de digest inválido.");
    }
  }

  private void validatePaths(Path digestListPath, Path filesDirectory) {
    if (!Files.isRegularFile(digestListPath)) {
      throw new IllegalArgumentException("O caminho da lista de digests não corresponde a um arquivo válido.");
    }

    if (!Files.isDirectory(filesDirectory)) {
      throw new IllegalArgumentException("O caminho dos arquivos não corresponde a uma pasta válida.");
    }
  }

  public static String getUsage() {
    return USAGE;
  }
}