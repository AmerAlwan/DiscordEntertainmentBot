package EntertainmentBot;

import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class Music extends Entertainment {
	
	private String artist = "", artist_url = "", artist_image = "", album = "", album_url = "", album_image = "";
	
	public Music(JSONObject media) {
		this.media = media;
		name = media.optString("title");
		this.setArtist(media.optJSONObject("artist").optString("name"));
		artist_url = media.optJSONObject("artist").optString("link");
		artist_image = media.optJSONObject("artist").optString("picture");
		album = media.optJSONObject("album").optString("title");
		album_url = media.optJSONObject("album").optString("tracklist");
		album_image = media.optJSONObject("album").optString("cover");
		url = get("link");
		duration = get("duration");
		
		vote_avg = media.optString("rank");
		
		type = "mu";
		pureid = String.valueOf(media.optInt("id"));
		id = "mu" + pureid;
	}
	
	@Override
	public EmbedBuilder getInfoEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(name, url);
		embed.setAuthor(artist, artist_url, artist_image);
		embed.setFooter("Info from Deezer");
		embed.setThumbnail(album_image);
		embed.addField("ID", id, true);
		embed.addField("Rank", vote_avg, true);
		embed.addBlankField(true);
		String albumUrl = "[" + album + "](" + album_url + ")";
		embed.addField("Album", albumUrl, true);
		embed.addField("Duration", duration, true);
		embed.addBlankField(true);
		return embed;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist_url() {
		return artist_url;
	}

	public void setArtist_url(String artist_url) {
		this.artist_url = artist_url;
	}

}
