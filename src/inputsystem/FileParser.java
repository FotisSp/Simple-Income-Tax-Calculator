package inputsystem;

import java.io.File;
import java.util.HashMap;
import dataeditor.ReceiptsInfo;
import dataeditor.TaxpayerInfo;

public interface FileParser {
	void parse();

	HashMap<Integer, ReceiptsInfo> getReceipts();

	TaxpayerInfo getTaxpayerInfo();

	void openFile(File fileInput);
}
