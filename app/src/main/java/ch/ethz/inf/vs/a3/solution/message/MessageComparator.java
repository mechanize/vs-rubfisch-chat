package ch.ethz.inf.vs.a3.solution.message;


import java.util.Comparator;
import ch.ethz.inf.vs.a3.solution.clock.VectorClock;
import ch.ethz.inf.vs.a3.clock.VectorClockComparator;

/**
 * Message comparator class. Use with PriorityQueue.
 */
public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message lhs, Message rhs) {
        String TSL = lhs.timestamp; //tochange
        String TSR = rhs.timestamp; //tochange
        VectorClock Clockl = new VectorClock();
        VectorClock Clockr = new VectorClock();
        Clockl.setClockFromString(TSL);
        Clockr.setClockFromString(TSR);
        VectorClockComparator vc = new VectorClockComparator();
        return vc.compare(Clockl,Clockr);
    }

}
