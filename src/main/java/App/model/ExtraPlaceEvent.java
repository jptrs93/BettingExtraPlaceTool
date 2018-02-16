package App.model;

import java.time.LocalTime;
import java.util.HashMap;

public class ExtraPlaceEvent {

    public String eventId;
    public String bookmaker;
    public LocalTime startTime;
    public String name;
    public LocalTime timeOfLastPriceUpdate;
    public String eventURL;
    // key: runner, value: odds
    public HashMap<String, Float> prices;
}
