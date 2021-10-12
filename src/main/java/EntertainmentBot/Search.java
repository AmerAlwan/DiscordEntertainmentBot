package EntertainmentBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search {

	private String media_type, name = "", date = "", page = "";
	private JSONObject result = new JSONObject();
	private volatile boolean isActive = false;
	private Semaphore semaphore;
	private int maxPages;
	private volatile ArrayList<JSONObject> results = new ArrayList<>();

	public Search(Infoset is, Semaphore semaphore, int maxPages, int limit) {
		this.semaphore = semaphore;
		this.maxPages = maxPages;
		Thread thread = new Thread(new SearchThread0(is, semaphore, maxPages, limit));
		thread.start();

	}

	public void searchMovie(String searchItem, String year, int maxPages) {
		String url = "https://api.themoviedb.org/3/search/movie?api_key=ce242dc8631f3030059e51dca89df4fb&language=en-US&page=1&query="
				+ searchItem.replaceAll(" ", "%20") + "&include_adult=false" + "&year=" + String.valueOf(year)
				+ "&page=1";
		JSONObject[] searchPageResults = search(url, maxPages, "total_results", "total_pages", "page");

		JSONObject[] searchResults = getItems(searchPageResults, "results");
		if (searchPageResults != null) {
			if (searchResults != null) {
				for (JSONObject o : searchResults) {
					try {
						results.add(o.put("media_type", "m"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void searchTV(String searchItem, String year, int maxPages) {
		String url = "https://api.themoviedb.org/3/search/tv?api_key=ce242dc8631f3030059e51dca89df4fb&language=en-US&page=1&query="
				+ searchItem.replaceAll(" ", "%20") + "&include_adult=false" + "&year=" + String.valueOf(year)
				+ "&page=1";
		JSONObject[] searchPageResults = search(url, maxPages, "total_results", "total_pages", "page");
		if (searchPageResults != null) {
			JSONObject[] searchResults = getItems(searchPageResults, "results");
			if (searchResults != null) {
				for (JSONObject o : searchResults) {
					try {
						results.add(o.put("media_type", "tv"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void searchMovieTV(String searchItem, String year, int maxPages) {
		String url = "https://api.themoviedb.org/3/search/multi?api_key=ce242dc8631f3030059e51dca89df4fb&language=en-US&page=1&query="
				+ searchItem.replaceAll(" ", "%20") + "&include_adult=false" + "&year=" + String.valueOf(year)
				+ "&page=1";
		JSONObject[] searchPageResults = search(url, maxPages, "total_results", "total_pages", "page");
		if (searchPageResults != null) {
			JSONObject[] searchResults = getItems(searchPageResults, "results");
			if (searchResults != null) {
				for (JSONObject o : searchResults) {
					try {
						results.add(o.put("media_type", o.optString("media_type").replace("movie", "m")));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void searchMusic(String searchItem, String artist) {
		String url = "https://api.deezer.com/search?q=artist:%22" + artist + "%22%20track:%22"
				+ searchItem.replaceAll(" ", "%20") + "%22%20album:%22%22";
		JSONObject searchPageResults;
		try {
			searchPageResults = search(url);
			if (searchPageResults != null) {
				JSONArray searchResults = searchPageResults.getJSONArray("data");
				if (searchResults != null) {
					for (int i = 0; i < searchResults.length(); i++) {
						results.add(searchResults.getJSONObject(i).put("media_type", "mu"));
					}
				}
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String url =
		// "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" +
		// searchItem
		// + "&api_key=906cfc1d50758a334bda9546b1137764&format=json&limit=" +
		// limit + "&artist=" + artist;
		// JSONObject searchPageResults;
		// try {
		// searchPageResults = search(url);
		// if (searchPageResults != null) {
		// JSONObject searchResults =
		// searchPageResults.getJSONObject("results");
		// if (searchResults != null) {
		// JSONObject searchResults2 =
		// searchResults.getJSONObject("trackmatches");
		// if (searchResults2 != null) {
		// JSONArray searchResults3 = searchResults2.getJSONArray("track");
		//
		// for (int i = 0; i < searchResults3.length(); i++) {
		// results.add(searchResults3.getJSONObject(i).put("media_type", "mu"));
		// }
		//
		// }
		//
		// }
		// }
		// } catch (IOException | JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public void searchGame(String searchItem, String year, int maxPages, int limit) {
		String date = "";
		if (year != "") {
			date = year + "," + year;
		}
		String url = "https://api.rawg.io/api/games?search=" + searchItem.replaceAll(" ", "%20") + "&page=1&page_size="
				+ limit + "&dates=" + date;
		JSONObject[] searchPageResults = search(url, maxPages, "count", "none", "page");
		if (searchPageResults != null) {
			JSONObject[] searchResults = getItems(searchPageResults, "results");
			if (searchResults != null) {
				for (JSONObject o : searchResults) {
					try {
						results.add(o.put("media_type", "g"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void searchAllAnime(String searchItem, int maxPages, int limit, String type1, String type20, String date) {
		String type2 = "";
		if (type20 != "") {
			type2 = "&type=" + type20;
		}
		String url = "https://api.jikan.moe/v3/search/" + type1 + "?q=" + searchItem.replaceAll(" ", "%20")
				+ "&start_date=" + date + "&page=1&limit=" + limit + type2;

		JSONObject[] searchPageResults = search(url, maxPages, "none", "last_page", "page");
		if (searchPageResults != null) {
			JSONObject[] searchResults = getItems(searchPageResults, "results");
			if (searchResults != null) {
				// System.out.println("Search Results Size: " +
				// searchResults.length);
				for (JSONObject o : searchResults) {
					// if(type2.equals("")) {
					type2 = o.optString("type").toLowerCase();
					// System.out.println("Type 2: " + type2);
					// }
					try {

						results.add(o.put("media_type",
								type2.replace("&type=", "").replace("movie", "anm").replace("tv", "antv")
										.replace("novel", "ln").replace("manga", "mg").replace("special", "ansp")
										.replace("ova", "anov").replace("ona", "anon").replace("music", "anmu")
										.replace("one-shot", "mgos").replace("doujin", "mgdo")
										.replace("manhwa", "mgmwa").replace("manhua", "mgmua")));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void searchAnime(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "", date);
	}

	public void searchAnimeMovie(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "movie", date);
	}

	public void searchAnimeTV(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "tv", date);
	}

	public void searchAnimeSpecial(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "special", date);
	}

	public void searchAnimeOVA(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "ova", date);
	}

	public void searchManga(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "", date);
	}

	public void searchMangaManga(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "manga", date);
	}

	public void searchLightNovel(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "novel", date);
	}

	public void searchONA(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "ona", date);
	}

	public void searchAnimeMusic(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "anime", "music", date);
	}

	public void searchOneshot(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "oneshot", date);
	}

	public void searchDoujin(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "doujin", date);
	}

	public void searchManhwa(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "manhwa", date);
	}

	public void searchManhua(String searchItem, int maxPages, int limit, String date) {
		searchAllAnime(searchItem, maxPages, limit, "manga", "novel", date);
	}

	public JSONObject search(String url) throws IOException, JSONException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(3000);
		con.connect();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending GET request to URL: " + url);
		System.out.println("Response Code: " + responseCode);
		if (responseCode != 200) {
			return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// System.out.println(response.toString());

		JSONObject myresponse = new JSONObject(response.toString());

		System.out.println(myresponse);

		// System.out.println("Total Pages: " +
		// myresponse.getInt("total_pages"));
		return myresponse;
	}

	public JSONObject[] search(String url, int maxPages, String totalResultsKeyword, String totalPagesKeyword,
			String pageKeyword) {
		ArrayList<JSONObject> results = new ArrayList<>();
		try {
			JSONObject page1 = search(url);
			if (page1 != null) {
				int actualMaxPages;
				if (totalPagesKeyword.equals("none")) {
					actualMaxPages = 1;
				} else {
					actualMaxPages = page1.optInt(totalPagesKeyword);
				}
				if (!totalResultsKeyword.equals("none") && page1.optInt(totalResultsKeyword) == 0) {
					return null;
				}
				results.add(page1);

				for (int i = 2; i <= maxPages; i++) {
					if (i <= actualMaxPages) {
						results.add(search(url.replace(pageKeyword + "=" + (i - 1), pageKeyword + "=" + i)));
					} else {
						break;
					}
				}

				return results.toArray(new JSONObject[results.size()]);
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return isActive;
	}

	public ArrayList<JSONObject> getResults() {
		return results;
	}

	public JSONObject[] getItems(JSONObject[] searchResults, String resultsKeyword) {
		ArrayList<JSONObject> results = new ArrayList<>();
		if (searchResults != null) {
			for (JSONObject o : searchResults) {
				JSONArray jsonarray;
				try {
					jsonarray = o.getJSONArray(resultsKeyword);
					for (int j = 0; j < jsonarray.length(); j++) {
						results.add(jsonarray.getJSONObject(j));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}

			}
			return results.toArray(new JSONObject[results.size()]);
		}
		return null;
	}

	class SearchThread0 implements Runnable {

		volatile Semaphore semaphore = null;
		volatile Infoset is = null;
		volatile int maxPages;
		volatile int limit;

		public SearchThread0(Infoset is, Semaphore semaphore, int maxPages, int limit) {
			this.semaphore = semaphore;
			this.is = is;
			this.maxPages = maxPages;
			this.limit = limit;
		}

		@Override
		public void run() {
			// while (true) {
			System.out.println("Media Type: " + is.getType());
			// is.displayValues();
			try {
				this.semaphore.acquire();
				if (is.isAll()) {
					searchMovieTV(is.getName(), is.getDate(), maxPages);
					//results = results.
					searchAnime(is.getName(), 1, 8, is.getDate());
					searchGame(is.getName(), is.getDate(), 1, 8);
					//results.sort((a, b) -> a.compareTo(b));


				} else if (is.isMovie()) {
					System.out.println("Movie Search");
					searchMovie(is.getName(), is.getDate(), maxPages);

				} else if (is.isTV()) {
					System.out.println("TV Search");
					searchTV(is.getName(), is.getDate(), maxPages);

				} else if (is.isMTV()) {
					System.out.println("MTV Search");
					searchMovieTV(is.getName(), is.getDate(), maxPages);

				} else if (is.isAnime()) {
					System.out.println("Anime Search");
					searchAnime(is.getName(), 1, limit, is.getDate());

				} else if (is.isManga()) {
					System.out.println("Manga Search");
					searchManga(is.getName(), 1, limit, is.getDate());
				} else if (is.isGame()) {
					System.out.println("Game Search");
					searchGame(is.getName(), is.getDate(), 1, limit);
				} else if (is.isMusic()) {
					System.out.println("Music Search");
					searchMusic(is.getName(), is.getMusicArtist());
				} else if (is.isBook()) {

				} else if (is.isAnimeOVA()) {
					System.out.println("OVA Search");
					searchAnimeOVA(is.getName(), 1, limit, is.getDate());
				} else if (is.isAnimeSpecial()) {
					System.out.println("Special Search");
					searchAnimeSpecial(is.getName(), 1, limit, is.getDate());
				} else if (is.isAnimeMovie()) {
					System.out.println("Anime Movie Search");
					searchAnimeMovie(is.getName(), 1, limit, is.getDate());
				} else if (is.isAnimeTV()) {
					System.out.println("Anime TV Search");
					searchAnimeTV(is.getName(), 1, limit, is.getDate());
				} else if (is.isMangaManga()) {
					System.out.println("Manga Manga Search");
					searchMangaManga(is.getName(), 1, limit, is.getDate());
				} else if (is.isLightNovel()) {
					System.out.println("Light Novel Search");
					searchLightNovel(is.getName(), 1, limit, is.getDate());
				}

				isActive = true;
				System.out.println("Acquired");
				Thread.sleep(1000);

				// break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.out.println("Released");
				this.semaphore.release();
			}

			// }
		}

	}

}
