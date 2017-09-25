package gui;

import inputsystem.FileHandler;
import outputsystem.OutputFile;
import outputsystem.OutputFileFactory;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import org.xml.sax.SAXException;

import dataeditor.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

import com.toedter.calendar.JDateChooser;

public class IncomeTaxGUI {
	private JFrame frmIncomeTaxCalculator;
	private JPanel mainPanel;
	private JPanel furtherFunctions;
	private JPanel deleteReceipt;
	private JPanel reportOptions;
	private JButton btnNext;
	private JButton btnImportNewTaxpayer;
	private JButton btnAddReceipt;
	private JButton btnDeleteReceipt;
	private JButton btnViewReport;
	private JButton btnDone;
	private JButton btnDelete;
	private JButton btnBack_2;
	private JButton btnDone_2;
	private JButton btnBack_3;
	private JList<String> taxpayersList;
	private JList<String> deleteReceiptList;
	private JTextArea TaxpayerInformationWindow;
	private JTextArea deleteReceiptWindow;
	private DefaultListModel<String> addListModel;
	private DefaultListModel<String> deleteListModel;
	private DefaultListModel<String> reportListModel;
	private JScrollPane scrollPane;
	private ArrayList<Taxpayer> taxpayerInfo = new ArrayList<Taxpayer>();
	private int numberOfAddedFiles = 0;
	private JPanel addNewReceipt;
	private JTextField amountTextField;
	private JTextField companyTextField;
	private JTextField countryTextField;
	private JTextField cityTextField;
	private JTextField streetTextField;
	private JTextField numberTextField;
	private JTable addNewReceiptTable;
	private JButton btnAddNewReceipt;
	private Object[][] receiptID = {};
	private Object[] headers = { "ReceiptID", "Date", "Kind", "Amount",
			"Company", "Country", "City", "Street", "Number" };
	DefaultTableModel newReceiptTableModel = new DefaultTableModel(receiptID,
			headers);
	private JComboBox<String> kindOptionsBox;
	private JList<String> reportOptionsList;
	private ArrayList<String> printReportOptions;
	private int taxpayerSelectedIndex;
	private int deleteReceiptSelectedIndex;
	private int previousTaxpayer = 0;
	private HashMap<Integer, Taxpayer> taxpayers;
	private FileHandler file = null;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JButton btnDeleteTaxpayer;
	private JCheckBox basicTaxCheckBox;
	private JCheckBox taxAnalysisCheckBox;
	private JCheckBox totalReceiptValueCheckBox;
	private JCheckBox expensesAnalysisCheckBox;
	private ReceiptValue totalReceiptValue;
	private JButton btnSaveReport;
	private JRadioButton txtOutputFormatButton;
	private JRadioButton xmlOutputFormatButton;
	private String fileExtension = null;
	private String date = null;
	private JDateChooser chooser;
	private SimpleDateFormat sdf;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IncomeTaxGUI window = new IncomeTaxGUI();
					window.frmIncomeTaxCalculator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public IncomeTaxGUI() {
		initialize();
	}

