package App.model;

import sun.util.resources.cldr.lag.LocaleNames_lag;

import java.time.LocalTime;
import java.util.List;

public class TimeManager {

    public static boolean isEventStartingSoon(LocalTime startTime){
        return LocalTime.now().plusHours(1).isAfter(startTime);
    }


    public static LocalTime calculateSleep(List<LocalTime> startTimes){

        return LocalTime.now().plusSeconds(30);
    }

}
