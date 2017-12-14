package sub.adw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Xpath {

	private XPath xpath;
	private Document doc;

	public Xpath(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(IOUtils.toInputStream(xml, "UTF-8"));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		xpath = xPathfactory.newXPath();
	}

	public String getString(String xpathExpression) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpression);
		String result = (String) expr.evaluate(doc, XPathConstants.STRING);
		return result;
	}

	public List<String> getList(String xpathExpression) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathExpression);
		NodeList resultNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < resultNodes.getLength(); i++) {
			resultList.add(resultNodes.item(i).getTextContent());
		}
		return resultList;
	}
}
