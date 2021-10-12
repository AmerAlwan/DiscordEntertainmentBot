package Enums;

public enum ETypes {
	//
	// ALL(new EType[] { EType.ALL, EType.TELEVISIONSHOW,
	// EType.MOVIETELEVISIONSHOW, EType.ANIME, EType.ANIMEMOVIE,
	// EType.ANIMEMUSIC, EType.ANIMEOVA, EType.ANIMESPECIAL, EType.ANIMETV,
	// EType.ANIMEONA, EType.MANGA,
	// EType.MANGADOUJIN, EType.MANGAMANGA, EType.MANGAMANHUA,
	// EType.MANGAMANHWA, EType.MANGAONESHOT, EType.MUSIC,
	// EType.GAME, EType.BOOK, EType.RADIO }),
	ALL(new EType[] {EType.ALL}), MOVIETV(new EType[] { EType.MOVIE, EType.TELEVISIONSHOW, EType.MOVIETELEVISIONSHOW }), ANIMEMANGA(
			new EType[] { EType.ANIME, EType.ANIMEMOVIE, EType.ANIMEMUSIC, EType.ANIMEOVA, EType.ANIMESPECIAL,
					EType.ANIMETV, EType.ANIMEONA, EType.MANGA, EType.MANGADOUJIN, EType.MANGAMANGA, EType.MANGAMANHUA,
					EType.MANGAMANHWA, EType.MANGAONESHOT }), MUSIC(new EType[] { EType.MUSIC }), GAME(
							new EType[] { EType.GAME }), RADIO(
									new EType[] { EType.RADIO }), BOOK(new EType[] { EType.BOOK });

	EType[] types;

	private ETypes(EType[] types) {
		this.types = types;
	}

	public boolean containsEType(EType type) {
		for (EType e : types) {
			if (e == type) {
				return true;
			}
		}
		return false;
	}

}
