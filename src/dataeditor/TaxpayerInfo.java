package dataeditor;

public class TaxpayerInfo {
	private String name;
	private int afm;
	private String status;
	private int income;

	public TaxpayerInfo(String name, int afm, String status, int income) {
		this.name = name;
		this.afm = afm;
		this.status = status;
		this.income = income;
	}

	public String getName() {
		return name;
	}

	public Integer getAfm() {
		return afm;
	}

	public String getStatus() {
		return status;
	}

	public Integer getIncome() {
		return income;
	}
}
