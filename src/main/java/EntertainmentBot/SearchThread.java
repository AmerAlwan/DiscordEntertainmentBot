package EntertainmentBot;

import java.util.ArrayList;
//import static EntertainmentBot.SortBy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import org.json.JSONObject;

//import com.sun.javafx.collections.MappingChange.Map;

import Enums.EType;
import Enums.ETypes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SearchThread implements Runnable {
	volatile Infoset is;
	volatile HashMap<String, Semaphore> semaphore;
	volatile ArrayList<JSONObject> results = new ArrayList<>();
	volatile MessageReceivedEvent event;
	volatile List list;
	volatile boolean isActive = false;
	volatile Commands commands;
	volatile int limit;

	SearchThread(Infoset is, MessageReceivedEvent event, HashMap semaphore, Commands commands, int limit) {
		this.semaphore = semaphore;
		this.is = is;
		this.event = event;
		this.commands = commands;
		this.limit = limit;
		Thread thread = new Thread(this);
		thread.start();
	}

	public List getList() {
		return list;
	}

	public boolean isActive() {
		return isActive;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Search search = new Search(is, semaphore.get(is.getETypes()), 1, limit);
		// TODO Auto-generated method stub

		while (true) {
			if (search.isActive()) {
				System.out.println("Done");
				results = search.getResults();
				if (results.size() == 0) {
					event.getChannel().sendMessage("No results found for '" + is.getName() + "'").queue();
					break;
				} else {
					Entertainment[] entertainment = new Entertainment[results.size()];

					for (int i = 0; i < results.size(); i++) {
						System.out.println("Media Type: " + results.get(i).optString("media_type"));

						String type = results.get(i).optString("media_type");
						ETypes eType = is.getETypes(type);

						System.out.println("EType: " + eType);

						if (eType == ETypes.GAME) {
							entertainment[i] = new Game(results.get(i));
						}

						if (eType == ETypes.MUSIC) {
							entertainment[i] = new Music(results.get(i));
						}

						if (eType == ETypes.ANIMEMANGA) {
							entertainment[i] = new AnimeManga(results.get(i));
						}

						if (eType == ETypes.MOVIETV) {
							entertainment[i] = new MovieTV(results.get(i));
						}
					}

					//SortBy sortBy = new SortBy();
					
					
					
					if (is.isAll()) {
						entertainment = SortBy.sortRelevance(entertainment, is.getName());
					}
					
					if(!is.getDate().equals("")) {
						//entertainment = SortBy.filterByDate(entertainment, is.getDate());
					}

					// int index = 1;
					// for(Entertainment e : entertainment) {
					// System.out.println(index + ": " + e.getName());
					// index += 1;
					// }
					System.out.println(is.isModeSearch());
					if (is.isModeSearch()) {
						System.out.println("TRUE");
						list = new List(entertainment,
								"Showing " + entertainment.length + " results for " + is.getName(), 8, is.getPrivacy(),
								event);

						if (!is.getPage().equals("")) {
							list.setPage(Integer.valueOf(is.getPage()));
						}
						if (list.isPrivate()) {
							list.displayEmbeds();
						} else {
							list.displayEmbed();
							while (true) {
								if (list.isEmbed()) {
									System.out.println("Channel ID: " + list.getChannelId());
									commands.checkLists(event, list);
									break;
								}
							}
						}
					} else if(is.isModeInfo()) {
						event.getChannel().sendMessage(entertainment[0].getInfoEmbed().build()).queue();
					}

					break;
				}
			}
		}

	}

}
