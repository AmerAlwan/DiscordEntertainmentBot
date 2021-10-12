package EntertainmentBot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class Entertainment {

	protected String name, date, year, description, poster_path, pureid, id, type, vote_avg, popularity, vote_count,
			country, status, end_date, seasons, episodes, chapters, volumes, url, duration, thumbnail;
	protected String[] genres, productionCompanies;
	protected JSONObject media;

	public Entertainment() {

	}

	public Entertainment(JSONObject object) {

	}

	public Entertainment(String name, String date, String type, String pureid) {
		this.name = name;
		year = "";
		System.out.println("Date: " + date);
		if (date != null && date.length() >= 4) {
			year = date.substring(0, 4);
		}
		// this.year = year;
		this.type = type;
		this.id = type + pureid;
		this.pureid = id;
	}

	public Entertainment(String name, String date, String year, String description, String poster_path, String id,
			String[] genres, String[] productionCompanies) {
		this.name = name;
		this.date = date;
		this.year = year;
		this.description = description;
		this.poster_path = poster_path;
		this.id = id;
		// this.index = index;
		this.genres = genres;
		this.productionCompanies = productionCompanies;
	}

	public String[] getJSONArray(JSONArray arr) {
		String[] arrstr = new String[arr.length()];
		for (int i = 0; i < arr.length(); i++) {
			try {
				arrstr[i] = (String) arr.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return arrstr;
	}

	public EmbedBuilder getSearchEmbed(EmbedBuilder embed0) {
		EmbedBuilder embed = embed0;
		embed.addField(name + "(" + year + ")", " ", true);
		embed.addBlankField(true);
		embed.addField(id, " ", true);
		return embed;
	}

	public EmbedBuilder getInfoEmbed() {
		return null;
	}

	public String get(String get) {
		return media.optString(get);
	}

	public String getName() {
		return (name == null) ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getYear() {
		return year;
	}

	public int getYearInt() {
		return ((year.equals("")) ? 0 : Integer.valueOf(year));
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public String[] getProductionCompanies() {
		return productionCompanies;
	}

	public void setProductionCompanies(String[] productionCompanies) {
		this.productionCompanies = productionCompanies;
	}

}
