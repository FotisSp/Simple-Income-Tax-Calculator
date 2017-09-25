package outputsystem;

import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;

public class XmlWriter implements RefreshFile {
	private static final int ID = 0;
	private static final int DATE = 1;
	private static final int KIND = 2;
	private static final int AMOUNT = 3;
	private static final int COMPANY = 4;
	private static final int COUNTRY = 5;
	private static final int CITY = 6;
	private static final int STREET = 7;
	private static final int NUMBER = 8;
	private static final int NAME = 0;
	private static final int AFM = 1;
	private static final int STATUS = 2;
	private static final int INCOME = 3;
	private File file;
	private Element receipt;
	private Element rootElement;
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	private String[] taxpayerTable = { "Name", "AFM", "Status", "Income" };
	private String[] receiptTable = { "id", "Date", "Kind", "Amount",
			"Company", "Country", "City", "Street", "Number" };

	public XmlWriter(File file) {
		this.file = file;
	}

	public boolean addReceiptToFile(ReceiptsInfo newReceipt)
			throws IOException, ParserConfigurationException, SAXException,
			TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		if (!checkFileExistance(file.toString())) {
			fileAppendNotFound();
			return false;
		}
		appendXmlFile(newReceipt);
		return true;
	}

	private void appendXmlFile(ReceiptsInfo newReceipt)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		setDocBuilder();
		doc = docBuilder.parse(file);
		appendReceiptElement(newReceipt);
		appendReceiptInfo(newReceipt);
		appendToFile();
	}

	private void appendToFile() throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file.toString()));
		transformer.transform(source, result);
	}

	private void setDocBuilder() throws ParserConfigurationException,
			SAXException, IOException {
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
	}

	private void appendReceiptInfo(ReceiptsInfo newReceipt) {
		appendBasicReceiptInfo(newReceipt);
		appendCompanyInfo(newReceipt);
	}

	private void appendCompanyInfo(ReceiptsInfo newReceipt) {
		appendCompany(newReceipt);
		appendCountry(newReceipt);
		appendCity(newReceipt);
		appendStreet(newReceipt);
		appendNumber(newReceipt);
	}

	private void appendBasicReceiptInfo(ReceiptsInfo newReceipt) {
		appendId(newReceipt);
		appendDate(newReceipt);
		appendKind(newReceipt);
		appendAmmount(newReceipt);
	}

	private void appendNumber(ReceiptsInfo newReceipt) {
		Element newReceiptNumber = doc.createElement(receiptTable[NUMBER]);
		newReceiptNumber
				.appendChild(doc.createTextNode(newReceipt.getNumber()));
		receipt.appendChild(newReceiptNumber);
	}

	private void appendStreet(ReceiptsInfo newReceipt) {
		Element newReceiptStreet = doc.createElement(receiptTable[STREET]);
		newReceiptStreet
				.appendChild(doc.createTextNode(newReceipt.getStreet()));
		receipt.appendChild(newReceiptStreet);
	}

	private void appendCity(ReceiptsInfo newReceipt) {
		Element newReceiptCity = doc.createElement(receiptTable[CITY]);
		newReceiptCity.appendChild(doc.createTextNode(newReceipt.getCity()));
		receipt.appendChild(newReceiptCity);
	}

	private void appendCountry(ReceiptsInfo newReceipt) {
		Element newReceiptCountry = doc.createElement(receiptTable[COUNTRY]);
		newReceiptCountry.appendChild(doc.createTextNode(newReceipt
				.getCountry()));
		receipt.appendChild(newReceiptCountry);
	}

	private void appendCompany(ReceiptsInfo newReceipt) {
		Element newReceiptCompany = doc.createElement(receiptTable[COMPANY]);
		newReceiptCompany.appendChild(doc.createTextNode(newReceipt
				.getCompany()));
		receipt.appendChild(newReceiptCompany);
	}

	private void appendAmmount(ReceiptsInfo newReceipt) {
		Element newReceiptAmount = doc.createElement(receiptTable[AMOUNT]);
		newReceiptAmount.appendChild(doc.createTextNode(""
				+ newReceipt.getAmount()));
		receipt.appendChild(newReceiptAmount);
	}

	private void appendKind(ReceiptsInfo newReceipt) {
		Element newReceiptKind = doc.createElement(receiptTable[KIND]);
		newReceiptKind.appendChild(doc.createTextNode(newReceipt.getKind()));
		receipt.appendChild(newReceiptKind);
	}

	private void appendDate(ReceiptsInfo newReceipt) {
		Element newReceiptDate = doc.createElement(receiptTable[DATE]);
		newReceiptDate.appendChild(doc.createTextNode(newReceipt.getDate()));
		receipt.appendChild(newReceiptDate);
	}

	private void appendId(ReceiptsInfo newReceipt) {
		Element newReceiptId = doc.createElement(receiptTable[ID]);
		newReceiptId.appendChild(doc.createTextNode("" + newReceipt.getId()));
		receipt.appendChild(newReceiptId);
	}

	private void appendReceiptElement(ReceiptsInfo newReceipt) {
		Node rootElement = doc.getFirstChild();
		receipt = doc.createElement("Receipt");
		rootElement.appendChild(receipt);

		Attr xmlAttribute = doc.createAttribute("id");
		int id = newReceipt.getId();
		xmlAttribute.setValue("" + id);
		receipt.setAttributeNode(xmlAttribute);
	}

	private void fileAppendNotFound() {
		fileDeleteNotFound();
	}

	public void deleteReceiptFromFile(HashMap<Integer, ReceiptsInfo> receipts,
			TaxpayerInfo taxpayer) throws IOException, SAXException {
		try {
			if (!checkFileExistance(file.toString())) {
				fileDeleteNotFound();
			}
			overwriteXmlFile(receipts, taxpayer);
		} catch (ParserConfigurationException | TransformerException e) {
			System.out
					.println("Sax parser configuration problem or Transformer error exception");
		}
	}

	private void overwriteXmlFile(HashMap<Integer, ReceiptsInfo> receipts,
			TaxpayerInfo taxpayer) throws ParserConfigurationException,
			SAXException, IOException, TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		setDocBuilder();
		doc = docBuilder.newDocument();
		overwriteTaxpayerElement(taxpayer);
		overwriteReceipts(receipts);
		appendToFile();
	}

	private void overwriteReceipts(HashMap<Integer, ReceiptsInfo> receipts) {
		for (int i = 0; i < receipts.size(); i++) {
			Attr xmlAttribute = newReceiptElement();
			int id = i + 1;
			xmlAttribute.setValue("" + id);
			receipt.setAttributeNode(xmlAttribute);
			addReceiptInfo(receipts, i);
		}
	}

	private Attr newReceiptElement() {
		receipt = doc.createElement("Receipt");
		rootElement.appendChild(receipt);
		Attr xmlAttribute = doc.createAttribute("id");
		return xmlAttribute;
	}

	private void addReceiptInfo(HashMap<Integer, ReceiptsInfo> receipts, int key) {
		for (int j = 0; j < receipts.size(); j++) {
			appendReceiptInfo(receipts.get(key));
		}
	}

	private void overwriteTaxpayerElement(TaxpayerInfo taxpayer) {
		Element info = newTaxpayerElement();

		overwriteName(taxpayer, info);
		overwriteAfm(taxpayer, info);
		overwriteStatus(taxpayer, info);
		overwriteIncome(taxpayer, info);
	}

	private void overwriteIncome(TaxpayerInfo taxpayer, Element info) {
		Element taxpayerIncome = doc.createElement(taxpayerTable[INCOME]);
		taxpayerIncome.appendChild(doc.createTextNode(taxpayer.getIncome()
				.toString()));
		info.appendChild(taxpayerIncome);
	}

	private void overwriteStatus(TaxpayerInfo taxpayer, Element info) {
		Element taxpayerStatus = doc.createElement(taxpayerTable[STATUS]);
		taxpayerStatus.appendChild(doc.createTextNode(taxpayer.getStatus()));
		info.appendChild(taxpayerStatus);
	}

	private void overwriteAfm(TaxpayerInfo taxpayer, Element info) {
		Element taxpayerAfm = doc.createElement(taxpayerTable[AFM]);
		taxpayerAfm.appendChild(doc
				.createTextNode(taxpayer.getAfm().toString()));
		info.appendChild(taxpayerAfm);
	}

	private void overwriteName(TaxpayerInfo taxpayer, Element info) {
		Element taxpayerName = doc.createElement(taxpayerTable[NAME]);
		taxpayerName.appendChild(doc.createTextNode(taxpayer.getName()));
		info.appendChild(taxpayerName);
	}

	private Element newTaxpayerElement() {
		rootElement = doc.createElement("Taxpayer");
		doc.appendChild(rootElement);
		Element info = doc.createElement("info");
		rootElement.appendChild(info);
		return info;
	}

	private void fileDeleteNotFound() {
		JOptionPane.showMessageDialog(null,
				"Could not locate file : " + file.getName()
						+ " to remove the desired receipt!", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private static boolean checkFileExistance(String filename) {
		boolean fileExists = new File(filename).exists();
		return fileExists;
	}
}