package App.scraper;

import App.model.ExtraPlaceEvent;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

abstract class BaseScraper {

    protected final Logger logger = LogManager.getLogger(getClass());
    String bookmaker;
    WebDriver driver;
    List<ExtraPlaceEvent> events = new ArrayList<>();

    abstract boolean login();

    /**
     * Gets new prices for an event. The new prices are set in the relevant object in the events list.
     *
     * @param  eventId Id of the event
     **/
    abstract void getNewPrices(String eventId);


    /**
     * Attempts to find the url of a page relating to a specified event
     *
     * @param  eventId Id of the event
     **/
    abstract String getEventURL(String eventId);


    /**
     * Retrieves information for a new eventId then creates and populates an ExtraPlaceEvent object which is added to
     * the events list
     *
     * @param  eventId Id of the event to add
    **/
    public void addEvent(String eventId){

        String[] temp = eventId.split(" ",2);
        ExtraPlaceEvent newEvent = new ExtraPlaceEvent();
        newEvent.name = temp[1];
        newEvent.bookmaker = bookmaker;
        newEvent.eventId = eventId;
        newEvent.startTime = LocalTime.parse(temp[0]);
        newEvent.eventURL = getEventURL(eventId);
        events.add(newEvent);
    }

    public void closeDriver(){
        driver.quit();
    }

}
