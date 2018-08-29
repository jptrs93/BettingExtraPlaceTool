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

    public void printData(){
        System.out.print("\n");
        System.out.print("Data for event: "+eventId);
        System.out.print("Bookie: "+bookmaker);
        System.out.print("\n");
        System.out.print(String.format("%-12s %-5s\n","Runner","Price"));
        System.out.print("-------------------------------\n");
        for(String runner : prices.keySet()){
            System.out.print(String.format("%-20s %-8s\n",runner,prices.get(runner).toString()));
        }
        System.out.print("--------------------------------");
    }
}
