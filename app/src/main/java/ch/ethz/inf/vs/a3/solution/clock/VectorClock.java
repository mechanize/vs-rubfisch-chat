package ch.ethz.inf.vs.a3.solution.clock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by ruben on 28.10.17.
 */

public class VectorClock implements Clock{

    private Map<Integer,Integer> vector;

    public VectorClock() {
        vector = new HashMap<Integer, Integer>();
    }

    @Override
    public void update(Clock other) {
        VectorClock clockV = (VectorClock) other;
        Map<Integer,Integer> oMap = clockV.getVect();

        for(int k: oMap.keySet()) {
            if(vector.containsKey(k)) {
                vector.put(k, Math.max(vector.get(k), clockV.getTime(k)));
            } else {
                vector.put(k,clockV.getTime(k));
            }
        }
    }

    @Override
    public void setClock(Clock other) {
        VectorClock clockV = (VectorClock) other;
        Map<Integer,Integer> oMap = clockV.getVect();

        for(int k: oMap.keySet()) {
            vector.put(k,clockV.getTime(k));
        }
    }

    @Override
    public void tick(Integer pid) {
        vector.put(pid,vector.get(pid) + 1);
    }

    @Override
    public boolean happenedBefore(Clock other) {
        //is true if one or more process times are strictly smaller
        boolean strictlySmaller = false;
        boolean smaller = true;

        VectorClock clockV = (VectorClock) other;

        Set<Integer> intersection = new HashSet<Integer>(vector.keySet());
        intersection.retainAll(clockV.getVect().keySet());

        boolean tempS;
        for(int k: intersection){
            tempS = vector.get(k) <= clockV.getTime(k);
            if(vector.get(k) < clockV.getTime(k)) {
                strictlySmaller = true;
            }
            smaller = smaller && tempS;
        }

        return smaller && strictlySmaller;
    }

    @Override
    public void setClockFromString(String clock) {
        if(clock.equals(new String("{}"))) {
            vector = new HashMap<Integer, Integer>();
            return;
        }

        Map<Integer,Integer> newVec = new HashMap<Integer, Integer>();

        //replace the brackets with spaces
        String content = clock.replace("{"," ");
        content = content.replace("}"," ");

        //trim the spaces
        content = content.trim();

        //split the string into entries
        String[] entries = content.split(",");

        int k = 1;
        int v = 1;
        String cutV;
        boolean failure = false;

        try {
            //add entries to hashmap
            for(String entry : entries) {
                String[] ent = entry.split(":");
                v = Integer.parseInt(ent[1]);
                cutV = ent[0].replace('"',' ');
                cutV = cutV.trim();
                k = Integer.parseInt(cutV);

                newVec.put(k,v);
            }
        } catch(Exception e) {
            failure = true;
        }
        finally {
            if(failure) {
                return;
            }
            else {
                vector = newVec;
            }
        }

    }

    @Override
    public String toString() {
        String inner = "";

        for(Map.Entry<Integer,Integer> entry : vector.entrySet()) {
            inner = inner + "," + '"' + entry.getKey() + '"' + ':' + entry.getValue();
        }

        //if not an empty string, remove first comma
        if(inner.length() > 0)
            inner = inner.substring(1);

        return '{' + inner + '}';
    }

    //returns time for a process pid
    public int getTime(Integer pid){
        if(vector.containsKey(pid))
            return vector.get(pid);
        return -3;
    }

    //adds a new process and its vector clock
    public void addProcess(Integer pid, int time) {
        vector.put(pid,time);
    }

    public Map<Integer,Integer> getVect() {
        return vector;
    }
}
