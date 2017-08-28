package simulator;

import org.apache.commons.lang3.time.StopWatch;

public class Watch {

    private StopWatch w = new StopWatch();

    public void start(){
        w.start();
    }

    public void stop(){
        w.stop();
        System.out.println(w.toString());
        w.reset();
    }
}
