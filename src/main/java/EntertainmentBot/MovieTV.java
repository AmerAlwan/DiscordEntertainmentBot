package EntertainmentBot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class MovieTV extends Entertainment {

	// protected String name, date, year, description, poster_path, pureid, id,
	// type, vote, popularity, vote_count;

	public MovieTV(JSONObject media) {
		this.pureid = media.optString("id");
		if (media.optString("media_type").equals("m")) {
			this.name = media.optString("title");
			this.date = media.optString("release_date");
			this.type = "m";
			this.id = "m" + pureid;
			// super(, media.optString("release_date"),
			// media.optString("media_type"), media.optString("id"));
		} else {
			this.name = media.optString("name");
			this.date = media.optString("first_air_date");
			this.type = "tv";
			this.id = "tv" + pureid;
			// super(media.optString("title"), media.optString("release_date"),
			// media.optString("media_type"), media.optString("id"));
		}

		this.description = media.optString("overview");
		this.poster_path = media.optString("poster_path");
		this.popularity = media.optString("popularity");
		this.vote_count = media.optString("vote_count");
		this.vote_avg = media.optString("vote_average");
		try {
			JSONArray array = media.optJSONArray("origin_country");
			if (array != null && array.length() > 0) {
				this.country = (String) array.get(0);
			} else {
				this.country = "";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.year = "";
		System.out.println("Date: " + date);
		if (date != null && date.length() >= 4) {
			year = date.substring(0, 4);
		}
		// this.year = year;
		// this.type = type;
		// this.id = type + pureid;
		// this.pureid = id;

		// name = media.optString("title");
		// date = media.optString("release_date");
		// if(date != "") {
		// year = date.substring(0,4);
		// }
		// description = media.optString("overview");
		// poster_path = "https://image.tmdb.org/t/p/original" +
		// media.optString("poster_path");
		// type = media.optString("media_type");
		// vote = String.valueOf(media.optDouble("vote_average"));
		// vote_count = String.valueOf(media.optInt("vote_count"));
		// popularity = String.valueOf(media.optDouble("popularity"));
	}

	@Override
	public EmbedBuilder getInfoEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(name + " (" + year + ")");
		embed.setDescription(description);
		embed.setThumbnail("https://image.tmdb.org/t/p/original/" + poster_path);
		embed.setFooter("Info from tmdb");
		embed.addField("ID", id, true);
		embed.addField("Type", type, true);
		embed.addField(((type == "tv") ? "First Air Date" : "Release Date"), date, true);
		embed.addField("Country", country, true);
		embed.addField("Vote Average", vote_avg + " (" + vote_count + ")", true);
		embed.addField("Popularoty", popularity, true);
		return embed;

	}

}
