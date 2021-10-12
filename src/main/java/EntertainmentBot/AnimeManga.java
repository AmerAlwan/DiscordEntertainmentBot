package EntertainmentBot;

import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class AnimeManga extends Entertainment {

	public AnimeManga(JSONObject media) {
		this.media = media;
		// if(media.optString("media_type").equals("anm")) {
		this.name = media.optString("title");

		this.pureid = String.valueOf(media.optInt("mal_id"));
		this.type = media.optString("media_type");
		this.id = type + pureid;
		this.poster_path = get("image_url");

		String airing = get("airing");
		boolean ongoing = ((airing != "") ? Boolean.valueOf(airing) : Boolean.valueOf(get("publishing")));
		this.status = ((ongoing) ? "Ongoing" : "Completed");

		this.description = get("synopsis");
		this.end_date = ((get("end_date") != null) ? get("end_date") : "");

		this.vote_avg = get("score");

		if (isAnime()) {
			this.episodes = get("episodes");
		} else {
			this.volumes = get("volumes");
			this.chapters = get("chapters");
		}

		this.vote_count = get("members");

		this.url = get("url");

		// }
		this.date = media.optString("start_date");
		if (!date.equals("")) {
			date = date.substring(0, 9);
		}
		
		if (!end_date.equals("")) {
			end_date = end_date.substring(0, 9);
		}
		
		if (date != null && date.length() >= 4) {
			year = date.substring(0, 4);
		}
	}

	@Override
	public EmbedBuilder getInfoEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(name + " (" + year + ")", url);
		embed.setDescription(description);
		embed.setFooter("Info from MyAnimeList <- Jikan");
		embed.setThumbnail(poster_path);
		embed.addField("Type", type, true);
		if (isAnime()) {
			embed.addField("ID", id, true);
			embed.addField("Episodes", episodes, true);
			
		} else {
			embed.addField("ID", id, true);
			embed.addBlankField(true);
			embed.addField("Volumes", volumes, true);
			embed.addField("Chapters", chapters, true);
			embed.addBlankField(true);
		}
		embed.addField("Status", status, true);
		embed.addField("Start Date", date, true);
		embed.addField("End Date", end_date, true);
		embed.addField("Score", vote_avg, true);
		embed.addField("Members", vote_count, true);
		embed.addBlankField(true);
		return embed;
	}

	private boolean isAnime() {
		return type.contains("an");
	}

	private boolean isManga() {
		return type.contains("mg");
	}
}
