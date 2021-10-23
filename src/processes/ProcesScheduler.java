package processes;

import memory.Memorija;

import java.util.Queue;

public class ProcesScheduler {
    public static synchronized void schedule() {
        Queue<Proces> readyQueue = Memorija.getReadyQueue();
        if(!readyQueue.isEmpty() && Memorija.getRunning_proces() == null) {
            Proces proces = readyQueue.remove();
            Memorija.setRunning_process(proces);
            proces.setState("RUNNING");
            //TODO
            //new ExecutionThread(process);

        }
    }

}
