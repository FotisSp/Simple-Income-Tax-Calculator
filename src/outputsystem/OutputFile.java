package outputsystem;

import java.io.IOException;
import java.util.ArrayList;
import dataeditor.TaxpayerInfo;

public interface OutputFile {

	void printReport(TaxpayerInfo taxpayer, ArrayList<String> selectedTaxes)
			throws IOException;
}
