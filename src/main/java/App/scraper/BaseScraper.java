package App.scraper;

import App.model.ExtraPlaceEvent;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

abstract class BaseScraper {

    protected final Logger logger = LogManager.getLogger(getClass());

    private String bookmaker;
    WebDriver driver;
    List<ExtraPlaceEvent> events = new ArrayList<>();

    BaseScraper(){
        bookmaker = "Ladbrokes";
        System.setProperty("webdriver.gecko.driver", "D:/Google Drive/Repos/BettingExtraPlaceTool/otherDependencies/geckodriver.exe");
        System.setProperty("webdriver.firefox.logfile","D:/Google Drive/Repos/BettingExtraPlaceTool/otherDependencies/driver.log");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    abstract boolean login();

    abstract HashMap<String, Double[]> getPricesForEvent(String eventURL, int attempts);

    /**
     * Gets new prices for an event. The new prices are set in the relevant object in the events list.
     *
     * @param  eventId Id of the event
     **/
    public List<ExtraPlaceEvent> getNewPrices(String eventId){

        // find event in list based on the Id
        List<ExtraPlaceEvent> eventsToPrice = ScraperHelper.getEventsById(eventId, events);
        for(ExtraPlaceEvent event : eventsToPrice){
            event.prices = getPricesForEvent(event.eventURL,0);
            event.timeOfLastPriceUpdate = LocalTime.now();
        }
        return eventsToPrice;
    }
    /**
     * Attempts to find the url of a page relating to a specified event
     *
     **/
    abstract String getEventURL(String name, String time, String market);


    /**
     * Retrieves information for a new eventId then creates and populates an ExtraPlaceEvent object which is added to
     * the events list
     *
     * @param  eventId Id of the event to add
    **/
    public void addEvent(String eventId, String market){

        String[] temp = eventId.split(" ",2);
        ExtraPlaceEvent newEvent = new ExtraPlaceEvent();
        newEvent.bookmaker = bookmaker;
        newEvent.eventId = eventId;
        newEvent.startTime = convertToTimeObject(temp[0]);
        if(newEvent.startTime.isAfter(LocalTime.now())) {
            newEvent.eventURL = getEventURL(temp[1], temp[0], market);
            if(newEvent.eventURL != null){ events.add(newEvent);}
        }
    }

    public List<ExtraPlaceEvent> getEvents() {
        return events;
    }

    public void removeById(String eventId){
        events.removeIf(e -> e.eventId.equals(eventId));
    }

    public static LocalTime convertToTimeObject(String stringForm){
        try{
            return LocalTime.parse(stringForm);
        } catch (DateTimeParseException e){
            String[] temp = stringForm.split(":",2);
            Integer hours = Integer.valueOf(temp[0]) + 12;
            return LocalTime.parse(hours.toString()+":"+temp[1]);
        }
    }
}
