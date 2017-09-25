package dataeditor;

import java.util.HashMap;

public class ReceiptValue {
	private HashMap<Integer, ReceiptsInfo> receipts;

	public ReceiptValue(HashMap<Integer, ReceiptsInfo> receipts) {
		this.receipts = receipts;
	}

	public double calculateTotalReceiptValue() {
		double totalAmount = 0;

		for (int i = 0; i < receipts.size(); i++) {
			totalAmount += getReceiptAmount(i);
		}
		return totalAmount;
	}

	private double getReceiptAmount(int key) {
		return receipts.get(key).getReceiptAmount();
	}

	private String getReceiptKind(int key) {
		return receipts.get(key).getKind();
	}

	public double calculateTotalReceiptAmountPerKind(String kind) {
		double totalAmount = 0;

		for (int i = 0; i < receipts.size(); i++) {
			if (kind.equals(getReceiptKind(i))) {
				totalAmount += getReceiptAmount(i);
			}
		}
		return totalAmount;
	}
}
