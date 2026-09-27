import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class DigestCalculator {

  public static void main(String[] args) {

    ArgumentParser parser = new ArgumentParser();

    try {
      ProgramArguments arguments = parser.parse(args);

      DigestService digestService = new DigestService();
      FileProcessor fileProcessor = new FileProcessor(digestService);

      Map<Path, String> digests = fileProcessor.process(arguments.getFilesDirectory(), arguments.getDigestType());

      // print temporário pra testar os digests
      for (Map.Entry<Path, String> entry : digests.entrySet()) {
        System.out.println(
            entry.getKey().getFileName() + ": " + entry.getValue());
      }

    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      System.out.println(ArgumentParser.getUsage());

    } catch (IOException e) {
      System.out.println("Erro ao acessar os arquivos: " + e.getMessage());
    }
  }
}