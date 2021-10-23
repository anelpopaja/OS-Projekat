import file_system.FileSystem;
import memory.DinamickoParticionisanje;
import memory.Memorija;
import processes.Proces;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main extends Thread{

    public static void main(String[] args) {
        DinamickoParticionisanje d = new DinamickoParticionisanje();
        ArrayList<String> codeAndData = new ArrayList<>();
        String file = "random text sad";

        Proces p1 = new Proces(codeAndData, "p1", file);
        Proces p2 = new Proces(codeAndData, "p2", file);
        Proces p3 = new Proces(codeAndData, "p3", file);
        try{

            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }
        Proces.list();
        System.out.println(Memorija.prikazMemorije());
    }

}
