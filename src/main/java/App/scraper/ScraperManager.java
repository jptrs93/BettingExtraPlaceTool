package App.scraper;

import App.model.BetInstruction;
import App.model.ExtraPlaceEvent;
import App.model.TimeManager;
import com.sun.org.apache.bcel.internal.generic.LoadClass;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ScraperManager {

    private List<String> eventsWithBets = new ArrayList<>();
    private HashMap<String, LocalTime> events = new HashMap<>();

    private LadbrokesScraper ladbrokesScraper = new LadbrokesScraper();;
    private BetfairScraper betfairScraper = new BetfairScraper();;


    public List<String> checkForEvents(){

        List<String> events = ladbrokesScraper.getExtraPlaceEvents();
        for(String eventId : events){
            addToEventsList(eventId);
            for(String market : new String[]{"BACK-WIN" , "LAY-EACHWAY" , "LAY-WIN", "LAY-PLACE"}) {
                ladbrokesScraper.addEvent(eventId,market);
                betfairScraper.addEvent(eventId, market);
            }
        }
        return events;
    }

    public List<String> getEventsToAnalyse(){
        List<String> eventsStartingSoon =  new ArrayList<>();
        for(String eventId : events.keySet()){
            if(TimeManager.isEventStartingSoon(events.get(eventId))){
                eventsStartingSoon.add(eventId);
            }
        }
        return eventsStartingSoon;
    }

    public List<ExtraPlaceEvent> getPriceData(String eventId){
        List<ExtraPlaceEvent> events = new ArrayList<>();
        events.addAll(ladbrokesScraper.getNewPrices(eventId));
        events.addAll(betfairScraper.getNewPrices(eventId));
        return events;
    }

    private void addToEventsList(String eventId){
        String[] temp = eventId.split(" ",2);
        events.put(eventId,LocalTime.parse(temp[0]));
    }

    public void clean(){
        for(String eventId : events.keySet()){
            if(events.get(eventId).isBefore(LocalTime.now().plusMinutes(1))){
                ladbrokesScraper.removeById(eventId);
                betfairScraper.removeById(eventId);
            }
        }
    }

    public void placeBet(BetInstruction betInstruction){
        System.out.print("Placing bet on: "+betInstruction.horseName);
        // todo: implement betting functionality
    }

    public List<String> getEvents(){
        return new ArrayList<>(events.keySet());
    }

    public List<LocalTime> getListOfStartTimes(){
        return new ArrayList<>(events.values());
    }
}