	private void initialize() {
		addListModel = new DefaultListModel<String>();
		deleteListModel = new DefaultListModel<String>();
		reportListModel = new DefaultListModel<String>();
		file = new FileHandler();
		printReportOptions = new ArrayList<String>();

		frmIncomeTaxCalculator = new JFrame();
		frmIncomeTaxCalculator.setTitle("Income Tax Calculator");
		frmIncomeTaxCalculator.setBounds(100, 100, 800, 600);
		frmIncomeTaxCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIncomeTaxCalculator.getContentPane().setLayout(new CardLayout(0, 0));

		mainPanel = new JPanel();
		frmIncomeTaxCalculator.getContentPane().add(mainPanel, "mainPanel");
		mainPanel.setLayout(null);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(20, 55, 216, 457);
		mainPanel.add(scrollPane_2);

		taxpayersList = new JList<String>();
		scrollPane_2.setViewportView(taxpayersList);

		btnNext = new JButton("Next");
		btnNext.addActionListener(new GeneralButtonListener());
		btnNext.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNext.setBounds(679, 514, 95, 36);
		btnNext.setEnabled(false);
		mainPanel.add(btnNext);

		btnImportNewTaxpayer = new JButton("+");
		btnImportNewTaxpayer.addActionListener(new MainPanelButtonListener());
		btnImportNewTaxpayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnImportNewTaxpayer.setBounds(20, 514, 45, 36);
		mainPanel.add(btnImportNewTaxpayer);

		btnDeleteTaxpayer = new JButton("-");
		btnDeleteTaxpayer.addActionListener(new MainPanelButtonListener());
		btnDeleteTaxpayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDeleteTaxpayer.setBounds(72, 514, 45, 36);
		btnDeleteTaxpayer.setEnabled(false);
		mainPanel.add(btnDeleteTaxpayer);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(262, 56, 501, 399);
		mainPanel.add(scrollPane);

		TaxpayerInformationWindow = new JTextArea();
		scrollPane.setViewportView(TaxpayerInformationWindow);
		TaxpayerInformationWindow.setEditable(false);

		JLabel lblInformation = new JLabel("Information");
		lblInformation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInformation.setBounds(262, 11, 132, 33);
		mainPanel.add(lblInformation);

		JLabel lblSelectTaxpayer = new JLabel("Select Tax Payer");
		lblSelectTaxpayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectTaxpayer.setBounds(20, 16, 139, 22);
		mainPanel.add(lblSelectTaxpayer);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new GeneralButtonListener());
		btnExit.setBounds(572, 515, 95, 36);
		mainPanel.add(btnExit);

		furtherFunctions = new JPanel();
		frmIncomeTaxCalculator.getContentPane().add(furtherFunctions,
				"furtherFunctionsPanel");
		furtherFunctions.setLayout(null);

		btnAddReceipt = new JButton("Add Receipt");
		btnAddReceipt.addActionListener(new AddReceiptButtonListener());
		btnAddReceipt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAddReceipt.setBounds(134, 153, 150, 65);
		furtherFunctions.add(btnAddReceipt);

		btnDeleteReceipt = new JButton("Delete Receipt");
		btnDeleteReceipt.addActionListener(new DeleteReceiptButtonListener());
		btnDeleteReceipt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDeleteReceipt.setBounds(452, 153, 150, 65);
		furtherFunctions.add(btnDeleteReceipt);

		btnViewReport = new JButton("View Report");
		btnViewReport.addActionListener(new ReportButtonListener());
		btnViewReport.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnViewReport.setBounds(247, 269, 256, 65);
		furtherFunctions.add(btnViewReport);

		btnDone = new JButton("Done");
		btnDone.addActionListener(new GeneralButtonListener());
		btnDone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDone.setBounds(681, 514, 95, 36);
		furtherFunctions.add(btnDone);

		JLabel lblChooseAnAction = new JLabel("Choose An Action");
		lblChooseAnAction.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblChooseAnAction.setBounds(294, 49, 160, 43);
		furtherFunctions.add(lblChooseAnAction);

		addNewReceipt = new JPanel();
		frmIncomeTaxCalculator.getContentPane().add(addNewReceipt,
				"addNewReceipt");
		addNewReceipt.setLayout(null);

		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(144, 42, 60, 14);
		addNewReceipt.add(lblDate);

		JLabel lblKind = new JLabel("Kind");
		lblKind.setBounds(144, 67, 60, 14);
		addNewReceipt.add(lblKind);

		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setBounds(144, 92, 60, 14);
		addNewReceipt.add(lblAmount);

		JLabel lblCompany = new JLabel("Company");
		lblCompany.setBounds(144, 117, 60, 14);
		addNewReceipt.add(lblCompany);

		JLabel lblCountry = new JLabel("Country");
		lblCountry.setBounds(144, 142, 60, 14);
		addNewReceipt.add(lblCountry);

		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(144, 167, 60, 14);
		addNewReceipt.add(lblCity);

		JLabel lblStreet = new JLabel("Street");
		lblStreet.setBounds(144, 192, 60, 14);
		addNewReceipt.add(lblStreet);

		JLabel lblNumber = new JLabel("Number");
		lblNumber.setBounds(144, 217, 60, 14);
		addNewReceipt.add(lblNumber);

		amountTextField = new JTextField();
		amountTextField.setColumns(10);
		amountTextField.setBounds(266, 89, 299, 20);
		addNewReceipt.add(amountTextField);

