package weather;

//imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jibble.pircbot.PircBot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//create bot
public class MyQuoteBot extends PircBot{
	
	//name the bot
	public MyQuoteBot() {
        this.setName("ZachQuoteBot");
    } 
	
	//find message and call quote function
	public void onMessage(String channel, String sender, String login, String hostname, String message) 
	{
		//look for command "quote"
		if (message.equalsIgnoreCase("quote")) 
		{
			try {
				//call function
				getQuote(channel, sender);
			} catch (IOException e) {
				//catch errors
				e.printStackTrace();
				{
					sendMessage(channel, sender + " error occured");
				}
			}
		}
	}
	
	//retrieve quotes from online source
	public void getQuote(String channel, String sender) throws IOException
	{
		//make string url
		String quoteURL = "https://breaking-bad-quotes.herokuapp.com/v1/quotes";

		//make url object
		URL url1 = new URL(quoteURL); 
		HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
		conn1.setRequestMethod("GET");
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
		String results = rd1.readLine();

		//parse information
		String quotes = parseJsonFunction(results);

		//send the message
		sendMessage(channel, sender + ": Heres a quote from breaking bad " + quotes);
	}
	
	//parse the information
	public static String parseJsonFunction(String json) 
	{
		//create jsonarray parse
		JsonArray entries = (JsonArray) new JsonParser().parse(json);
		JsonElement quote = ((JsonObject)entries.get(0)).get("quote");
		JsonElement author = ((JsonObject)entries.get(0)).get("author");

		//get quote
		String quote1 = String.valueOf(quote);

		//get author
		String author1 = String.valueOf(author);
		
		//combine
		String total = quote1 + " " + author1;
		return total; 
	}

}
