package weather;

public class MyBotMain {

    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        MyWeatherBot bot = new MyWeatherBot();
        MyQuoteBot bot2 = new MyQuoteBot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        bot2.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.freenode.net");
        bot2.connect("irc.freenode.net");

        // Join the #pircbot channel.
        bot.joinChannel("#zachsbots");
        bot2.joinChannel("#zachsbots");

    }
    
}