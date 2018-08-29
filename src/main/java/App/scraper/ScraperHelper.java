package App.scraper;

import App.model.ExtraPlaceEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScraperHelper {

    protected static final Logger logger = LogManager.getLogger();
    /**
     *
     * @param eventId
     * @param events
     * @return
     */
    public static List<ExtraPlaceEvent> getEventsById(String eventId, List<ExtraPlaceEvent> events){
        return events.stream().filter(e -> e.eventId.equals(eventId)).collect(Collectors.toList());
    }


    /**
     * Converts a odds into double from string. If the odds are in the fractional format they are converted to decimal.
     * Returns null if string is not convertible
     *
     * @param odds the odds to convert as a string
     * @return  the converted odds
     */
    public static Double convertOdds(String odds){

        if(odds.matches("\\d+/\\d+")){
            String[] temp = odds.split("/");
            return Double.parseDouble(temp[0])/Double.parseDouble(temp[1]) + 1;
        }
        else if (odds.matches("\\d+.\\d+")){
            return Double.parseDouble(odds);
        }
        else{
            logger.warn("Couldn't convert odds : "+odds);
            return null;
        }
    }

    public static void printPrices(List<ExtraPlaceEvent> events){
        events.forEach(e ->{
            System.out.println("\n"+e.eventId);
            System.out.println("------------------------------------");
            e.prices.forEach((k,v) -> {
                System.out.printf("%-30.30s  %-30.30s%n", k, v);
            });
        });
    }
}
