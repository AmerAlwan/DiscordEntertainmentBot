package Enums;

public enum EFilter {

	PAGE("pg"), DATE("d"), EPISODE("ep"), NUM("n"), TYPE("ty"), SEASON("sn"), CHAPTER("ch"), SORTBY("sb"), LASTSEARCH(
			"ls"), MUSICARTIST("muar"), PRIVACY("p"), NAME("na");

	private String abbreviation;

	private EFilter(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbv() {
		return abbreviation;
	}
	
	public String getAbv1() {
		return abbreviation + ":";
	}

	public String toText() {
		return this.toString().toLowerCase();
	}
	
	public String toText1() {
		return this.toString().toLowerCase() + ":";
	}

}
