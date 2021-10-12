package EntertainmentBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Main {

	public static JDA jda;
	public static String prefix = "!e";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws LoginException {

		jda = new JDABuilder(AccountType.BOT).setToken("NzE0NTEwODA2OTA5NDUyMzU4.XsvuUw.LHXoTfRY4Kb1dfD4wr9O5AQ7eao")
				.build();
		jda.getPresence().setStatus(OnlineStatus.ONLINE);
		jda.addEventListener(new Commands());
		
		Playlist<Game> test = new Playlist<>();
		//test.add(new String());
		

	}

}
