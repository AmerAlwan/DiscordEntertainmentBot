package EntertainmentBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

import com.sun.javafx.collections.MappingChange.Map;
import com.vdurmont.emoji.EmojiParser;

import Enums.*;
import Events.MessageReactionAdd;
import music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	int thread = 1;
	// Semaphore semaphore = new Semaphore(2);
	HashMap<ETypes, Semaphore> semaphore = new HashMap<>();
	public final int ITEMS_PER_PAGE = 10;
	//private HashMap<String, HashMap<String, List>> lists = new HashMap<>();
	private MessageReactionAdd msgRA = new MessageReactionAdd();

	public Commands() {
		for (ETypes e : ETypes.values()) {
			semaphore.put(e, new Semaphore(2));
		}
	}

	public void onMessageReactionAdd(MessageReactionAddEvent react) {
		msgRA.update(react);
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ");

		if (!args[0].equals(Main.prefix))
			return;

		if (event.getAuthor().isBot())
			return;

		if (args.length <= 1) {
			event.getChannel().sendMessage("Please Enter Command!").queue();
			return;
		}

		if (args[1].equals("info") || args[1].equals("in")) {
			SearchThread searchThread = searchThread(event, args, EType.NONE, 1, "info");
		} else if (args[1].contains("info") || args[1].contains("in")) {
			for (EType e : EType.values()) {
				if (args[1].equals(e.toText() + "info") || args[1].equals(e.getAbv() + "in")) {
					SearchThread searchThread = searchThread(event, args, e, 1, "info");
				}
			}
		}

		else if (args[1].contains("search") || args[1].contains("s")) {
			if (args[1].equals("search") || args[1].equals("s")) {
				SearchThread searchThread = searchThread(event, args, EType.NONE, 20, "search");
				// List list = searchThread.getList();
				// checkLists(event.getGuild().getId(), list);
				// list.displayEmbed();

			} else {
				for (EType e : EType.values()) {
					if (args[1].equals(e.toText() + "search") || args[1].equals(e.getAbv() + "s")) {
						SearchThread searchThread = searchThread(event, args, e, 20, "search");
					}
				}
			}
		}

		else if (args[1].equals("test")) {
			System.out.println("Test");
		}

		else if (args[1].equals("play")) {
			PlayerManager manager = PlayerManager.getInstance();
			manager.loadAndPlay(event.getTextChannel(), "");
			manager.getGuildMusicManager(event.getGuild()).player.setVolume(10);
		}

	}

	public void checkLists(MessageReceivedEvent event, List list) {
		HashMap<String, HashMap<String, List>> lists = msgRA.getLists();
		if (!lists.containsKey(list.getChannelId())) {
			lists.put(list.getChannelId(), new HashMap<>());
			// lists.get(guildId).put(userId, list);
		}
		// if (lists.get(guildId).containsKey(userId)) {

		HashMap<String, List> map = (HashMap<String, List>) lists.get(list.getChannelId()).entrySet().stream()
				.filter(l -> l.getValue().getUserId().equals(event.getMember().getId())).findFirst().orElse(null);

		if (map != null) {
			lists.remove(map);
		}
		lists.get(list.getChannelId()).put(list.getMessageId(), list);

		// }

	}

	public SearchThread searchThread(MessageReceivedEvent event, String[] args, EType type, int limit, String mode) {
		Infoset is = new Infoset(event, args, mode);
		is.displayValues();
		if (type != EType.NONE) {
			is.setType(type);
		}
		if (!is.isActive()) {
			return null;
		}
		return new SearchThread(is, event, semaphore, this, limit);
	}

}
