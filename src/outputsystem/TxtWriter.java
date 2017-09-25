package outputsystem;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.xml.sax.SAXException;
import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;

public class TxtWriter implements RefreshFile {
	private static final int NAME = 0;
	private static final int AFM = 1;
	private static final int STATUS = 2;
	private static final int INCOME = 3;
	private File file;
	private String[] taxpayerTable = { "Name       : ", "AFM        : ",
			"Status     : ", "Income     : " };

	public TxtWriter(File file) {
		this.file = file;
	}

	public boolean addReceiptToFile(ReceiptsInfo newReceipt) throws IOException {
		if (!checkFileExistance(file.toString())) {
			fileAppendNotFound();
			return false;
		}
		appendReceipt(newReceipt);
		return true;
	}

	private void fileAppendNotFound() {
		JOptionPane.showMessageDialog(null,
				"Could not find file : " + file.getName()
						+ " to add extra receipts!", "Error File Not Found",
				JOptionPane.ERROR_MESSAGE);
	}

	private void appendReceipt(ReceiptsInfo newReceipt) throws IOException {
		FileWriter fileWriter = new FileWriter(file, true);
		BufferedWriter appendToFile = new BufferedWriter(fileWriter);
		appendToFile.newLine();
		appendToFile.write(newReceipt.toString());
		appendToFile.newLine();
		appendToFile.close();
	}

	public void deleteReceiptFromFile(HashMap<Integer, ReceiptsInfo> receipts,
			TaxpayerInfo taxpayer) throws IOException, SAXException {
		if (!checkFileExistance(file.toString())) {
			fileDeleteNotFound();
		}
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter renewFile = new BufferedWriter(fileWriter);

		overwriteTaxpayerInfo(taxpayer, renewFile);
		overwriteReceipts(receipts, renewFile);
	}

	private void fileDeleteNotFound() {
		JOptionPane.showMessageDialog(null,
				"Could not locate file : " + file.getName()
						+ " to remove the desired receipt!", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private void overwriteReceipts(HashMap<Integer, ReceiptsInfo> receipts,
			BufferedWriter renewFile) throws IOException {
		renewFile.newLine();
		renewFile.write("Receipts   : ");
		renewFile.newLine();
		renewFile.newLine();
		for (int i = 0; i < receipts.size(); i++) {
			renewFile.write(receipts.get(i).toString());
		}
		renewFile.close();
	}

	private void overwriteTaxpayerInfo(TaxpayerInfo taxpayer,
			BufferedWriter renewFile) throws IOException {
		renewFile.write(taxpayerTable[NAME] + taxpayer.getName());
		renewFile.newLine();
		renewFile.write(taxpayerTable[AFM] + taxpayer.getAfm());
		renewFile.newLine();
		renewFile.write(taxpayerTable[STATUS] + taxpayer.getStatus());
		renewFile.newLine();
		renewFile.write(taxpayerTable[INCOME] + taxpayer.getIncome());
		renewFile.newLine();
	}

	private static boolean checkFileExistance(String filename) {
		boolean exists = new File(filename).exists();
		return exists;
	}
}
