package dataeditor;

import java.io.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import outputsystem.RefreshFile;
import outputsystem.RefreshFileFactory;

public class Taxpayer {
	private static final int NAME = 0;
	private static final int AFM = 1;
	private static final int STATUS = 2;
	private static final int INCOME = 3;
	private TaxpayerInfo taxpayer;
	private HashMap<Integer, ReceiptsInfo> receipts;
	private String[] infoTable = { "Name   : ", "AFM    : ", "Status : ",
			"Income : " };

	public Taxpayer(TaxpayerInfo payerInfo,
			HashMap<Integer, ReceiptsInfo> receipts) {
		taxpayer = payerInfo;
		this.receipts = new HashMap<Integer, ReceiptsInfo>();
		for (int i = 0; i < receipts.size(); i++) {
			ReceiptsInfo newReceipt = receipts.get(i);
			this.receipts.put(i, newReceipt);
		}
	}

	public String getID() {
		return Integer.toString(receipts.size() + 1);
	}

	public String getName() {
		return taxpayer.getName();
	}

	public int getAfm() {
		return taxpayer.getAfm();
	}

	public String getStatus() {
		return taxpayer.getStatus();
	}

	public int getIncome() {
		return taxpayer.getIncome();
	}

	public Integer getNumberOfReceipts() {
		return receipts.size();
	}

	public ReceiptsInfo getReceipt(int receiptId) {
		return receipts.get(receiptId);
	}

	public TaxpayerInfo getTaxpayerInfo() {
		return taxpayer;
	}

	public HashMap<Integer, ReceiptsInfo> getAllReceipts() {
		return receipts;
	}

	public boolean addNewReceiptToTaxpayer(ReceiptsInfo newReceipt, File file)
			throws IOException, TransformerConfigurationException,
			ParserConfigurationException, SAXException,
			TransformerFactoryConfigurationError, TransformerException {
		ReceiptsInfo receiptToAdd = newReceipt;
		RefreshFileFactory refreshFileFactory = new RefreshFileFactory();
		RefreshFile refreshFile = getFileExtension(file, refreshFileFactory);
		if (!refreshFile.addReceiptToFile(newReceipt)) {
			return false;
		}
		receipts.put(receipts.size(), receiptToAdd);
		return true;
	}

	private RefreshFile getFileExtension(File file,
			RefreshFileFactory refreshFileFactory) {
		return refreshFileFactory.getFileType(getFileExtension(file), file);
	}

	public void deleteReceipt(int receiptId, File file) throws IOException,
			SAXException {
		HashMap<Integer, ReceiptsInfo> alteredReceipts = new HashMap<Integer, ReceiptsInfo>();
		receipts.remove(receiptId);
		updateReceipts(receiptId, alteredReceipts);
		RefreshFileFactory refreshFileFactory = new RefreshFileFactory();
		RefreshFile refreshFile = getFileExtension(file, refreshFileFactory);
		refreshFile.deleteReceiptFromFile(receipts, taxpayer);
	}

	private void updateReceipts(int receiptId,
			HashMap<Integer, ReceiptsInfo> alteredReceipts) {
		rearrangeReceipts(receiptId, alteredReceipts);
		receipts = alteredReceipts;
		for (int i = receiptId; i < receipts.size(); i++) {
			receipts.get(i).decreaseId();
		}
	}

	private void rearrangeReceipts(int receiptId,
			HashMap<Integer, ReceiptsInfo> alteredReceipts) {
		for (int i = 0; i < receiptId; i++) {
			alteredReceipts.put(i, receipts.get(i));
		}
		for (int i = receiptId; i < receipts.size(); i++) {
			alteredReceipts.put(i, receipts.get(i + 1));
		}
	}

	private String getFileExtension(File file) {
		String pathToString = file.toString();
		int dotDelimiter = pathToString.lastIndexOf(".");
		return pathToString.substring(dotDelimiter + 1);
	}

	public String toString() {
		String infoOutputString = "", receiptOutputString = "\r\nReceipts\r\n\n";

		infoOutputString = printTaxpayerInfo(infoOutputString);
		for (int i = 0; i < receipts.size(); i++) {
			receiptOutputString += receipts.get(i).toString();
		}
		return infoOutputString + "" + receiptOutputString;
	}

	private String printTaxpayerInfo(String infoOutputString) {
		infoOutputString += infoTable[NAME] + taxpayer.getName() + "\r\n";
		infoOutputString += infoTable[AFM] + taxpayer.getAfm() + "\r\n";
		infoOutputString += infoTable[STATUS] + taxpayer.getStatus() + "\r\n";
		infoOutputString += infoTable[INCOME] + taxpayer.getIncome() + "\r\n";
		return infoOutputString;
	}
}
