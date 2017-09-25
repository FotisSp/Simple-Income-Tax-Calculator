package inputsystem;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import dataeditor.CompanyInfo;
import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class XmlParser implements FileParser {
	private static final int NAME = 0;
	private static final int AFM = 1;
	private static final int STATUS = 2;
	private static final int INCOME = 3;
	private static final int ID = 0;
	private static final int DATE = 1;
	private static final int KIND = 2;
	private static final int AMOUNT = 3;
	private static final int COMPANY = 4;
	private static final int COUNTRY = 5;
	private static final int CITY = 6;
	private static final int STREET = 7;
	private static final int NUMBER = 8;
	private File xmlFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private TaxpayerInfo taxpayer;
	private ReceiptsInfo currentReceipt;
	private HashMap<Integer, ReceiptsInfo> receipts;
	private String[] tTable = { "Name", "AFM", "Status", "Income" };

	private String[] rTable = { "id", "Date", "Kind", "Amount", "Company",
			"Country", "City", "Street", "Number" };

	public XmlParser(File fileInput) {
		receipts = new HashMap<Integer, ReceiptsInfo>();
		openFile(fileInput);
	}

	public void openFile(File fileInput) {
		xmlFile = new File(fileInput.toString());
	}

	public void parse() {
		createDocumentBuilderFactory();
		documentBuilderParser();
		doc.getDocumentElement().normalize();

		addTaxpayerInfoElement();
		addReceiptElement();
	}

	private void documentBuilderParser() {
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (IOException | SAXException e) {
			System.out.println("Input/Output Exception! or SAX Exception");
			e.printStackTrace();
		}
	}

	private void createDocumentBuilderFactory() {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.out.println("Parser Configuration Exception!");
			pce.printStackTrace();
		}
	}

	private void addReceiptElement() {
		NodeList newList = doc.getElementsByTagName("Receipt");
		for (int temp = 0; temp < newList.getLength(); temp++) {
			getCurrentReceipt(temp, newList);
			receipts.put(temp, currentReceipt);
		}
	}

	private void getCurrentReceipt(int key, NodeList newList) {
		Node newNode = newList.item(key);
		if (newNode.getNodeType() == Node.ELEMENT_NODE) {
			Element newElement = (Element) newNode;
			addCurrentReceipt(newElement);
		}
	}

	private void addCurrentReceipt(Element newElement) {
		int id = Integer.parseInt(newElement.getAttribute(rTable[ID]));
		String date = getReceiptTagName(newElement, DATE);
		String kind = getReceiptTagName(newElement, KIND);
		double amount = Double
				.parseDouble(getReceiptTagName(newElement, AMOUNT));
		CompanyInfo companyInfo = createCompanyObject(newElement);
		currentReceipt = new ReceiptsInfo(id, date, kind, amount, companyInfo);
	}

	private CompanyInfo createCompanyObject(Element newElement) {
		String company = getReceiptTagName(newElement, COMPANY);
		String country = getReceiptTagName(newElement, COUNTRY);
		String city = getReceiptTagName(newElement, CITY);
		String street = getReceiptTagName(newElement, STREET);
		String number = getReceiptTagName(newElement, NUMBER);
		CompanyInfo companyInfo = new CompanyInfo(company, country, city,
				street, number);
		return companyInfo;
	}

	private String getReceiptTagName(Element newElement, int key) {
		return newElement.getElementsByTagName(rTable[key]).item(0)
				.getTextContent().trim();
	}

	private void addTaxpayerInfoElement() {
		NodeList nList = doc.getElementsByTagName("info");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			getTaxpayerInfo(nList, temp);
		}
	}

	private void getTaxpayerInfo(NodeList nList, int temp) {
		Node nNode = nList.item(temp);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			getInfo(nNode);
		}
	}

	private void getInfo(Node nNode) {
		String taxpayerName = getTaxpayerName(nNode);
		String taxpayerAfm = getTaxpayerAfm(nNode);
		String taxpayerStatus = getTaxpayerStatus(nNode);
		String taxpayerIncome = getTaxpayerIncome(nNode);
		taxpayer = new TaxpayerInfo(taxpayerName,
				Integer.parseInt(taxpayerAfm), taxpayerStatus,
				Integer.parseInt(taxpayerIncome));
	}

	private String getTaxpayerIncome(Node nNode) {
		Element elementIncome = (Element) nNode;
		String income = getTaxpayerTagName(elementIncome, INCOME);
		return income;
	}

	private String getTaxpayerStatus(Node nNode) {
		Element elementStatus = (Element) nNode;
		String status = getTaxpayerTagName(elementStatus, STATUS);
		return status;
	}

	private String getTaxpayerAfm(Node nNode) {
		Element elementAfm = (Element) nNode;
		String afm = getTaxpayerTagName(elementAfm, AFM);
		return afm;
	}

	private String getTaxpayerName(Node nNode) {
		Element elementName = (Element) nNode;
		String name = getTaxpayerTagName(elementName, NAME);
		return name;
	}

	private String getTaxpayerTagName(Element eElement, int key) {
		return eElement.getElementsByTagName(tTable[key]).item(0)
				.getTextContent().trim();
	}

	public HashMap<Integer, ReceiptsInfo> getReceipts() {
		return receipts;
	}

	public TaxpayerInfo getTaxpayerInfo() {
		return taxpayer;
	}
}
