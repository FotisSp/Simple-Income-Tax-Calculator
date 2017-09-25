package inputsystem;

import java.util.*;
import javax.swing.*;
import dataeditor.Taxpayer;
import java.io.*;

@SuppressWarnings("serial")
public class FileHandler extends JFrame {
	private File[] selectedFiles;
	private ArrayList<File> listOfFiles;
	private HashMap<Integer, Taxpayer> taxpayersEntries;
	private FileParserFactory fileParserFactory;

	public FileHandler() {
		listOfFiles = new ArrayList<File>();
		taxpayersEntries = new HashMap<Integer, Taxpayer>();
	}

	public void openFileChooser() {
		JFileChooser chooser = new JFileChooser();
		if (approveOption(chooser)) {
			approveEntries();
		}
	}

	private void approveEntries() {
		for (int i = 0; i < listOfFiles.size(); i++) {
			try {
				addTaxpayerEntries(i);
			} catch (NullPointerException e) {
				showFileError(i);
				break;
			}
		}
	}

	private boolean approveOption(JFileChooser chooser) {
		return getSelectedFiles(chooser) == JFileChooser.APPROVE_OPTION;
	}

	private void addTaxpayerEntries(int i) {
		fileParserFactory = new FileParserFactory();
		FileParser fileParser = getFileType(i);
		fileParser.parse();
		Taxpayer payer = new Taxpayer(fileParser.getTaxpayerInfo(),
				fileParser.getReceipts());
		taxpayersEntries.put(i, payer);
	}

	private FileParser getFileType(int i) {
		return fileParserFactory.getFileType(getFileExtension(i),
				listOfFiles.get(i));
	}

	private void showFileError(int i) {
		JOptionPane.showMessageDialog(null, "Wrong file type", "File error",
				JOptionPane.ERROR_MESSAGE);
		listOfFiles.remove(i);
	}

	private int getSelectedFiles(JFileChooser chooser) {
		int option = getListOfFiles(chooser);
		for (int i = 0; i < selectedFiles.length; i++) {
			addSelectedFiles(i);
		}
		return option;
	}

	private void addSelectedFiles(int i) {
		if (selectedFiles[i].length() == 0) {
			JOptionPane.showMessageDialog(null,
					"File not found or file is empty", "File error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			listOfFiles.add(selectedFiles[i]);
		}
	}

	private int getListOfFiles(JFileChooser chooser) {
		chooser.setMultiSelectionEnabled(true);
		int option = chooser.showOpenDialog(FileHandler.this);
		selectedFiles = chooser.getSelectedFiles();
		return option;
	}

	private String getFileExtension(int fileID) {
		String pathToString = listOfFiles.get(fileID).toString();
		int dotDelimiter = pathToString.lastIndexOf(".");
		String extension = pathToString.substring(dotDelimiter + 1);

		return extension;
	}

	public void removeTaxpayer(int taxpayerSelectedIndex) {
		listOfFiles.remove(taxpayerSelectedIndex);
		taxpayersEntries.remove(taxpayerSelectedIndex);
		for (int i = taxpayerSelectedIndex; i < taxpayersEntries.size() - 1; i++) {
			taxpayersEntries.put(taxpayerSelectedIndex,
					taxpayersEntries.get(taxpayerSelectedIndex + 1));
			taxpayersEntries.remove(taxpayerSelectedIndex + 1);
		}
	}

	public File getFile(int fileId) {
		return listOfFiles.get(fileId);
	}

	public Integer getNumberOfAddedFiles() {
		return listOfFiles.size();
	}

	public HashMap<Integer, Taxpayer> getTaxpayersEntries() {
		return taxpayersEntries;
	}
}
