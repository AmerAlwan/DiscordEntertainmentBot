package EntertainmentBot;

import org.json.JSONObject;

public class Game extends Entertainment {
	
	String[] platforms;

	public Game(JSONObject game) {
		//String name, String date, String year, String description, String poster_path, String id,
		//String[] genres, String[] productionCompanies
		super(game.optString("name"), game.optString("released"), game.optString("media_type"), game.optString("id"));
		System.out.println("Name: " + name);
	}
	
	
	

}
