package outputsystem;

import java.io.*;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.xml.sax.SAXException;
import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;

public interface RefreshFile {
	boolean addReceiptToFile(ReceiptsInfo newReceipt) throws IOException,
			ParserConfigurationException, SAXException,
			TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException;

	void deleteReceiptFromFile(HashMap<Integer, ReceiptsInfo> receipts,
			TaxpayerInfo taxpayer) throws IOException, SAXException;

}