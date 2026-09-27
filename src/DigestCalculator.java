import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DigestCalculator {

  public static void main(String[] args) {

    ArgumentParser parser = new ArgumentParser();

    try {
      ProgramArguments arguments = parser.parse(args);

      XmlCatalogReader catalogReader = new XmlCatalogReader();

      DigestService digestService = new DigestService();
      FileProcessor fileProcessor = new FileProcessor(digestService);

      Map<Path, String> digests = fileProcessor.process(arguments.getFilesDirectory(), arguments.getDigestType());

      DigestCatalog catalog = catalogReader.read(arguments.getDigestListPath());

      DigestValidator validator = new DigestValidator(catalog);

      List<DigestResult> results = validator.validate(digests, arguments.getDigestType());

      XmlCatalogWriter catalogWriter = new XmlCatalogWriter();

      catalogWriter.update(arguments.getDigestListPath(), results);

      for (DigestResult result : results) {
        System.out.println(result.getFileName() + " " + result.getDigestType() + " " + result.getDigestHex() + " ("
            + result.getStatus() + ")");
      }

    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      System.out.println(ArgumentParser.getUsage());

    } catch (IOException e) {
      System.out.println("Erro ao acessar os arquivos: " + e.getMessage());

    } catch (ParserConfigurationException e) {
      System.out.println("Erro ao configurar o leitor de XML.");

    } catch (SAXException e) {
      System.out.println("Erro ao interpretar o arquivo XML: " + e.getMessage());
    }
  }
}