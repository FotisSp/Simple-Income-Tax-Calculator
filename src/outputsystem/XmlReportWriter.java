package outputsystem;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import dataeditor.TaxpayerInfo;

public class XmlReportWriter implements OutputFile {
	private Document doc;
	private Element rootElement;

	public void printReport(TaxpayerInfo taxpayer,
			ArrayList<String> selectedTaxes) throws IOException {
		try {
			writeElements(taxpayer, selectedTaxes);
			JOptionPane.showMessageDialog(null, "File Saved !", "File Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ParserConfigurationException | TransformerException e) {
			System.out.println("Parser Configuration\\Transformer Exception!");
			e.printStackTrace();
		}
	}

	private void writeElements(TaxpayerInfo taxpayer,
			ArrayList<String> selectedTaxes)
			throws ParserConfigurationException,
			TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		String outputFileName = taxpayer.getAfm() + "_LOG.xml";
		writeRootElement();
		writeNameElement(taxpayer);
		writeAfmElement(taxpayer);
		writeIncomeElement(taxpayer);
		writeTaxOptions(selectedTaxes);

		writeToFile(outputFileName);
	}

	private void writeToFile(String outputFileName)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(outputFileName));

		transformer.transform(source, result);
	}

	private void writeTaxOptions(ArrayList<String> selectedTaxes) {
		for (int i = 0; i < selectedTaxes.size(); i++) {
			String[] splitSelectedTaxesElement = getXmlTag(selectedTaxes, i)
					.split(":");
			splitSelectedTaxesElement[0] = splitSelectedTaxesElement[0]
					.replaceAll("\\s+", "");
			Element taxOptions = doc
					.createElement(splitSelectedTaxesElement[0]);
			taxOptions.appendChild(doc
					.createTextNode(splitSelectedTaxesElement[1].trim()));
			rootElement.appendChild(taxOptions);
		}
	}

	private String getXmlTag(ArrayList<String> selectedTaxes, int i) {
		return selectedTaxes.get(i).trim();
	}

	private void writeIncomeElement(TaxpayerInfo taxpayer) {
		Element income = doc.createElement("Income");
		income.appendChild(doc.createTextNode(taxpayer.getIncome().toString()));
		rootElement.appendChild(income);
	}

	private void writeAfmElement(TaxpayerInfo taxpayer) {
		Element afm = doc.createElement("Afm");
		afm.appendChild(doc.createTextNode(taxpayer.getAfm().toString()));
		rootElement.appendChild(afm);
	}

	private void writeNameElement(TaxpayerInfo taxpayer) {
		Element name = doc.createElement("Name");
		name.appendChild(doc.createTextNode(taxpayer.getName()));
		rootElement.appendChild(name);
	}

	private void writeRootElement() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		doc = (Document) docBuilder.newDocument();
		rootElement = doc.createElement("Taxpayer");
		doc.appendChild(rootElement);
	}
}
