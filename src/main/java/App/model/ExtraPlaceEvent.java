package App.model;

import java.time.LocalTime;
import java.util.HashMap;

public class ExtraPlaceEvent {

    public String market = "BACK-WIN";
    public String eventId;
    public String bookmaker;
    public LocalTime startTime;
    public LocalTime timeOfLastPriceUpdate;
    public String eventURL;
    // key: runner, value: odds
    public HashMap<String, Double[]> prices = new HashMap<>();
}
