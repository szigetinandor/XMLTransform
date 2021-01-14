package merid.webshop.xmltransform;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) {
        String inputPath = "input.xml";
        String outputPath = "output.xml";

        try {
            XMLDocument inputDocument = new XMLDocument();
            inputDocument.read(inputPath);
            XMLDocument resultXMLDocument = inputDocument.createDocumentWithSelectedNodeValues("termekkategoria", "Kategória Leírás", false);
            resultXMLDocument.writeToFile(outputPath);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }

    }
}
