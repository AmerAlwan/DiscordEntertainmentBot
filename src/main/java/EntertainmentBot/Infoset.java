package EntertainmentBot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import Enums.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Infoset {

	private boolean isActive = true;
	private HashMap<EFilter, String> info = new HashMap<>();
	private MessageReceivedEvent event;
	private String mode;

	public Infoset(MessageReceivedEvent event, String[] args, String mode) {
		
		this.mode = mode;

		this.event = event;

		if (args.length < 2) {
			event.getChannel().sendMessage("Please enter name of media!").queue();
			deactivate();
		}

		for (EFilter f : EFilter.values()) {
			info.put(f, "");
		}

		EFilter filter = EFilter.NAME;
		info.put(filter, "");
		for (int i = 2; i < args.length; i++) {
			for (EFilter e : EFilter.values()) {
				if (args[i].contains(":")) {
					String arg = args[i].substring(0, args[i].indexOf(":"));
					if (arg.equals(e.toText()) || arg.equals(e.getAbv())) {
						filter = e;
					}
				}
			}
			info.put(filter,
					info.get(filter) + args[i].replaceAll(filter.toText1(), "").replaceAll(filter.getAbv1(), "")
							+ (filter == EFilter.NAME ? " " : ""));
		}

		evaluateErrors();

		if (info.get(EFilter.TYPE).equals("")) {
			info.put(EFilter.TYPE, "all");
		}
		
		if (info.get(EFilter.PRIVACY).equals("")) {
			info.put(EFilter.PRIVACY, "prot");
		}

	}

	public Infoset() {
		// TODO Auto-generated constructor stub
	}

	private void evaluateErrors() {
		EFilter filter = EFilter.TYPE;
		if (!info.get(filter).equals("")) {
			boolean active = Arrays.stream(EType.values())
					.anyMatch((a) -> info.get(filter).equals(a.toText()) || info.get(filter).equals(a.getAbv()));
			if (!active) {
				deactivate();
				error("Entertainment type '" + info.get(filter) + "' is unknown for type filter");
				return;
			}
		}

		EFilter filter1 = EFilter.PRIVACY;
		if (!info.get(filter1).equals("")) {
			boolean active = Arrays.stream(EPrivacy.values())
					.anyMatch(a -> info.get(filter1).equals(a.toText()) || info.get(filter1).equals(a.getAbv()));
			if (!active) {
				deactivate();
				error("Privacy type '" + info.get(filter1) + "' is unknown for privacy filter");
				return;
			}
		}

		EFilter filter2 = EFilter.SORTBY;
		if (!info.get(filter2).equals("")) {
			boolean active = Arrays.stream(ESort.values())
					.anyMatch(a -> info.get(filter2).equals(a.toText()) || info.get(filter2).equals(a.getAbv()));
			if (!active) {
				deactivate();
				error("Sort type '" + info.get(filter2) + "' is unknown for sort by filter");
				return;
			}
		}

		if (!info.get(EFilter.PAGE).equals("")) {
			if (!checkNumber(EFilter.PAGE, 1, 100))
				return;
		}

		if (!info.get(EFilter.SEASON).equals("")) {
			if (!checkNumber(EFilter.SEASON, 1, 100))
				return;
		}

		if (!info.get(EFilter.EPISODE).equals("")) {
			if (!checkNumber(EFilter.EPISODE, 1, 1000))
				return;
		}

		if (!info.get(EFilter.NUM).equals("")) {
			if (!checkNumber(EFilter.NUM, 1, 1000))
				return;
		}

		if (!info.get(EFilter.DATE).equals("")) {
			if (!checkNumber(EFilter.DATE, 1900, 2100))
				return;
		}

		if (!info.get(EFilter.CHAPTER).equals("")) {
			if (!checkNumber(EFilter.CHAPTER, 1, 10000))
				return;
		}
	}

	private boolean checkNumber(EFilter numFilter, int min, int max) {
		int num = getInt(info.get(numFilter));
		if (num < min || num > max) {
			deactivate();
			error(numFilter.toText1() + " number must be between " + min + " and " + max);
			return false;
		} else {
			return true;
		}
	}

	private int getInt(String text0) {
		// System.out.println("Before Check: " + text0);
		if (text0.trim().matches("[0-9]+")) {
			// System.out.println("Check: " + text0);
			return Integer.valueOf(text0);
		}
		return -1;
	}

	private void error(String error) {
		event.getChannel().sendMessage(error).queue();
	}

	private void deactivate() {
		this.isActive = false;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void displayValues() {
		for (EFilter e : info.keySet()) {
			System.out.println(e.toText() + " " + info.get(e));
		}
	}

	public boolean isType(EType type) {
		return (info.get(EFilter.TYPE).equals(type.toText())) ? true
				: (info.get(EFilter.TYPE).equals(type.getAbv())) ? true : false;
	}

	public EType getEType(String type) {
		for (EType e : EType.values()) {
			if (type.equals(e.toText()) || type.equals(e.getAbv())) {
				return e;
			}
		}

		return null;
	}

	public ETypes getETypes(String type) {
		for (ETypes e : ETypes.values()) {
			if (e.containsEType(getEType(type))) {
				return e;
			}
		}

		return null;
	}

	public ETypes getETypes() {
		for (ETypes e : ETypes.values()) {
			if (e.containsEType(getEType(info.get(EFilter.TYPE)))) {
				return e;
			}
		}

		return null;
	}
	
	public boolean isModeSearch() {
		return mode.equals("search");
	}
	
	public boolean isModeInfo() {
		return mode.equals("info");
	}
	
	public boolean isModeInfoPlus() {
		return mode.equals("infoplus");
	}
	
	

	public void setType(EType type) {
		info.put(EFilter.TYPE, type.getAbv());
	}

	public boolean isMovie() {
		return isType(EType.MOVIE);
	}

	public boolean isTV() {
		return isType(EType.TELEVISIONSHOW);
	}

	public boolean isMTV() {
		return isType(EType.MOVIETELEVISIONSHOW);
	}

	public boolean isGame() {
		return isType(EType.GAME);
	}

	public boolean isMusic() {
		return isType(EType.MUSIC);
	}

	public boolean isRadio() {
		return isType(EType.RADIO);
	}

	public boolean isBook() {
		return isType(EType.BOOK);
	}

	public boolean isAll() {
		return isType(EType.ALL);
	}

	public boolean isNone() {
		return isType(EType.NONE);
	}

	public boolean isAnime() {
		return isType(EType.ANIME);
	}

	public boolean isAnimeMovie() {
		return isType(EType.ANIMEMOVIE);
	}

	public boolean isAnimeTV() {
		return isType(EType.ANIMETV);
	}

	public boolean isAnimeMusic() {
		return isType(EType.ANIMEMUSIC);
	}

	public boolean isAnimeOVA() {
		return isType(EType.ANIMEOVA);
	}

	public boolean isAnimeSpecial() {
		return isType(EType.ANIMESPECIAL);
	}

	public boolean isLightNovel() {
		return isType(EType.LIGHTNOVEL);
	}

	public boolean isManga() {
		return isType(EType.MANGA);
	}

	public boolean isMangaManga() {
		return isType(EType.MANGAMANGA);
	}

	public boolean isMangaOneshot() {
		return isType(EType.MANGAONESHOT);
	}

	public boolean isMangaDoujin() {
		return isType(EType.MANGADOUJIN);
	}

	public boolean isMangaManhwa() {
		return isType(EType.MANGAMANHWA);
	}

	public boolean isMangaManhua() {
		return isType(EType.MANGAMANHUA);
	}

	public String getName() {
		return info.get(EFilter.NAME);
	}

	public String getDate() {
		return info.get(EFilter.DATE);
	}

	public String getType() {
		return info.get(EFilter.TYPE);
	}

	public String getNum() {
		return info.get(EFilter.NUM);
	}

	public String getSortBy() {
		return info.get(EFilter.SORTBY);
	}

	public String getSeason() {
		return info.get(EFilter.SEASON);
	}

	public String getEpisode() {
		return info.get(EFilter.EPISODE);
	}

	public String getChapter() {
		return info.get(EFilter.CHAPTER);
	}

	public String getLastSearch() {
		return info.get(EFilter.LASTSEARCH);
	}

	public String getMusicArtist() {
		return info.get(EFilter.MUSICARTIST);
	}

	public String getPrivacy() {
		return info.get(EFilter.PRIVACY);
	}

	public String getPage() {
		return info.get(EFilter.PAGE);
	}

}