package App;


import App.model.Analyser;
import App.model.BetInstruction;
import App.model.ExtraPlaceEvent;
import App.model.TimeManager;
import App.scraper.BetfairScraper;
import App.scraper.LadbrokesScraper;
import App.scraper.ScraperHelper;
import App.scraper.ScraperManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    private static ScraperManager scraperManager = new ScraperManager();
    private static Analyser analyser = new Analyser();

    private static boolean sleepMode(LocalTime wakeUpTime){
        System.out.println("Entering quiet mode. Press \"q\" to quit application or \"a\" to force analysis to run.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String temp = "";
        while(LocalTime.now().isBefore(wakeUpTime)){
            try {
                Thread.sleep(100);
                if(reader.ready()){temp = reader.readLine();}
            } catch (InterruptedException| IOException e) {
                e.printStackTrace();
            }
            if(temp.matches("[aA].*")){
                for(String eventId : scraperManager.getEvents()) {
                    System.out.println("Fetching data for event: " + eventId);
                    List<ExtraPlaceEvent> eventData = scraperManager.getPriceData(eventId);
                    System.out.println("Analysing data for event: " + eventId);
                    BetInstruction betInstruction = analyser.analyse(eventData);
                };
                temp = "";
                System.out.println("Going back to quiet mode");
            }
            else if(temp.matches("[qQ].*"))
                return true;
            else if(!temp.equals("")){
                System.out.println("Command not recognised");
            }
        }
        return false;
    }

    public static void main(String args[]){

        System.out.println("Starting Application...");
        LocalTime wakeUpTime = LocalTime.now().plusSeconds(3);

        while(true){
            if(sleepMode(wakeUpTime)){
                break;
            }
            System.out.println("Cleaning old events");
            scraperManager.clean();
            System.out.println("Checking for extra place offer events. The following events found:");
            scraperManager.checkForEvents().forEach(System.out::println);
            System.out.println("Checking and analysing for events staring soon:");
            for(String eventId : scraperManager.getEventsToAnalyse()){
                System.out.println("Fetching data for event: "+eventId);
                List<ExtraPlaceEvent> eventData = scraperManager.getPriceData(eventId);
                for (ExtraPlaceEvent event : eventData){
                    event.printData();
                }
                System.out.println("Analysing data for event: "+eventId);
                BetInstruction betInstruction = analyser.analyse(eventData);
                if(betInstruction != null) {
                    scraperManager.placeBet(betInstruction);
                }
            }
            wakeUpTime = TimeManager.calculateSleep(scraperManager.getListOfStartTimes());
        }
    }
}
