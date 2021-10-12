package EntertainmentBot;

import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite.Channel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class List {

	private volatile String title, privacy, userId, messageId, channelId;
	private volatile int limit, pages, currentPage;
	private Entertainment[] entertainment;
	private EmbedBuilder embed = new EmbedBuilder();
	private MessageReceivedEvent event;
	private volatile boolean firstTime = true, isPrivate = false, isEmbed = false, addReaction = true;
	

	public List(Entertainment[] entertainment, String title, int limit, String privacy,
			MessageReceivedEvent event) {
		this.entertainment = entertainment;
		this.title = title;
		this.limit = limit;
		this.event = event;
		this.privacy = privacy;
		this.userId = event.getMember().getId();
		currentPage = 1;
	//	System.out.println("ENTERTAINMENT LENGTH: " + entertainment.length);
		this.pages = (int) Math.ceil(Double.valueOf(entertainment.length) / limit);
	//	System.out.println("LIMIT: " + limit);
	//	System.out.println("PAGES: " + (entertainment.length / limit));
		if(privacy.equals("pr")) {
			isPrivate = true;
		}
	}

	public String getMessageId() {
		return messageId;
	}
	
	public String getChannelId() {
		return channelId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public boolean isEmbed() {
		return isEmbed;
	}

	public boolean getAccess(String id) {
		if (privacy.equals("prot") || privacy.equals("pr")) {
			if (id.equals(userId)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void nextPage() {
		currentPage += 1;
		if (currentPage > pages) {
			currentPage = 1;
		} // else {

		// }
	}
	
	public void nextPages(int num) {
		currentPage += num;
		if (currentPage > pages) {
			currentPage = 1;
		} // else {

		// }
	}

	public void previousPage() {
		currentPage -= 1;
		if (currentPage <= 0) {
			currentPage = pages;
		}
	}
	
	public void previousPages(int num) {
		currentPage -= num;
		if (currentPage <= 0) {
			currentPage = pages;
		}
	}
	
	public void setPage(int page) {
		if(page > pages) {
			currentPage = pages;
		}
		else if(page <= 0) {
			currentPage = 1;
		} else {
			currentPage = page;
		}
	}

	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void setFirstTime() {
		firstTime = true;
	}
	
	public void lock() {
		//isPrivate = true;
		privacy = "prot";
	}
	
	public void unlock() {
		//isPrivate = false;
		privacy = "pu";
	}
	
	public void displayEmbeds() {
		addReaction = false;
		for(int i = 1; i <= pages; i++) {
			currentPage = i;
			displayEmbed();
		}
	}
	
	public String getEmoji(String text) {
		return EmojiParser.parseToUnicode(text);
	}
	
	public void displayEmbed() {
		int startIndex = (currentPage - 1) * limit;
		int lastIndex = currentPage * limit;
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title);
		embed.setFooter("Showing Page " + currentPage + " of " + pages);
		System.out.println("Add Reaction: " + addReaction);
		for (int i = startIndex; i < lastIndex; i++) {
			if (i >= entertainment.length) {
				break;
			}
			if(entertainment[i] == null) {
				break;
			}
			System.out.println("Name: " + entertainment[i].getName());
			System.out.println("Year: " + ((entertainment[i].getYear() == null) ? "" : entertainment[i].getYear()));
			embed.addField((i + 1) + ".", entertainment[i].getName() + " (" + entertainment[i].getYear() + ") ", true);
			embed.addField("Type", entertainment[i].getType(), true);
			embed.addField("ID", entertainment[i].getId(), true);
		}

		if (firstTime) {
			if (isPrivate) {
				event.getAuthor().openPrivateChannel().flatMap(c -> c.sendMessage(embed.build())).queue(m -> {
					//m.addReaction(EmojiParser.parseToUnicode(":rewind:")).queue();
					//m.addReaction(EmojiParser.parseToUnicode(":fast_forward:")).queue();
					messageId = m.getId();
					firstTime = false;
					channelId = m.getPrivateChannel().getId();
					isEmbed = true;
					//isPrivate = true;
					// searchEmbed = embed;
				});
				//System.out.println("Private");
			} else {
				event.getChannel().sendMessage(embed.build()).queue(m -> {
					m.addReaction(getEmoji(":rewind:")).queue();
					m.addReaction(getEmoji(":arrow_backward:")).queue();
					m.addReaction(getEmoji(":arrow_forward:")).queue();
					m.addReaction(getEmoji(":fast_forward:")).queue();
					m.addReaction(getEmoji(":lock:")).queue();
					m.addReaction(getEmoji(":unlock:")).queue();
					messageId = m.getId();
					firstTime = false;
					channelId = m.getTextChannel().getId();
					System.out.println("Channel ID 1: " + channelId);
					isEmbed = true;
					//isPrivate = false;
					// searchEmbed = embed;
				});
				//System.out.println("Public");
			}
		} else if(addReaction) {
				event.getJDA().getTextChannelById(channelId).editMessageById(messageId, embed.build()).queue(m -> {
					m.addReaction(getEmoji(":rewind:")).queue();
					m.addReaction(getEmoji(":arrow_backward:")).queue();
					m.addReaction(getEmoji(":arrow_forward:")).queue();
					m.addReaction(getEmoji(":fast_forward:")).queue();
					m.addReaction(getEmoji(":lock:")).queue();
					m.addReaction(getEmoji(":unlock:")).queue();
					// messageID = event.getChannel().getLatestMessageId();
					// searchEmbed = embed;

				});
				System.out.println("Edited");
			

		}
	}
}
