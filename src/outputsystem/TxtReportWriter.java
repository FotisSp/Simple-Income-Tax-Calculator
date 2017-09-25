package outputsystem;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import dataeditor.TaxpayerInfo;

public class TxtReportWriter implements OutputFile {
	private static final int NAME = 0;
	private static final int AFM = 1;
	private static final int INCOME = 3;
	private FileWriter fileWriter;
	private BufferedWriter report;

	private String[] taxpayerTable = { "Name   : ", "AFM    : ", "",
			"Income : " };

	public void printReport(TaxpayerInfo taxpayer,
			ArrayList<String> selectedTaxes) throws IOException {
		createFileWriter(taxpayer);
		writeTaxpayerInfo(taxpayer, report);
		writeReceipts(selectedTaxes, report);
		report.close();
		JOptionPane.showMessageDialog(null, "File Saved !", "File Saved",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void createFileWriter(TaxpayerInfo taxpayer) throws IOException {
		String outputFileName = taxpayer.getAfm() + "_LOG.txt";
		try {
			fileWriter = new FileWriter(outputFileName);
			report = new BufferedWriter(fileWriter);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error creating file", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void writeReceipts(ArrayList<String> selectedTaxes,
			BufferedWriter report) throws IOException {
		for (int i = 0; i < selectedTaxes.size(); i++) {
			report.write(selectedTaxes.get(i));
			report.newLine();
		}
	}

	private void writeTaxpayerInfo(TaxpayerInfo taxpayer, BufferedWriter report)
			throws IOException {
		report.write(taxpayerTable[NAME] + taxpayer.getName());
		report.newLine();
		report.write(taxpayerTable[AFM] + taxpayer.getAfm());
		report.newLine();
		report.write(taxpayerTable[INCOME] + taxpayer.getIncome());
		report.newLine();
		report.newLine();
	}

}
