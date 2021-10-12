package Enums;

public enum ESort {

	ALPHAASCENDING("aa"), ALPHADESCENDING("ad"), DATEASCENDING("da"), DATEDESCENDING("dd"), RATEASCENDING(
			"ra"), RATEDESCENDING("rd");

	private String abbreviation;

	private ESort(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbv() {
		return abbreviation;
	}

	public String toText() {
		return this.toString().toLowerCase();
	}

}
