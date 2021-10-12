package EntertainmentBot;

import java.util.Arrays;
import java.util.HashMap;

import com.sun.xml.internal.ws.wsdl.writer.document.Types;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class InfosetCopy {

	private boolean active = true;
	private HashMap<String, String> info = new HashMap<>();

	public InfosetCopy(MessageReceivedEvent event, String arg0) {
		this(event, arg0.split(" "));
	}

	public InfosetCopy(MessageReceivedEvent event, String[] args0) {

		String[] args = args0;
		// String[] arg = arg0.split(",");
		info.put("name", "");
		info.put("date", "");
		info.put("type", "");
		info.put("season", "");
		info.put("episode", "");
		info.put("chapter", "");
		info.put("num", "");
		info.put("sortby", "");
		info.put("lastsearch", "");
		info.put("page", "");
		info.put("musicartist", "");
		info.put("privacy", "");
		String[][] filters = { { "pg:", "page:" }, { "d:", "date:" }, { "ty:", "type:" }, { "sn:", "season:" },
				{ "ep:", "episode:" }, { "n:", "num:" }, { "ch:", "chapter:" }, { "sb:", "sortby:" },
				{ "ls:", "lastsearch:" }, { "muar:", "musicartist" }, { "p:", "privacy:" } };
		HashMap<String, String> type = new HashMap<>();
		type.put("movie", "m");
		type.put("televisionshow", "tv");
		type.put("movietelevisionshow", "mtv");
		type.put("book", "b");
		type.put("game", "g");
		type.put("animemovie", "anm");
		type.put("animetv", "antv");
		type.put("all", "a");
		type.put("manga", "mg");
		type.put("animeonda", "anon");
		type.put("animemusic", "anmu");
		type.put("mangaoneshot", "mgos");
		type.put("mangadoujin", "mgdo");
		type.put("mangamanhwa", "mgmwa");
		type.put("mangamanhua", "mgmua");
		type.put("mangamanga", "mgmg");
		type.put("animeova", "anov");
		type.put("animespecial", "ansp");
		type.put("music", "mu");
		type.put("lightnovel", "ln");
		type.put("anime", "an");
		HashMap<String, String> sortby = new HashMap<>();
		sortby.put("alphaascending", "aa");
		sortby.put("alphadescending", "ad");
		sortby.put("dateascending", "da");
		sortby.put("datedescending", "dd");
		sortby.put("rateascending", "ra");
		sortby.put("ratedescending", "rd");
		HashMap<String, String> privacies = new HashMap<>();
		privacies.put("", "prot");
		privacies.put("private", "pr");
		privacies.put("protected", "prot");
		privacies.put("public", "pu");
		privacies.put("global", "gl");

		String addModifier = "name";
		if (args.length < 2) {
			event.getChannel().sendMessage("Please enter name of media!").queue();
		}

		for (int i = 2; i < args.length; i++) {
			String c = args[i];
			String arg = null;
			String[] f = null;
			for (String[] ff : filters) {
				arg = Arrays.stream(ff).parallel().filter(x -> c.trim().contains(x)).findFirst().orElse(null);
				if (arg != null) {
					f = ff;
					break;
				}
				// System.out.println("arg: " + arg);
			}

			if (arg != null && info.get("name").equals("")) {
				event.getChannel().sendMessage("Please put name first").queue();
				active = false;
			} else if (arg != null) {

				if (arg.contains(f[0]) || arg.contains(f[1])) {
					addModifier = f[1].replaceAll(":", "");
					String value = args[i].replaceAll(f[0], "").replaceAll(f[1], "").trim();
					if (f[1].equals("type:")) {
						if (type.containsKey(value) || type.containsValue(value)) {
							if (type.containsKey(value)) {
								value = type.get(value);
							}
						} else {
							active = false;
							event.getChannel().sendMessage("Unknown type for type").queue();
							break;
						}
					}
					if (f[1].equals("sortby:")) {
						if (sortby.containsKey(value) || sortby.containsValue(value)) {
							if (sortby.containsKey(value)) {
								value = sortby.get(value);
							}
						} else {
							active = false;
							event.getChannel().sendMessage("Unknown type for sortby").queue();
							break;
						}
					}
					
					if (f[1].equals("privacy:")) {
						if (privacies.containsKey(value) || privacies.containsValue(value)) {
							if (privacies.containsKey(value)) {
								value = privacies.get(value);
							}
						} else {
							active = false;
							event.getChannel().sendMessage("Unknown type for privacy").queue();
							break;
						}
					}
					info.put(addModifier, value);
				} else {
					info.put(addModifier, (info.get(addModifier) + args[i]).trim() + " ");
				}
			} else {
				info.put(addModifier, (info.get(addModifier) + args[i]).trim() + " ");
			}

		}

		if (active) {
			boolean exit = false;
			for (String s : info.keySet()) {
				info.put(s, info.get(s).trim());
				for (String[] ff : filters) {
					String arg = null;
					arg = Arrays.stream(ff).parallel().filter(x -> info.get(s).trim().contains(x)).findFirst()
							.orElse(null);
					if (arg != null) {
						active = false;
						exit = true;
						event.getChannel().sendMessage("Incorrect Syntax!").queue();
						break;
					}
				}
				if (exit) {
					break;
				}
			}
			if (!exit) {
				String error = getErrorMessage();
				if (error != null) {
					active = false;
					event.getChannel().sendMessage(error).queue();
				}
			}
		}

	}

	public String getErrorMessage() {
		String wrong = "";

		String[] typeFilter = { "m", "movie", "b", "book", "tv", "televisionshow", "mtv", "movietelevisionshow", "a",
				"all", "antv", "animetv", "anm", "animemovie", "mg", "manga", "mgmg", "mangamanga", "mu", "music",
				"anov", "animeova", "ansp", "animespecial", "ln", "lightnovel", "an", "anime", "g", "game", "muar",
				"musicartist", "mual", "musicalbum", "anon", "animeona", "anmu", "animemusic", "mgos", "mangaoneshot", "mgdo", "mangadoujin", "mgmwa", "mangamanhwa", "mgmua", "mangamanhua" };
		boolean cont = false;
		for (String s : typeFilter) {
			if (info.get("type").equals("")) {
				info.put("type", "a");
				cont = true;
				break;
			}
			if (info.get("type").equals(s)) {
				cont = true;
			}
		}
		if (!cont) {
			wrong += "type";
			active = false;
		}
		cont = false;

		String[] sortFilter = { "", null, "da", "dateascending", "dd", "datedescending", "ta", "typeascending", "td",
				"typedescending", "va", "voteascending", "vd", "votedescending", "na", "nameascending", "nd",
				"namedescending" };
		for (String s : sortFilter) {
			if (info.get("sortby").equals("")) {
				cont = true;
				break;
			}
			if (info.get("sortby").equals(s)) {
				cont = true;
			}
		}
		if (!cont) {
			if (!wrong.isEmpty()) {
				wrong += ", ";
			}
			wrong += "sort";
			active = false;
		}
		cont = false;

		if (info.get("date").equals("") || info.get("date").equals(null)) {
			cont = true;
		}
		if (getInt(info.get("date")) >= 1900) {
			cont = true;
		}

		if (!cont) {
			if (!wrong.isEmpty()) {
				wrong += ", ";
			}
			wrong += "date";
			active = false;
		}

		for (String s : new String[] { "season", "episode", "chapter" }) {
			cont = false;
			if (info.get(s).equals("")) {
				cont = true;
			}
			if (getInt(info.get(s)) >= 0) {
				cont = true;
			}
			if (!cont) {
				if (!wrong.isEmpty()) {
					wrong += ", ";
				}
				wrong += s;
				active = false;
			}

		}

		cont = false;

		if (info.get("privacy").equals("") || info.get("privacy").equals(null)) {
			info.put("privacy", "prot");
			cont = true;
		}
		if (info.get("privacy").equals("public") || info.get("privacy").equals("pu")
				|| info.get("privacy").equals("private") || info.get("privacy").equals("pr")
				|| info.get("privacy").equals("global") || info.get("privacy").equals("gl")
				|| info.get("privacy").equals("protected") || info.get("privacy").equals("prot")) {
			cont = true;
		}

		if (!cont) {
			if (!wrong.isEmpty()) {
				wrong += ", ";
			}
			wrong += "privacy";
			active = false;
		}

		if (wrong.equals("")) {
			return null;
		}
		return "Incorrect syntax for " + wrong;

	}

	public int getInt(String text0) {
		// System.out.println("Before Check: " + text0);
		if (text0.trim().matches("[0-9]+")) {
			System.out.println("Check: " + text0);
			return Integer.valueOf(text0);
		}
		return -1;
	}

	public boolean isActive() {
		return active;
	}

	public void displayValues() {
		System.out.println("======================================");
		for (String s : info.keySet()) {
			System.out.println(s + ": " + info.get(s));
		}
		System.out.println("======================================");
	}

	public HashMap<String, String> getInfo() {
		return info;
	}

	public String getName() {
		return info.get("name");
	}

	public String getDate() {
		return info.get("date");
	}

	public String getPage() {
		return info.get("page");
	}

	public String getType() {
		return info.get("type");
	}

	public String getMusicArtist() {
		return info.get("musicartist");
	}

	public String getSeason() {
		return info.get("season");
	}

	public String getChapter() {
		return info.get("chapter");
	}

	public String getNum() {
		return info.get("num");
	}
	
	public String getPrivacy() {
		return info.get("privacy");
	}

	public String getSortBy() {
		return info.get("sortby");
	}

	public String getLastSearch() {
		return info.get("lastsearch");
	}

	public void setType(String type) {
		info.put("type", type);
	}

	public boolean isAll() {
		return info.get("type").equals("a");
	}

	public boolean isMovie() {
		return info.get("type").equals("m");
	}

	public boolean isTV() {
		return info.get("type").equals("tv");
	}

	public boolean isMTV() {
		return info.get("type").equals("mtv");
	}

	public boolean isGame() {
		return info.get("type").equals("g");
	}

	public boolean isLightNovel() {
		return info.get("type").equals("ln");
	}

	public boolean isAnime() {
		return info.get("type").equals("an");
	}

	public boolean isAnimeTV() {
		return info.get("type").equals("antv");
	}

	public boolean isAnimeMovie() {
		return info.get("type").equals("anm");
	}

	public boolean isManga() {
		return info.get("type").equals("mg");
	}

	public boolean isMangaManga() {
		return info.get("type").equals("mgmg");
	}

	public boolean isMusic() {
		return info.get("type").equals("mu");
	}

	public boolean isBook() {
		return info.get("type").equals("b");
	}

	public boolean isOVA() {
		return info.get("type").equals("anov");
	}

	public boolean isSpecial() {
		return info.get("type").equals("ansp");
	}
	
	public boolean isONA() {
		return info.get("type").equals("anon");
	}
	public boolean isAnimeMusic() {
		return info.get("type").equals("anmu");
	}
	public boolean isOneshot() {
		return info.get("type").equals("mgos");
	}
	public boolean isDoujin() {
		return info.get("type").equals("mgdo");
	}
	public boolean isManhwa() {
		return info.get("type").equals("mgmwa");
	}
	public boolean isManhua() {
		return info.get("type").equals("mgmua");
	}
	
	
	
	

}