		companyTextField = new JTextField();
		companyTextField.setColumns(10);
		companyTextField.setBounds(266, 114, 299, 20);
		addNewReceipt.add(companyTextField);

		countryTextField = new JTextField();
		countryTextField.setColumns(10);
		countryTextField.setBounds(266, 139, 299, 20);
		addNewReceipt.add(countryTextField);

		cityTextField = new JTextField();
		cityTextField.setColumns(10);
		cityTextField.setBounds(266, 164, 299, 20);
		addNewReceipt.add(cityTextField);

		streetTextField = new JTextField();
		streetTextField.setColumns(10);
		streetTextField.setBounds(266, 189, 299, 20);
		addNewReceipt.add(streetTextField);

		numberTextField = new JTextField();
		numberTextField.setColumns(10);
		numberTextField.setBounds(266, 214, 299, 20);
		addNewReceipt.add(numberTextField);

		addNewReceiptTable = new JTable(newReceiptTableModel);
		addNewReceiptTable.setBounds(58, 279, 653, 211);
		addNewReceipt.add(addNewReceiptTable);
		addNewReceiptTable.setEnabled(false);

		btnAddNewReceipt = new JButton("Add");
		btnAddNewReceipt.addActionListener(new AddReceiptButtonListener());
		btnAddNewReceipt.setBounds(607, 36, 117, 29);
		addNewReceipt.add(btnAddNewReceipt);

