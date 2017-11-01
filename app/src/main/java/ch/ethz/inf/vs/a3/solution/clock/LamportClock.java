package ch.ethz.inf.vs.a3.solution.clock;


/**
 * Created by ruben on 28.10.17.
 */

public class LamportClock implements Clock{

    private int time;

    public LamportClock() {
        time = 0;
    }

    @Override
    public void update(Clock other) {
        //if(other == null) return;
        LamportClock otherL = (LamportClock) other;
        int temp = Math.max(time, otherL.getTime());
        time = temp;
    }

    @Override
    public void setClock(Clock other) {
        //if(other == null) return;
        LamportClock otherL = (LamportClock) other;
        time = otherL.getTime();
    }

    @Override
    public void tick(Integer pid) {
        time = time + 1;
    }

    @Override
    public boolean happenedBefore(Clock other) {
        LamportClock otherL = (LamportClock) other;
        return time < otherL.getTime();
    }

    @Override
    public String toString() {
        return Integer.toString(time);
    }

    @Override
    public void setClockFromString(String clock) {
        if(clock == null) return;

        try {
            time = Integer.parseInt(clock);
        } catch(NumberFormatException e) {

        }
    }

    //overrides the curret clock value with time
    public void setTime(int t){
        time = t;
    }

    // returns current clock value
    public int getTime() {
        return time;
    }

}
