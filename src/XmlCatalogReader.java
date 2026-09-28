// Julia Gomes Zibordi (2320934)
// Marcos Paulo Marinho Vieira (2320466)

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlCatalogReader {

  public DigestCatalog read(Path xmlFile) throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(xmlFile.toFile());
    Element root = document.getDocumentElement();

    if (!"CATALOG".equals(root.getTagName())) {
      throw new IllegalArgumentException("O elemento raiz do XML deve ser CATALOG.");
    }

    return createCatalog(root);
  }

  private DigestCatalog createCatalog(Element root) {
    DigestCatalog catalog = new DigestCatalog();

    NodeList fileEntries = root.getElementsByTagName("FILE_ENTRY");

    for (int i = 0; i < fileEntries.getLength(); i++) {
      Element fileEntry = (Element) fileEntries.item(i);

      String fileName = fileEntry.getElementsByTagName("FILE_NAME").item(0).getTextContent().trim();

      NodeList digestEntries = fileEntry.getElementsByTagName("DIGEST_ENTRY");

      for (int j = 0; j < digestEntries.getLength(); j++) {
        Element digestEntry = (Element) digestEntries.item(j);

        String digestTypeText = digestEntry.getElementsByTagName("DIGEST_TYPE").item(0).getTextContent().trim();

        String digest = digestEntry.getElementsByTagName("DIGEST_HEX").item(0).getTextContent().trim();

        DigestType digestType;

        try {
          digestType = DigestType.valueOf(digestTypeText.toUpperCase());

        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Tipo de digest inválido no XML: " + digestTypeText, e);
        }

        catalog.addDigest(fileName, digestType, digest);
      }
    }

    return catalog;
  }
}