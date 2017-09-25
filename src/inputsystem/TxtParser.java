package inputsystem;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import dataeditor.CompanyInfo;
import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;

public class TxtParser implements FileParser {
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
	private static final int INITIALISECOUNTER = 0;
	private static final int RECEIPTSINFO = 9;
	private static final int RECEIPTINFOSTART = 5;
	private TaxpayerInfo taxpayer;
	private ReceiptsInfo currentReceipt;
	private ArrayList<String> fileIndex;
	private HashMap<Integer, ReceiptsInfo> receipts;

	public TxtParser(File fileInput) {
		receipts = new HashMap<Integer, ReceiptsInfo>();
		openFile(fileInput);
	}

	public void openFile(File fileInput) {
		try {
			readFile(fileInput);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Error opening file! File not found");
		}
	}

	private void readFile(File fileInput) throws FileNotFoundException {
		Scanner inputFile = new Scanner(new FileInputStream(fileInput));
		fileIndex = new ArrayList<String>();
		while (inputFile.hasNextLine()) {
			String line = inputFile.nextLine();
			checkNotEmptyLine(line);
		}
		inputFile.close();
	}

	private void checkNotEmptyLine(String line) {
		if (!line.equals("")) {
			fileIndex.add(line);
		}
	}

	public void parse() {
		getUserInfo();
		getReceiptInfo();
	}

	private void getUserInfo() {
		String[] taxpayerName = fileIndex.get(NAME).trim().split(":");
		String[] taxpayerAfm = fileIndex.get(AFM).trim().split(":");
		String[] taxpayerStatus = fileIndex.get(STATUS).trim().split(":");
		String[] taxpayerIncome = fileIndex.get(INCOME).trim().split(":");
		taxpayer = new TaxpayerInfo(taxpayerName[1].trim(),
				Integer.parseInt(taxpayerAfm[1].trim()),
				taxpayerStatus[1].trim(), Integer.parseInt(taxpayerIncome[1]
						.trim()));
	}

	private void getReceiptInfo() {
		int receiptInfoStart = RECEIPTINFOSTART, key = INITIALISECOUNTER;

		while (receiptInfoStart < fileIndex.size()) {
			addCurrentReceipt(receiptInfoStart);
			receipts.put(key, currentReceipt);
			key++;
			receiptInfoStart += RECEIPTSINFO;
		}
	}

	private void addCurrentReceipt(int receiptInfoStart) {
		String[] id = fileIndex.get(receiptInfoStart + ID).trim().split(":");
		String[] date = fileIndex.get(receiptInfoStart + DATE).trim()
				.split(":");
		String[] kind = fileIndex.get(receiptInfoStart + KIND).trim()
				.split(":");
		String[] amount = fileIndex.get(receiptInfoStart + AMOUNT).trim()
				.split(":");
		CompanyInfo companyInfo = createCompanyObject(receiptInfoStart);
		currentReceipt = new ReceiptsInfo(Integer.parseInt(id[1].trim()),
				date[1], kind[1], Double.parseDouble(amount[1].trim()),
				companyInfo);
	}

	private CompanyInfo createCompanyObject(int receiptInfoStart) {
		String[] company = fileIndex.get(receiptInfoStart + COMPANY).trim()
				.split(":");
		String[] country = fileIndex.get(receiptInfoStart + COUNTRY).trim()
				.split(":");
		String[] city = fileIndex.get(receiptInfoStart + CITY).trim()
				.split(":");
		String[] street = fileIndex.get(receiptInfoStart + STREET).trim()
				.split(":");
		String[] number = fileIndex.get(receiptInfoStart + NUMBER).trim()
				.split(":");
		CompanyInfo companyInfo = new CompanyInfo(company[1], country[1],
				city[1], street[1], number[1]);
		return companyInfo;
	}

	public HashMap<Integer, ReceiptsInfo> getReceipts() {
		return receipts;
	}

	public TaxpayerInfo getTaxpayerInfo() {
		return taxpayer;
	}
}
