package dataeditor;

public class TaxCalculation {
	private static final int PERCENT = 100;
	private static final int FIRSTTIER = 20;
	private static final int SECONDTIER = 40;
	private static final int THIRDTIER = 60;
	private static final double TAXINCREASE = 0.05;
	private static final double TAXDECREASETIER1 = 0.05;
	private static final double TAXDECREASETIER2 = 0.1;
	private static final double TAXDECREASETIER3 = 0.15;
	private static final int TIERLIMIT = 4;
	private double addedOrSubtractedTax;
	private double income;
	private double totalReceiptValue;
	private int[] tierLimits;
	private int[] previousTierLimits;
	private double[] percentages;
	private double[] baseTax;

	public TaxCalculation(double income, double totalReceiptValue,
			int[] tierLimits, int[] previousTierLimits, double[] percentages,
			double[] baseTax) {
		this.income = income;
		this.totalReceiptValue = totalReceiptValue;
		this.tierLimits = tierLimits;
		this.percentages = percentages;
		this.baseTax = baseTax;
		this.previousTierLimits = previousTierLimits;
	}

	public double getTax(int key) {
		if (income < tierLimits[key] || key == TIERLIMIT) {
			return baseTax[key] + percentages[key]
					* (income - previousTierLimits[key]);
		} else {
			return getTax(++key);
		}
	}

	public double getTotalReceiptValue() {
		return totalReceiptValue;
	}

	public double getIncome() {
		return income;
	}

	public double calculateFirstTierAddedOrSubtractedTax() {
		if (receiptsIncomePercent() < FIRSTTIER) {
			addedOrSubtractedTax = getTax(0) * TAXINCREASE;
		} else {
			calculateSecondTierAddedOrSubtractedTax();
		}
		return calculateTotalTax();
	}

	private void calculateSecondTierAddedOrSubtractedTax() {
		if (receiptsIncomePercent() < SECONDTIER) {
			addedOrSubtractedTax = -(getTax(0) * TAXDECREASETIER1);
		} else {
			calculateThirdTierAddedOrSubtractedTax();
		}
	}

	private void calculateThirdTierAddedOrSubtractedTax() {
		if (receiptsIncomePercent() < THIRDTIER) {
			addedOrSubtractedTax = -(getTax(0) * TAXDECREASETIER2);
		} else {
			calculateLastTierAddedOrSubtractedTax();
		}
	}

	private void calculateLastTierAddedOrSubtractedTax() {
		addedOrSubtractedTax = -(getTax(0) * TAXDECREASETIER3);
	}

	private double calculateTotalTax() {
		final double totalTax = getTax(0) + addedOrSubtractedTax;
		return (double) Math.round(totalTax * PERCENT) / PERCENT;
	}

	private double receiptsIncomePercent() {
		return getTotalReceiptValue() / getIncome() * PERCENT;
	}

	public double getAddedOrSubtractedTax() {
		return addedOrSubtractedTax;
	}
}
