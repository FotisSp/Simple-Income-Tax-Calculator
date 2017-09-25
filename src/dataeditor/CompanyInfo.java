package dataeditor;

public class CompanyInfo {
	private String company;
	private String country;
	private String city;
	private String street;
	private String number;

	public CompanyInfo(String company, String country, String city,
			String street, String number) {
		this.company = company;
		this.country = country;
		this.city = city;
		this.street = street;
		this.number = number;
	}

	public String getCompany() {
		return company;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String toString() {
		return "\r\nCompany    : " + company + "\r\nCountry    : " + country
				+ "\r\nCity       : " + city + "\r\nStreet     : " + street
				+ "\r\nNumber     : " + number + "\r\n\r\n";
	}
}