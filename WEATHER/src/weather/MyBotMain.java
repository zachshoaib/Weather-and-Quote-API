package weather;
import org.jibble.pircbot.*;

public class MyBotMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        MyBot bot = new MyBot();
        MyQuoteBot bot2 = new MyQuoteBot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        bot2.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.freenode.net");
        bot2.connect("irc.freenode.net");

        // Join the #pircbot channel.
        bot.joinChannel("#zachbot6");
        bot2.joinChannel("#zachbot6");
        
       
        
    }
    
    
}