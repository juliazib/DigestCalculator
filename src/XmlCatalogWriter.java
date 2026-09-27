import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlCatalogWriter {

  public void update(Path xmlFile, List<DigestResult> results)
      throws IOException, ParserConfigurationException, SAXException {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    DocumentBuilder builder = factory.newDocumentBuilder();

    Document document = builder.parse(xmlFile.toFile());

    Element root = document.getDocumentElement();

    for (DigestResult result : results) {

      if (result.getStatus() != DigestStatus.NOT_FOUND) {
        continue;
      }

      addDigestEntry(document, root, result);
    }

    try {
      writeDocument(document, xmlFile);
    } catch (TransformerException e) {
      throw new IOException("Erro ao escrever o catálogo XML.", e);
    }
  }

  private void addDigestEntry(Document document, Element root, DigestResult result) {
    Element fileEntry = findFileEntry(root, result.getFileName());

    if (fileEntry == null) {

      fileEntry = document.createElement("FILE_ENTRY");

      Element fileName = document.createElement("FILE_NAME");

      fileName.setTextContent(result.getFileName());

      fileEntry.appendChild(fileName);
      root.appendChild(fileEntry);
    }

    Element digestEntry = createDigestEntry(document, result);

    fileEntry.appendChild(digestEntry);
  }

  private void writeDocument(Document document, Path xmlFile) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = transformerFactory.newTransformer();

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    transformer.transform(new DOMSource(document), new StreamResult(xmlFile.toFile()));
  }

  private Element findFileEntry(Element root, String fileName) {
    NodeList fileEntries = root.getElementsByTagName("FILE_ENTRY");

    for (int i = 0; i < fileEntries.getLength(); i++) {

      Element fileEntry = (Element) fileEntries.item(i);

      Element fileNameElement = (Element) fileEntry.getElementsByTagName("FILE_NAME").item(0);

      if (fileName.equals(fileNameElement.getTextContent().trim())) {
        return fileEntry;
      }
    }

    return null;
  }

  private Element createDigestEntry(Document document, DigestResult result) {

    Element digestEntry = document.createElement("DIGEST_ENTRY");

    Element digestType = document.createElement("DIGEST_TYPE");

    digestType.setTextContent(result.getDigestType().name());

    Element digestHex = document.createElement("DIGEST_HEX");

    digestHex.setTextContent(result.getDigestHex());

    digestEntry.appendChild(digestType);
    digestEntry.appendChild(digestHex);

    return digestEntry;
  }
}