		JButton btnDone_1 = new JButton("Done");
		btnDone_1.addActionListener(new GeneralButtonListener());
		btnDone_1.setBounds(679, 514, 95, 36);
		addNewReceipt.add(btnDone_1);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new GeneralButtonListener());
		btnBack.setBounds(572, 514, 95, 36);
		btnBack.setName("btnBack");
		addNewReceipt.add(btnBack);

		kindOptionsBox = new JComboBox<String>();
		kindOptionsBox.setBounds(266, 64, 299, 20);
		kindOptionsBox.setToolTipText("Choose Receipt Kind");
		kindOptionsBox.addItem("Basic");
		kindOptionsBox.addItem("Entertainment");
		kindOptionsBox.addItem("Travel");
		kindOptionsBox.addItem("Health");
		kindOptionsBox.addItem("Other");
		addNewReceipt.add(kindOptionsBox);

		sdf = new SimpleDateFormat("d/M/yyyy");
		chooser = new JDateChooser();
		chooser.setDateFormatString("d/M/yyyy");
		chooser.getDateEditor().setEnabled(false);
		chooser.getDateEditor().addPropertyChangeListener(
				new CalendarActionListener());
		chooser.setBounds(266, 39, 299, 20);
		addNewReceipt.add(chooser);

		deleteReceipt = new JPanel();
		frmIncomeTaxCalculator.getContentPane().add(deleteReceipt,
				"deleteReceiptPanel");
		deleteReceipt.setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(25, 39, 195, 475);
		deleteReceipt.add(scrollPane_1);

		deleteReceiptList = new JList<String>();
		scrollPane_1.setViewportView(deleteReceiptList);

		deleteReceiptWindow = new JTextArea();
		deleteReceiptWindow.setBounds(246, 39, 513, 416);
		deleteReceipt.add(deleteReceiptWindow);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new DeleteReceiptButtonListener());
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDelete.setBounds(246, 466, 513, 36);
		deleteReceipt.add(btnDelete);

		btnBack_2 = new JButton("Back");
		btnBack_2.addActionListener(new GeneralButtonListener());
		btnBack_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBack_2.setBounds(664, 514, 95, 36);
		deleteReceipt.add(btnBack_2);

		JLabel lblReceiptsId = new JLabel("Receipts ID");
		lblReceiptsId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblReceiptsId.setBounds(25, 18, 123, 14);
		deleteReceipt.add(lblReceiptsId);

		JLabel lblReceiptInformation = new JLabel("Receipt Information");
		lblReceiptInformation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblReceiptInformation.setBounds(246, 18, 163, 14);
		deleteReceipt.add(lblReceiptInformation);

		reportOptions = new JPanel();
		frmIncomeTaxCalculator.getContentPane().add(reportOptions,
				"reportOptionsPanel");
		reportOptions.setLayout(null);

		basicTaxCheckBox = new JCheckBox("Basic Tax");
		basicTaxCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		basicTaxCheckBox.setBounds(38, 102, 161, 23);
		basicTaxCheckBox.addItemListener(new CheckBoxListener());
		reportOptions.add(basicTaxCheckBox);

		taxAnalysisCheckBox = new JCheckBox("Tax Analysis");
		taxAnalysisCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		taxAnalysisCheckBox.setBounds(38, 137, 161, 23);
		taxAnalysisCheckBox.addItemListener(new CheckBoxListener());
		reportOptions.add(taxAnalysisCheckBox);

		totalReceiptValueCheckBox = new JCheckBox("Total Receipt Value");
		totalReceiptValueCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		totalReceiptValueCheckBox.setBounds(38, 178, 180, 23);
		totalReceiptValueCheckBox.addItemListener(new CheckBoxListener());
		reportOptions.add(totalReceiptValueCheckBox);

		expensesAnalysisCheckBox = new JCheckBox("Expenses Analysis");
		expensesAnalysisCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		expensesAnalysisCheckBox.setBounds(38, 215, 161, 23);
		expensesAnalysisCheckBox.addItemListener(new CheckBoxListener());
		reportOptions.add(expensesAnalysisCheckBox);

		btnDone_2 = new JButton("Done");
		btnDone_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDone_2.addActionListener(new GeneralButtonListener());
		btnDone_2.setBounds(679, 514, 95, 36);
		reportOptions.add(btnDone_2);

		btnBack_3 = new JButton(" Back");
		btnBack_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnBack_3.addActionListener(new GeneralButtonListener());
		btnBack_3.setBounds(466, 514, 95, 36);
		reportOptions.add(btnBack_3);

		JLabel lblChooseWhichInformation = new JLabel(
				"Choose which information to include");
		lblChooseWhichInformation.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblChooseWhichInformation.setBounds(230, 37, 301, 71);
		reportOptions.add(lblChooseWhichInformation);

		reportOptionsList = new JList<String>();
		reportOptionsList.setBounds(250, 107, 484, 369);
		reportOptions.add(reportOptionsList);

		btnSaveReport = new JButton("Save Report");
		btnSaveReport.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSaveReport.addActionListener(new ReportButtonListener());
		btnSaveReport.setBounds(574, 514, 95, 36);
		reportOptions.add(btnSaveReport);

		xmlOutputFormatButton = new JRadioButton("XML");
		xmlOutputFormatButton.setBounds(64, 453, 67, 23);
		xmlOutputFormatButton.addActionListener(new radioButtonListener());
		reportOptions.add(xmlOutputFormatButton);

		txtOutputFormatButton = new JRadioButton("TXT");
		txtOutputFormatButton.setBounds(143, 453, 56, 23);
		txtOutputFormatButton.addActionListener(new radioButtonListener());
		reportOptions.add(txtOutputFormatButton);

		JLabel lblChooseOutputFile = new JLabel("Choose output file format :");
		lblChooseOutputFile.setBounds(38, 425, 180, 16);
		reportOptions.add(lblChooseOutputFile);
	}

	private class GeneralButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String buttonName = ((JButton) event.getSource()).getText();

			if (buttonName.contains("Next")) {
				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.next(frmIncomeTaxCalculator.getContentPane());
				reportListModel.clear();
			} else if (buttonName.contains("Back")) {
				txtOutputFormatButton.setSelected(false);
				xmlOutputFormatButton.setSelected(false);
				reportListModel.clear();
				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.show(frmIncomeTaxCalculator.getContentPane(),
						"furtherFunctionsPanel");
			} else if (buttonName.contains("Done")) {
				previousTaxpayer = taxpayerSelectedIndex;
				refreshTaxpayersList();
				txtOutputFormatButton.setSelected(false);
				xmlOutputFormatButton.setSelected(false);
				printReportOptions.clear();
				btnNext.setEnabled(false);

				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.first(frmIncomeTaxCalculator.getContentPane());

			} else if (buttonName.contains("Exit")) {
				System.exit(0);
			}
		}

		private void refreshTaxpayersList() {
			addListModel.removeAllElements();
			TaxpayerInformationWindow.setText(null);

			for (int i = 0; i < taxpayerInfo.size(); i++) {
				addListModel.addElement(taxpayerInfo.get(i).getName());
				taxpayersList.setModel(addListModel);
			}
		}
	}

	private class MainPanelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String buttonName = ((JButton) event.getSource()).getText();

			if (buttonName.contains("+")) {
				file.openFileChooser();
				taxpayers = file.getTaxpayersEntries();

				for (int i = numberOfAddedFiles; i < taxpayers.size(); i++) {
					taxpayerInfo.add(taxpayers.get(i));
				}
				for (int i = numberOfAddedFiles; i < taxpayerInfo.size(); i++) {
					addListModel.addElement(taxpayerInfo.get(i).getName());
					taxpayersList.setModel(addListModel);
				}
				numberOfAddedFiles = file.getNumberOfAddedFiles();

				taxpayersList
						.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								taxpayerSelectedIndex = taxpayersList
										.getSelectedIndex();
								if (taxpayerSelectedIndex != -1) {
									btnNext.setEnabled(true);
									btnDeleteTaxpayer.setEnabled(true);
									TaxpayerInformationWindow.setText(" "
											+ taxpayerInfo
													.get(taxpayerSelectedIndex));
								}
							}
						});
				deleteReceiptWindow.setText(null);
			} else if (buttonName.equals("-") && taxpayerSelectedIndex != -1) {
				TaxpayerInformationWindow.setText(null);
				taxpayerInfo.remove(taxpayerSelectedIndex);
				file.removeTaxpayer(taxpayerSelectedIndex);
				addListModel.remove(taxpayerSelectedIndex);
				numberOfAddedFiles--;
				if (addListModel.isEmpty()) {
					btnDeleteTaxpayer.setEnabled(false);
				}
			}
		}
	}

	private class AddReceiptButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String buttonName = ((JButton) event.getSource()).getText();

			if (buttonName.equals("Add Receipt")) {
				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.next(frmIncomeTaxCalculator.getContentPane());
				if (previousTaxpayer != taxpayerSelectedIndex) {
					newReceiptTableModel.setRowCount(0);
					clearFields();
				}
			} else if (buttonName.equals("Add")) {
				try {
					boolean emptyFields = true;

					int id = Integer.parseInt(taxpayerInfo.get(
							taxpayerSelectedIndex).getID());
					String receiptDate = date;
					if (receiptDate.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					String kind = kindOptionsBox.getSelectedItem().toString();
					if (kind.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					double isNumber = Double.parseDouble(amountTextField
							.getText());
					if (amountTextField.getText().isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					double amount = isNumber;
					String company = companyTextField.getText();
					if (company.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					String country = countryTextField.getText();
					if (country.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					String city = cityTextField.getText();
					if (city.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					String street = streetTextField.getText();
					if (street.isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					String number = numberTextField.getText();
					if (numberTextField.getText().isEmpty()) {
						throw new Exception("Cannot leave empty field(s)");
					}
					CompanyInfo companyInfo = new CompanyInfo(company, country,
							city, street, number);
					ReceiptsInfo newReceipt = new ReceiptsInfo(id, receiptDate,
							kind, amount, companyInfo);

					if (emptyFields) {
						boolean checkIfFileExists = taxpayers.get(
								taxpayerSelectedIndex)
								.addNewReceiptToTaxpayer(newReceipt,
										file.getFile(taxpayerSelectedIndex));
						if (!checkIfFileExists) {
							throw new Exception("File Does not exist");
						}

						newReceiptTableModel = (DefaultTableModel) addNewReceiptTable
								.getModel();
						newReceiptTableModel
								.addRow(new Object[] {
										taxpayerInfo.get(taxpayerSelectedIndex)
												.getID(),
										date,
										kindOptionsBox.getSelectedItem()
												.toString(),
										amountTextField.getText(),
										companyTextField.getText(),
										countryTextField.getText(),
										cityTextField.getText(),
										streetTextField.getText(),
										numberTextField.getText() });
						clearFields();
					}
					refreshAddedReceiptList();
				} catch (Exception e) {
					String message = e.getMessage();
					if (e.getClass().getName()
							.contains("NumberFormatException")) {
						JOptionPane.showMessageDialog(null,
								"Amount value must be a number", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, message, "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

		private void refreshAddedReceiptList() {
			int numberOfReceipts = taxpayers.get(taxpayerSelectedIndex)
					.getNumberOfReceipts();
			deleteListModel.removeAllElements();
			deleteReceiptWindow.setText(null);

			for (int i = 0; i < numberOfReceipts; i++) {
				deleteListModel.addElement(taxpayers.get(taxpayerSelectedIndex)
						.getReceipt(i).getReceiptID());
				deleteReceiptList.setModel(deleteListModel);
			}
		}

		private void clearFields() {
			date = "";
			chooser.setDate(new Date());
			amountTextField.setText(null);
			companyTextField.setText(null);
			countryTextField.setText(null);
			cityTextField.setText(null);
			streetTextField.setText(null);
			numberTextField.setText(null);
		}
	}

	private class DeleteReceiptButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String buttonName = ((JButton) event.getSource()).getText();

			if (buttonName.equals("Delete Receipt")) {
				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.show(frmIncomeTaxCalculator.getContentPane(),
						"deleteReceiptPanel");
				int numberOfReceipts = taxpayers.get(taxpayerSelectedIndex)
						.getNumberOfReceipts();
				deleteListModel.removeAllElements();

				for (int i = 0; i < numberOfReceipts; i++) {
					deleteListModel.addElement(taxpayers
							.get(taxpayerSelectedIndex).getReceipt(i)
							.getReceiptID());
					deleteReceiptList.setModel(deleteListModel);
				}
				deleteReceiptWindow
						.setText(" "
								+ taxpayerInfo.get(0).getReceipt(
										taxpayerSelectedIndex));
				deleteReceiptList
						.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent e) {
								taxpayerSelectedIndex = taxpayersList
										.getSelectedIndex();
								deleteReceiptSelectedIndex = deleteReceiptList
										.getSelectedIndex();
								if (taxpayerSelectedIndex != -1) {
									deleteReceiptWindow
											.setText(" "
													+ taxpayerInfo
															.get(taxpayerSelectedIndex)
															.getReceipt(
																	deleteReceiptSelectedIndex));
								}
							}
						});
			} else if (buttonName.equals("Delete")
					&& taxpayerSelectedIndex != -1) {
				try {
					taxpayers.get(taxpayerSelectedIndex).deleteReceipt(
							deleteReceiptSelectedIndex,
							file.getFile(taxpayerSelectedIndex));
					refreshDeleteReceiptList();
				} catch (IOException e) {
					System.out.println("IO exception error");
					e.printStackTrace();
				} catch (SAXException e) {
					System.out.println("SAX exception error");
					e.printStackTrace();
				}
			}
		}

		private void refreshDeleteReceiptList() {
			int numberOfReceipts = taxpayers.get(taxpayerSelectedIndex)
					.getNumberOfReceipts();
			deleteListModel.removeAllElements();
			deleteReceiptWindow.setText(null);

			for (int i = 0; i < numberOfReceipts; i++) {
				deleteListModel.addElement(taxpayers.get(taxpayerSelectedIndex)
						.getReceipt(i).getReceiptID());
				deleteReceiptList.setModel(deleteListModel);
			}
		}
	}

	private class ReportButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String buttonName = ((JButton) event.getSource()).getText();

			if (buttonName.equals("View Report")) {
				basicTaxCheckBox.setSelected(false);
				taxAnalysisCheckBox.setSelected(false);
				totalReceiptValueCheckBox.setSelected(false);
				expensesAnalysisCheckBox.setSelected(false);
				fileExtension = null;
				totalReceiptValue = new ReceiptValue(taxpayers.get(
						taxpayerSelectedIndex).getAllReceipts());

				CardLayout cardLayout = (CardLayout) frmIncomeTaxCalculator
						.getContentPane().getLayout();
				cardLayout.show(frmIncomeTaxCalculator.getContentPane(),
						"reportOptionsPanel");

				reportListModel.addElement("Name : "
						+ taxpayerInfo.get(taxpayerSelectedIndex).getName()
						+ "\n");
				reportListModel.addElement("Afm : "
						+ taxpayerInfo.get(taxpayerSelectedIndex).getAfm()
						+ "\n");
				reportListModel.addElement("Income : "
						+ taxpayerInfo.get(taxpayerSelectedIndex).getIncome()
						+ "\n");
				reportOptionsList.setModel(reportListModel);
			} else if (buttonName.equals("Save Report")) {
				if (fileExtension == null) {
					JOptionPane.showMessageDialog(null,
							"Please choose output file format!",
							"Output file format error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					OutputFileFactory outputFileFactory = new OutputFileFactory();
					OutputFile outputFile = outputFileFactory
							.setFileType(fileExtension);
					try {
						outputFile.printReport(
								taxpayerInfo.get(taxpayerSelectedIndex)
										.getTaxpayerInfo(), printReportOptions);
					} catch (IOException message) {
						JOptionPane.showMessageDialog(null,
								"Error creating file", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	private class CheckBoxListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			Object checkBoxSource = e.getSource();
			Taxpayer selectedTaxpayer = taxpayerInfo.get(taxpayerSelectedIndex);
			String status = selectedTaxpayer.getStatus().trim();
			double totalTax = 0;
			double basicTax = 0;
			double taxIncrease = 0;
			double totalValue = totalReceiptValue.calculateTotalReceiptValue();
			switch (status) {
			case "Married":
				int[] marriedTierLimits = {36080, 90000, 143350, 254240, 0};
				int[] marriedPreviousTierLimits = {0, 36080, 90000, 143350, 254240};
				double[] marriedPercentages = {0.0535,0.0705 ,0.0705, 0.0785, 0.0985};
				double[] marriedBaseTax = {0, 1930.28, 5731.64, 9492.82, 18197.69};
				TaxCalculation marriedTaxpayer = new TaxCalculation(
						selectedTaxpayer.getIncome(), totalValue, marriedTierLimits, marriedPreviousTierLimits, marriedPercentages, marriedBaseTax);
				totalTax = marriedTaxpayer
						.calculateFirstTierAddedOrSubtractedTax();
				basicTax = marriedTaxpayer.getTax(0);
				taxIncrease = marriedTaxpayer.getAddedOrSubtractedTax();
				break;

			case "Single":
				int[] singleTierLimits = {24680, 81080, 90000, 152540, 0};
				int[] singlePreviousTierLimits = {0, 24680, 81080, 90000, 152540};
				double[] singlePercentages = {0.0535, 0.0705, 0.0785, 0.0785, 0.0985};
				double[] singleBaseTax = {0, 1320.38, 5296.58, 5996.80, 10906.19};
				TaxCalculation singleTaxpayer = new TaxCalculation(
						selectedTaxpayer.getIncome(), totalValue, singleTierLimits, singlePreviousTierLimits, singlePercentages, singleBaseTax);
				totalTax = singleTaxpayer
						.calculateFirstTierAddedOrSubtractedTax();
				basicTax = singleTaxpayer.getTax(0);
				taxIncrease = singleTaxpayer.getAddedOrSubtractedTax();
				break;

			case "Head of household":
				int[] headOfHouseholdTierLimits = {30390, 90000, 122110, 203390, 0};
				int[] headOfHouseholdPreviousTierLimits = {0, 30390, 90000, 122110, 203390};
				double[] headOfHouseholdPercentages = {0.0535,0.0705 ,0.0705, 0.0785, 0.0985};
				double[] headOfHouseholdBaseTax = {0, 1625.87, 5828.38, 8092.13, 14472.61};
				TaxCalculation headOfHouseholdTaxpayer = new TaxCalculation(
						selectedTaxpayer.getIncome(), totalValue, headOfHouseholdTierLimits, headOfHouseholdPreviousTierLimits, headOfHouseholdPercentages, headOfHouseholdBaseTax);
				
				totalTax = headOfHouseholdTaxpayer
						.calculateFirstTierAddedOrSubtractedTax();
				basicTax = headOfHouseholdTaxpayer.getTax(0);
				taxIncrease = headOfHouseholdTaxpayer.getAddedOrSubtractedTax();
				break;
			}

			if (checkBoxSource == basicTaxCheckBox) {
				if (basicTaxCheckBox.isSelected()) {
					reportListModel
							.addElement("Total Tax : " + totalTax + "\n");
					printReportOptions.add("Total Tax : " + totalTax);
				} else {
					reportListModel.removeElement("Total Tax : " + totalTax
							+ "\n");
					printReportOptions.remove("Total Tax : " + totalTax);
				}
			} else if (checkBoxSource == taxAnalysisCheckBox) {
				if (taxAnalysisCheckBox.isSelected()) {
					reportListModel.addElement("Basic Tax : " + basicTax);
					printReportOptions.add("Basic Tax : " + basicTax);

					if (taxIncrease > 0) {
						reportListModel.addElement("Tax Increase :"
								+ taxIncrease + "\n");
						printReportOptions.add("Tax Increase :" + taxIncrease);
					} else {

						reportListModel.addElement("Tax Decrease :"
								+ Math.abs(taxIncrease) + "\n");
						printReportOptions.add("Tax Decrease :"
								+ Math.abs(taxIncrease));
					}
				} else {
					reportListModel.removeElement("Basic Tax : " + basicTax);
					printReportOptions.remove("Basic Tax : " + basicTax);
					if (taxIncrease > 0) {

						reportListModel.removeElement("Tax Increase :"
								+ taxIncrease + "\n");
						printReportOptions.remove("Tax Increase :"
								+ taxIncrease);
					} else {
						reportListModel.removeElement("Tax Decrease :"
								+ Math.abs(taxIncrease) + "\n");
						printReportOptions.remove("Tax Decrease :"
								+ Math.abs(taxIncrease));
					}
				}
			} else if (checkBoxSource == totalReceiptValueCheckBox) {
				if (totalReceiptValueCheckBox.isSelected()) {
					reportListModel.addElement("Receipts : " + totalValue
							+ "\n");
					printReportOptions.add("Receipts : " + totalValue);
				} else {
					reportListModel.removeElement("Receipts : " + totalValue
							+ "\n");
					printReportOptions.remove("Receipts : " + totalValue);
				}
			} else if (checkBoxSource == expensesAnalysisCheckBox) {
				if (expensesAnalysisCheckBox.isSelected()) {
					reportListModel
							.addElement("Basic : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Basic")
									+ "\n");
					reportListModel
							.addElement("Entertainment : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Entertainment")
									+ "\n");
					reportListModel
							.addElement("Health : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Health")
									+ "\n");
					reportListModel
							.addElement("Other : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Other")
									+ "\n");
					reportListModel
							.addElement("Travel : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Travel")
									+ "\n");
					printReportOptions
							.add("Basic : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Basic"));
					printReportOptions
							.add("Entertainment : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Entertainment"));
					printReportOptions
							.add("Health : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Health"));
					printReportOptions
							.add("Other : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Other"));
					printReportOptions
							.add("Travel : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Travel"));
				} else {
					reportListModel
							.removeElement("Basic : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Basic")
									+ "\n");
					reportListModel
							.removeElement("Entertainment : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Entertainment")
									+ "\n");
					reportListModel
							.removeElement("Health : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Health")
									+ "\n");
					reportListModel
							.removeElement("Other : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Other")
									+ "\n");
					reportListModel
							.removeElement("Travel : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Travel")
									+ "\n");
					printReportOptions
							.remove("Basic : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Basic"));
					printReportOptions
							.remove("Entertainment : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Entertainment"));
					printReportOptions
							.remove("Health : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Health"));
					printReportOptions
							.remove("Other : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Other"));
					printReportOptions
							.remove("Travel : "
									+ totalReceiptValue
											.calculateTotalReceiptAmountPerKind("Travel"));
				}
			}
		}
	}

	private class radioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object radioButtonSource = e.getSource();

			if (radioButtonSource == txtOutputFormatButton) {
				toggleSelection(radioButtonSource);
				fileExtension = "txt";
			} else if (radioButtonSource == xmlOutputFormatButton) {
				toggleSelection(radioButtonSource);
				fileExtension = "xml";
			}
		}

		private void toggleSelection(Object radioButtonSource) {
			if (radioButtonSource == txtOutputFormatButton
					&& xmlOutputFormatButton.isSelected()) {
				xmlOutputFormatButton.setSelected(false);
			} else if (radioButtonSource == xmlOutputFormatButton
					&& txtOutputFormatButton.isSelected()) {
				txtOutputFormatButton.setSelected(false);
			}
		}
	}

	private class CalendarActionListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			if ("date".equals(e.getPropertyName())) {
				date = sdf.format(e.getNewValue()).toString();
			}
		}
	}
}
