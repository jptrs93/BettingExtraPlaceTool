package App.scraper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import App.model.ExtraPlaceEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



public class LadbrokesScraper extends BaseScraper {

    private HashMap<String, String> eventLinks = new HashMap<>();

    public LadbrokesScraper(){
        bookmaker = "Ladbrokes";
        System.setProperty("webdriver.gecko.driver", "D:/Google Drive/Repos/BettingExtraPlaceTool/otherDependencies/geckodriver.exe");
        System.setProperty("webdriver.firefox.logfile","D:/Google Drive/Repos/BettingExtraPlaceTool/otherDependencies/driver.log");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

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
                // get list of web elements relating to links for extra place races
                List<WebElement> races = driver.findElement(By.className("promotion-content")).findElements(By.tagName("a"));
                // extract the id and url link for each event
                races.forEach(r -> {
                    String temp = r.getText();
                    extraPlaceEventIds.add(temp);
                    eventLinks.put(temp,r.getAttribute("href"));
                });
                break;
            }
        }
        return extraPlaceEventIds;
    }

    @Override
    public void getNewPrices(String eventId){

        // find event in list based on the Id
        Optional<ExtraPlaceEvent> temp = events.stream().filter(e -> e.eventId.equals(eventId)).findFirst();
        if(temp.isPresent()){
            ExtraPlaceEvent event = temp.get();
            // load the event page
            driver.get(event.eventURL);
            // get list web elements for each runner
            List<WebElement> runners = driver.findElement(By.className("runners")).findElements(By.className("item"));
            for(WebElement runner : runners){
                // extract the name and price for each runner and update stored values
                String name = runner.findElement(By.className("horse")).findElement(By.className("name")).getText();
                Float odds = Float.parseFloat(runner.findElement(By.className("price")).getText());
                event.prices.put(name,odds);
            }
            event.timeOfLastPriceUpdate = LocalTime.now();
        }

    }

    @Override
    String getEventURL(String eventId) {
        return eventLinks.get(eventId);
    }

}
