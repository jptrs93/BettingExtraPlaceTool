package App.scraper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import App.model.ExtraPlaceEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



public class LadbrokesScraper extends BaseScraper {

    private HashMap<String, String> eventLinks = new HashMap<>();


    @Override
    public boolean login() {
        driver.get("https://sports.ladbrokes.com");
        driver.findElement(By.id("username")).sendKeys("jptrs93");
        driver.findElement(By.id("password")).sendKeys("glen5isla");
        driver.findElement(By.xpath("//*[@id=\"loginSubmit\"]/div[3]/button[1]")).click();
        return true;
    }

    public List<String> getExtraPlaceEvents(){

        driver.get("https://sports.ladbrokes.com/en-gb/promotions/");
        List<String> extraPlaceEventIds = new ArrayList<>();
        // get a list of promotions
        List<WebElement> promos = driver.findElements(By.className("card-container"));
        // find extra place promotion if it exists
        for(WebElement promo : promos){
            if(promo.getText().contains("EXTRA PLACE")) {
                promo.findElement(By.linkText("MORE INFO")).click();
                // get all of the text
                String[] promoText = driver.findElement(By.cssSelector(".promotion-content,.promotion")).getText().split("\\r?\\n");
                for(String line : promoText){
                    if (line.matches(".*\\s*\\d?\\d:\\d\\d.*")) {
                        System.out.println("Found Event: "+ line.trim());
                        extraPlaceEventIds.add(line.trim());
                    }
                }
                break;
            }
        }
        return extraPlaceEventIds;
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
                List<WebElement> runners = driver.findElement(By.className("race-container")).
                        findElement(By.className("runners")).findElements(By.className("item"));
                for (WebElement runner : runners) {
                    // extract the name and price for each runner and update stored values
                    String name = runner.findElement(By.className("horse")).findElement(By.className("name")).getText();
                    Double[] odds = new Double[2];
                    odds[0] =  ScraperHelper.convertOdds(runner.findElement(By.className("price")).getText());
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
        if(market.equals("BACK-WIN")){
            //todo: implement
            return null;
        }
        else {
            return null;
        }
    }
}
