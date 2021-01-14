package merid.webshop.xmltransform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLDocument {

	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder documentBuilder;

	static {
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	private Document document;

	public Document getDocument() {
		return document;
	}

	public XMLDocument() {
		this.document = documentBuilder.newDocument();
	}

	public void read(String path) throws ParserConfigurationException, IOException, SAXException {
		File inputFile = new File(path);
		document = documentBuilder.parse(inputFile);
		document.getDocumentElement().normalize();
	}

	public void addNode(Node node) {
		document.getDocumentElement().appendChild(node);
	}

	public XMLDocument createDocumentWithSelectedNodeValues(String nodeName, String nodeValue, boolean exactMatch) {
		XMLDocument resultXMLDocument = new XMLDocument();
		resultXMLDocument.createRootElement(this.document);
		NodeList nodes = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			NodeList childNodes = node.getChildNodes();
			for(int j = 0; j < childNodes.getLength(); j++) {
				Node childNode = childNodes.item(j);
				if (childNode.getNodeName().equals(nodeName) &&
						(exactMatch && childNode.getTextContent().equals(nodeValue) || !exactMatch && childNode.getTextContent().contains(nodeValue))) {
					Node nodeToAppend = this.document.importNode(node, true);
					resultXMLDocument.document.getDocumentElement().appendChild(resultXMLDocument.document.adoptNode(nodeToAppend.cloneNode(true)));
				}
			}
		}
		return resultXMLDocument;
	}

	private void createRootElement(Document document) {
		Element sourceRootElement = this.document.createElement(document.getDocumentElement().getNodeName());
		this.document.appendChild(sourceRootElement);
	}

	public void writeToFile(String path) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(this.document);
		StreamResult result = new StreamResult(new File(path));
		transformer.transform(source, result);
	}

}
