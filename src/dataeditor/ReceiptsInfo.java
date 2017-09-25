package dataeditor;

public class ReceiptsInfo {
	private int id;
	private String date;
	private String kind;
	private double amount;
	private CompanyInfo companyInfo;

	public ReceiptsInfo(int id, String date, String kind, double amount,
			CompanyInfo companyInfo) {
		this.id = id;
		this.date = date;
		this.kind = kind;
		this.amount = amount;
		this.companyInfo = companyInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public String getKind() {
		return kind;
	}

	public double getAmount() {
		return amount;
	}

	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public String getCompany() {
		return companyInfo.getCompany();
	}

	public String getCountry() {
		return companyInfo.getCountry();
	}

	public String getCity() {
		return companyInfo.getCity();
	}

	public String getStreet() {
		return companyInfo.getStreet();
	}

	public String getNumber() {
		return companyInfo.getNumber();
	}

	public void decreaseId() {
		this.id = id - 1;
	}

	public String getReceiptID() {
		return "Receipt ID : " + id;
	}

	public double getReceiptAmount() {
		return amount;
	}

	public String toString() {
		return "Receipt ID : " + id + "\r\nDate       : " + date
				+ "\r\nKind       : " + kind + "\r\nAmount     : " + amount
				+ companyInfo;
	}
}
