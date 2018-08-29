package App.scraper;

import App.model.ExtraPlaceEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BetfairScraper extends BaseScraper {

    @Override
    boolean login() {
        return false;
    }

    @Override
    public HashMap<String, Double[]> getPricesForEvent(String eventURL, int attempts){

        // find event in list based on the Id
        HashMap<String, Double[]> prices =  new HashMap<>();
        if(attempts < 5){
            try {
                // load the event page
                driver.get(eventURL);
                // get list web elements for each runner
                List<WebElement> runners = driver.findElements(By.className("runner-line"));
                System.out.print("Found:  " + runners.size());
                for (WebElement runner : runners) {
                    // extract the name and price for each runner and update stored values
                    String name = runner.findElement(By.className("name")).findElement(By.tagName("h3")).getText();
                    System.out.println(name);
                    Double[] odds = new Double[2];
                    odds[0] = ScraperHelper.convertOdds(runner.findElement(By.cssSelector("td.bet-buttons.lay-cell.first-lay-cell"))
                            .findElement(By.className("bet-button-price")).getText());
                    // todo: also get amount bet and add it to odds[1]
                    if (odds[0] != null) {
                        prices.put(name, odds);
                    }
                }
            }catch(StaleElementReferenceException e){
                return getPricesForEvent(eventURL, attempts +1);
            }
        }
        return prices;
    }

    @Override
    String getEventURL(String name, String time, String market) {
        switch (market) {
            case "LAY-WIN":
                driver.get("https://www.betfair.com/exchange/plus/horse-racing");
                // Get a list of web elements relating to all events and filter to the one which matches the race name
                Optional<WebElement> temp = driver.findElements(By.className("meeting-item")).stream().filter(e -> e.getText().contains(name)).findFirst();
                if (temp.isPresent()) {
                    // Get a list of web elements relating to the race times for the events and filter to correct time
                    temp = temp.get().findElements(By.tagName("a")).stream().filter(e -> e.getText().equals(time)).findFirst();
                    if (temp.isPresent()) {
                        return temp.get().getAttribute("href");
                    }
                }
                break;
            case "LAY-EACHWAY":
                //todo: implement
                return null;
            case "LAY-PLACE":
                // todo: implement
                return null;
        }
        return null;
    }
}
