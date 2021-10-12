package Enums;

public enum EPrivacy {
	
	PRIVATE("pr"), PROTECTED("prot"), PUBLIC("pu"), GLOBAL("gl");

	private String abbreviation;

	private EPrivacy(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbv() {
		return abbreviation;
	}

	public String toText() {
		return this.toString().toLowerCase();
	}

}
