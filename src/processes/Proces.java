package processes;

import memory.Memorija;
import memory.MemorijskaParticija;
import memory.SljedeciOdgovarajuci;

import java.util.ArrayList;
import java.util.Queue;


public class Proces {

        private static ArrayList<Proces> processes = new ArrayList<>();
        private MemorijskaParticija particija;
        private static int counter = 0;
        private int pid;
        private String state;
        private int programCounter;
        private int velicina;
        private String name;
        private ArrayList<String> codeAndData;

        private String file;

        public Proces(ArrayList<String> codeAndData, String name, String file) {
            pid = counter++;
            state = "NEW";
            programCounter = 0;
            this.codeAndData = codeAndData;
            this.name = name;
            this.file = file;
            velicina = codeAndData.size()*16;
            processes.add(this);
            this.particija = null;
            this.init();
        }

        public void init() {
            load();
        }

        public void load() {
            boolean loaded=false;
            while(!loaded) {
                //Load u memoriji

                Memorija.load(this);
                //MemorijskaParticija mp = SljedeciOdgovarajuci.ucitajProces(this);
                //this.particija = mp;
                if(Memorija.load(this))
                    loaded = true;
            }
            if(Memorija.getRunning_proces() == null)
               ProcesScheduler.schedule();
        }
        public void exit() {
            this.state = "TERMINATED";
            //CPU.setToZero();
            //processes.remove(this);
            Memorija.removeRunningProcess();
            Memorija.remove(this);
            //already done in .remove
            //this.particija = null;
            //ProcessScheduler.schedule();
        }
        public static void list() {
            Queue<Proces> readyProcesses = Memorija.getReadyQueue();
            Proces runningProcess = Memorija.getRunning_proces();
            if(runningProcess == null && readyProcesses.isEmpty())
                System.out.println("There are no processes that are currently in ready or running state.");
            else {
                System.out.println("List of processes:");
                if(runningProcess != null) {
                    System.out.println("\tPID: "+runningProcess.pid);
                    System.out.println("\tName: "+runningProcess.name);
                    System.out.println("\tState: "+runningProcess.state);
                    System.out.println("\tSize: "+runningProcess.velicina);
                }
                if(!readyProcesses.isEmpty()) {
                    for(Proces proces : readyProcesses) {
                        System.out.println("\tPID: "+ proces.pid);
                        System.out.println("\tName: "+ proces.name);
                        System.out.println("\tState: "+ proces.state);
                        System.out.println("\tSize: "+ proces.velicina);
                    }
                }
                System.out.println();
            }
        }
        public int getPid() {
            return pid;
        }
        public String getFile() {
            return file;
        }
        public void setState(String state) {
            this.state=state;
        }

        public int getVelicina(){
            return velicina;
        }
        public MemorijskaParticija getParticija(){
            return particija;
        }
        public void setParticija(MemorijskaParticija p){
            this.particija = p;
    }

}
