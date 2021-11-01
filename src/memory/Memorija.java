package memory;

import com.sun.security.jgss.GSSUtil;
import processes.Proces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Memorija {
    private static int VELICINA;
    private static int zauzeto;
    private static ArrayList<MemorijskaParticija> particije;
    private static Queue<Proces> readyQueue = new LinkedList<>();
    private static Proces running_process = null;

    public static void init(){
        VELICINA = 4096;
        zauzeto = 0;
        particije = new ArrayList<MemorijskaParticija>();
        Memorija.napraviParticiju(Memorija.getVelicina());

    }
    public static boolean napraviParticiju(int velicinaParticije){
        if(VELICINA - zauzeto < velicinaParticije)
            return false;
        particije.add(new MemorijskaParticija(velicinaParticije));
        return true;
    }



    public static boolean spojiSlobodneParticije(int i){
        if(i>0 && particije.get(i-1).getProces() == null){
            i--;
        }
        if(i >= particije.size()-1 || particije.get(i+1).getProces()!=null)
            return false;
        MemorijskaParticija nova_particija = new MemorijskaParticija(particije.get(i), particije.get(i+1));
        particije.set(i, nova_particija);
        boolean uslov = true;
        while(uslov){
            uslov = spojiSlobodneParticije(i);
        }
        return true;
    }

    public static boolean spojiSlobodneParticije(MemorijskaParticija particija) {
        int indeks_particije = particije.indexOf(particija);
        return spojiSlobodneParticije(indeks_particije);
    }

    public static boolean razdvojiParticije(int i){
        MemorijskaParticija memorijska_particija = particije.get(i);
        if(memorijska_particija.getVelicina() == memorijska_particija.getZauzeto())
            return false;

        MemorijskaParticija nova_particija1 = new MemorijskaParticija(memorijska_particija.getZauzeto());
        nova_particija1.zauzmiMemoriju(memorijska_particija.getProces());
        MemorijskaParticija nova_particija2 = new MemorijskaParticija(memorijska_particija.getSlobodno());
        particije.set(i, nova_particija1);
        particije.add(nova_particija2);

        spojiSlobodneParticije(i+1);
        return true;
    }

    public static MemorijskaParticija zauzmiParticiju(int index, int velicina) {
        return particije.get(index).zauzmiMemoriju(velicina);
    }
    public static MemorijskaParticija zauzmiParticiju(int index, Proces proces) {
        return particije.get(index).zauzmiMemoriju(proces);
    }

    public static synchronized boolean load(Proces proces){

        MemorijskaParticija mp = SljedeciOdgovarajuci.ucitajProces(proces);
        if(mp == null)
            return false;

        proces.setParticija(mp);
        readyQueue.add(proces);
        proces.setState("READY");
        Memorija.zauzeto = Memorija.zauzeto + mp.getZauzeto();
        System.out.println("MP limit: " + mp.getLimit());
        return true;
    }

    public static void remove(Proces proces) {
        Memorija.zauzeto = Memorija.zauzeto - proces.getParticija().getZauzeto();
        for(int i = 0; i<Memorija.getParticije().size(); i++){
            if (Memorija.getParticije().get(i) == proces.getParticija())
                Memorija.getParticije().get(i).setZauzeto(0);
            System.err.println(Memorija.getParticije().get(i).getZauzeto());
        }
        proces.getParticija().oslobodiMemoriju();
    }


    public static String info() {
        String s = "";
        for(MemorijskaParticija particija:particije)
            s += particija.info() + "| ";
        return s;
    }

    public static String prikazMemorije() {
        System.out.println("Velicina memorije: " + VELICINA);
        System.out.println("Zauzeto: " + zauzeto);
        System.out.println("Slobodno:" + (VELICINA-zauzeto));
        for(MemorijskaParticija mempart: Memorija.getParticije()){
            System.out.println("Particija velicina: " + mempart.getVelicina());
        }
        String res = "";
        int d = 0;
        for(MemorijskaParticija particija:particije) {
            res += particija;
            d += particija.toString().length();
            if(d>90) {
                d = 0;
                res += "\n";
            }
        }
        return res;
    }
    public static String decToBinary(int n){
        String binaryNumber="";
        int[] binaryNum = new int[1000];
        int i = 0;

        while (n > 0) {
            binaryNum[i] = n % 2;
            n = n / 2;
            i++;
        }
        for (int j = i - 1; j >= 0; j--)
            binaryNumber+=String.valueOf(binaryNum[j]);

        return binaryNumber;
    }


    public static int getVelicina() {
        return VELICINA;
    }
    public static ArrayList<MemorijskaParticija> getParticije(){
        return particije;
    }
    public static Proces getRunning_proces(){
        return running_process;
    }
    public static void removeRunningProcess() {
        running_process=null;
    }
    public static void setRunning_process(Proces proces){
        running_process = proces;
    }
    public static Queue<Proces> getReadyQueue(){
        return readyQueue;
    }

    public static int powerOfTwo(int size) {
        int i=1;
        int counter=0;
        while(i<=size) {
            i*=2;
            counter++;
        }
        if (i/2 == size)
            return --counter;
        return -1;
    }

}
