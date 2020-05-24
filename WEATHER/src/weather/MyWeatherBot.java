package weather;

//imports
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jibble.pircbot.*;


public class MyWeatherBot extends PircBot {
    
	//name of bot
    public MyWeatherBot() {
        this.setName("ZachWeatherBot");
    } 
     
    //send message
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
    	//pass message on ircnode to content
		String content = message;
		
        //this counts the number of words inputted by user
		int wordcount = 0;
		
        //divide content into seperate variables by spaces
		String command[] = content.split(" ");
		
        //this helps determine when there may be an error in userinput
		wordcount = command.length;
		
        //this determines if no valid userinput has been sent
        boolean notValid = true;
        
        //if first word is help go to help function
        if (command[0].equalsIgnoreCase("help"))
        {
        	helpfunction(channel, sender);
        	notValid = false;
        }
        
        //if first word is weather go to weather function
        else if(command[0].equalsIgnoreCase("weather"))
        {
			notValid = false;
			
        	if(wordcount == 1)
        	{
        		sendMessage(channel, sender + " Try again using the command format \"weather <cityname>\" or \"weather <zipcode>\"");
			}
			
			//build the url and get the request
			try
			{
				getWeather(channel, sender, command, wordcount);
			} 			
			//catch user error -> function called if user input can not be understood
			catch (IOException e)
			{
				{
					e.printStackTrace();
					sendMessage(channel, sender + " The weather is not currently available you may have misspelled the city or zipcode");
				}
			}
		}
		
		//do nothing here so that MyQuoteBot can handle this request alone
        else if(command[0].equalsIgnoreCase("quote"))
        {
        	notValid = false;
        }
        
        //if command is not a registered command display this error message
        else if(notValid && !message.contains("Heres a quote"))
        {
        	sendMessage(channel, sender + ": The command is not understood");
        	sendMessage(channel, sender + ": type \"help\" for instructions");
        }
      
    }
    
    //this function shows instructions to user
    public void helpfunction(String channel, String sender)
    {
    	sendMessage(channel, sender + ": To request temperature of a city in fahrenheit type \" weather <city name> \" or \"weather <zipcode>\"");
    	sendMessage(channel, sender + ": To get a quote from breaking bad type \"quote\"");
    }

	//build the url, get the temperature, and post it
	public void getWeather(String channel, String sender, String[] command, int wordcount) throws IOException 
	{
		//unique api token
		String myAPItoken = "&APPID=3e013d807d3ca0ebef47ccc9207c20fb";

		String myApiUrl = "";

		//if input contains numbers the user has entered a zipcode
		if(command[1].matches(".*\\d.*"))
		{
			myApiUrl = "http://api.openweathermap.org/data/2.5/weather?zip=";
		}
		//else it is a city name
		else
		{
			myApiUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
		}

		String userinput = command[1];

		//if city name has 2 words in it
		if(wordcount > 2)
		{
			userinput = command[1] + " " + command[2];
		}
		else
		{
			userinput = command[1];
		}

		String weatherURL = myApiUrl + userinput + myAPItoken;

		//create weather object
		URL url = new URL(weatherURL); 
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String result = rd.readLine();
		
		//this function parses information from website
		String temp = parseJsonFunction(result);

		//send message to user containing temperature
		sendMessage(channel, sender + ": The weather in " + userinput + " is " + temp + " degrees fahrenheit.");
	}
    
    //this function parses the information using json tools to return the temperature
	public static String parseJsonFunction(String json) 
	{
		//creates json object
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		JsonObject main = object.getAsJsonObject("main");

		//temp holds the temperature in kelvin -> convert it to fahrenheit
		double temp = main.get("temp").getAsDouble();
		temp = temp * 1.8;
		temp = temp - 459.67;

		//round decimal to 2 positions
		temp = round(temp, 2);

		//return temperature as a string
		String tempNew = Double.toString(temp);
		return tempNew; 
	}
	
	//this function rounds double temperature to 2 places
	private static double round(double value, int places)
	 {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}