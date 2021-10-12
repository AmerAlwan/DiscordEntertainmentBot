package Events;

import java.util.HashMap;

import com.vdurmont.emoji.EmojiParser;

import EntertainmentBot.List;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class MessageReactionAdd {

	private HashMap<String, HashMap<String, List>> lists = new HashMap<>();
	private MessageReactionAddEvent react;

	public MessageReactionAdd() {
		;
	}

	public void update(MessageReactionAddEvent react) {
		this.react = react;
		String id = react.getTextChannel().getId();

		if (!isBot() && lists.containsKey(id) && lists.get(id).containsKey(react.getMessageId())) {

				System.out.println("Step 1");
		
					List l = lists.get(id).get(react.getMessageId());
					if (l.getMessageId().equals(react.getMessageId())) {
						if (l.getAccess(react.getMember().getId())) {
							System.out.println("Step 2");
							if (isEmoji("arrow_backward")) {
								l.previousPage();
								l.displayEmbed();
							} else if (isEmoji("arrow_forward")) {
								l.nextPage();
								l.displayEmbed();
							} else if (isEmoji("rewind")) {
								l.previousPages(3);
								l.displayEmbed();
							} else if (isEmoji("fast_forward")) {
								l.nextPages(3);
								l.displayEmbed();
							} else if (isEmoji("lock")) {
								l.lock();
							} else if (isEmoji("unlock")) {
								l.unlock();
							}
							removeReaction();
						} else {
							removeReaction();
						}
					}
				
			
		}
	}
	
	public HashMap<String, HashMap<String, List>> getLists() {
		return lists;
	}
	
	public HashMap getListsOfUser(String userID, String serverID) {
		lists.get(serverID).entrySet().stream().filter(a -> a.getValue().getUserId().equals(userID));
	}

	public String getEmoji(String text) {
		return EmojiParser.parseToUnicode(text);
	}

	public boolean isBot() {
		return react.getUser().isBot();
	}

	public boolean isEmoji(String name) {
		return react.getReactionEmote().getName().equals(getEmoji(":" + name + ":"));
	}

	public void removeReaction() {
		react.getReaction().removeReaction(react.getMember().getUser()).queue();
	}

}
