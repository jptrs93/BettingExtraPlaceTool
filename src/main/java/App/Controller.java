package App;


import App.scraper.LadbrokesScraper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.List;
import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

public class Controller {

    public static void main(String args[]){

        println("Starting browser:");
        LadbrokesScraper ladbrokesScraper = new LadbrokesScraper();

        // Find any extra place events and add them to the events list
        List<String> eventIds = ladbrokesScraper.getExtraPlaceEvents();
        if(eventIds.size() > 0){
            System.out.println("Found the following events:");
            eventIds.forEach(e -> {
                System.out.print(e);
                ladbrokesScraper.addEvent(e);
                ladbrokesScraper.getNewPrices(e);
            });
        }
        else{
            System.out.println("Found no events");
            System.out.print(System.getProperties());
        }
        // get prices for events



        Scanner scanner = new Scanner(System.in);
        String answer;
        System.out.println("Close Browser?");
        while(true){
            if(scanner.hasNext()){
                answer = scanner.next();
                if(answer.equals("y")){
                    break;
                }
            }
        }
        ladbrokesScraper.closeDriver();
    }
}
