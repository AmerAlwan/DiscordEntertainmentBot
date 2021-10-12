package Enums;

public enum EType {
	MOVIE("m"), TELEVISIONSHOW("tv"), RADIO("r"), GAME("g"), MUSIC("mu"), ANIME("an"), MANGA("mg"), MOVIETELEVISIONSHOW(
			"mtv"), BOOK("b"), ANIMEMOVIE("anm"), ANIMETV("antv"), ALL("a"), MANGAMANGA("mgmg"), ANIMEOVA(
					"anov"), ANIMEONA("anon"), ANIMESPECIAL("ansp"), LIGHTNOVEL("ln"), ANIMEMUSIC("anmu"), MANGAONESHOT(
							"mgos"), MANGADOUJIN("mgdo"), MANGAMANHWA("mgmwa"), MANGAMANHUA("mgmua"), NONE("n");
	// m, tv, r, g, mu, an, mg, mtv, b, anm, antv, a, mgmg, anov, ansp, ln,
	// anon, anmu, mgos, mgdo, mgmwa, mgmua;

	private String abbreviation;

	private EType(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbv() {
		return abbreviation;
	}

	public String toText() {
		return this.toString().toLowerCase();
	}

}
