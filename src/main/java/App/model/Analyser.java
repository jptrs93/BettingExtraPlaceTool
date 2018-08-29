package App.model;

import java.util.ArrayList;
import java.util.List;

public class Analyser {


    public BetInstruction analyse(List<ExtraPlaceEvent> eventData) {
        // For now we will assume some standard rules about the races:
        //
        // 5-7 runners =  2 standard places (so 3 with extra place). Assume fraction to be 2/5
        // 8+ runner = 3 standard places (so 4 with the extra place).  Assume fraction to be 2/5



        // check if there is sufficient data to make analysis
        List<String> runners = new ArrayList<>();
        // check that the names match across the events
        double eachWayFactor = 2/5;
        int no_places = 3;
        int no_runners = runners.size();
        for(String runner : runners){

            Double[] prices = new Double[3];
            prices[0] = getPrice(runner,"LAY-WIN", eventData) - 1; // lay win price
            prices[1] = getPrice(runner, "LAY-PLACE", eventData)*0.95 - 0.95; // lay place price
            prices[2] = getPrice(runner, "BACK-WIN", eventData)*0.95 - 0.95; // back each way price
            Double[] probabilities =  new Double[4];

            probabilities[0] = 1 - 1 / prices[0];     // win scenario probability
            probabilities[1] = 1 - 1 / prices[1] - probabilities[0];   // place scenario probability
            probabilities[2] = 1 / (prices[2] * (no_runners - no_places));    // extra place scenario probability
            probabilities[3] = 1 - probabilities[1] - probabilities[2] - probabilities[0];     //  lose scenario probability

            Double bets[] = new Double[3];
            bets[0] = 10.0;
            getStakes(bets,prices,probabilities,eachWayFactor);

        }

        // for each runner
            // start with 5 pound stake on win market
            // minimise qualifying loss
            // Calculate return and estimated probability if horse wins (correct probability for over-round)
            // Calculate return and estimated probability if horse comes in top standard places
            // Calculate return and estimated probability if horse comes in extra place
            // Calculate return and estiamted probability if horse loses



        //todo: implement analysis

        return null;
    }
    /*
    Provides the overall expected return
     */
    private Double getExpectedReturn(Double[] b, Double[] x, Double[] p, Double f){
        return p[0]*(x[0]*b[0]  - b[1]      -b[2])
                + p[1]*(x[0]*f*b[0]+ b[1]*x[1] -b[2])
                + p[2]*(x[0]*f*b[0]+ b[1]*x[1] +b[2]*x[2])
                + p[3]*(-b[0]      + b[1]*x[1] +b[2]*x[2]);
    }

    /*
    Provides the return for each of the 4 possible scenarios
    */
    private Double[] getReturns(Double[] b, Double[] x,  Double f){
        Double[] returns =  new Double[4];
        returns[0] = x[0]*b[0]  - b[1]      -b[2];
        returns[1] = x[0]*f*b[0]+ b[1]*x[1] -b[2];
        returns[2] = x[0]*f*b[0]+ b[1]*x[1] +b[2]*x[2];
        returns[3] = -b[0]      + b[1]*x[1] +b[2]*x[2];
        return returns;
    }


    /*
    Works out the 2nd and 3d stakes by minimising the variance of expected returns
     */
    private void getStakes(Double[] b, Double[] x, Double[] p, Double f){

        // solve for b2 and b3 where others are known
        // a1 b2 + d1 b3 + c1 = 0
        // a2 b2 + d2 b3 + c2 = 0

        // a2 a1 b2 + a2 d1 b3 + a2 c1 = 0
        // a1 a2 b2 + a1 d2 b3 + a1 c2 = 0

        // (a2 d1 - a1 d2) b3 + (a2 - a1) c = 0
        // b3 = - (a2 c1 - a1 c2) /(a2 d1 - a1 d2)
        // b2 =  -(d1 b3 + c1)/a1


        Double a1 = 2*p[0] + 2*x[1]*x[1]*p[1] + 2*x[1]*x[1]*p[2]+ 2*x[1]*x[1]*p[3];
        Double d1 = 2*p[0] - 2*x[1]*p[1] + 2*x[1]*x[2]*p[2] + 2*x[1]*x[2]*p[3];
        Double c1 = -2*x[0]*b[0]*p[0] + 2*x[0]*x[1]*f*b[0]*p[1] + 2*x[1]*x[0]*f*b[0]*p[2] -p[3]*2*x[1]*b[0] +p[0] -x[1]*(p[1]+p[2]+p[3]);

        Double a2 = 2*p[0] - 2*x[1]*p[1] + 2*x[1]*x[2]*p[2]+ 2*x[1]*x[2]*p[3];
        Double d2 = 2*p[0] + 2*p[1] + 2*x[2]*x[2]*p[2] + 2*x[2]*x[2]*p[3];
        Double c2 = -2*x[0]*b[0]*p[0] - 2*x[0]*f*b[0]*p[1] + 2*x[2]*x[0]*f*b[0]*p[2] -p[3]*2*x[2]*b[0] +p[0]+p[1] -x[2]*(p[2]+p[3]);

        Double b3 = - (a2*c1 - a1*c2) /(a2*d1 - a1*d2);
        Double b2 = -(d1*b3 + c1)/a1;
        b[1] = b2;
        b[2] = b3;
    }

    private Double getPrice(String runner, String market, List<ExtraPlaceEvent> eventData) {
        return 2.0;
    }
}
