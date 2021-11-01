package memory;

import processes.Proces;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class SljedeciOdgovarajuci {
    private static int pokazivac = 0;

    public static MemorijskaParticija ucitajProces(Proces proces){
        int odgovarajuci_indeks = -1;
        ArrayList<MemorijskaParticija> odgovarajuceParticije = MemorijskaParticija.getSlobodneParticije(proces);

        for(MemorijskaParticija mp: odgovarajuceParticije)
            if(Memorija.getParticije().indexOf(mp) > pokazivac) {
                odgovarajuci_indeks = Memorija.getParticije().indexOf(mp);
                pokazivac = odgovarajuci_indeks;
                break;
            }

        if(odgovarajuci_indeks != -1) {

            MemorijskaParticija particija_procesa = Memorija.zauzmiParticiju(odgovarajuci_indeks, proces);
            System.out.println(odgovarajuci_indeks);
            Memorija.razdvojiParticije(odgovarajuci_indeks);
            MemorijskaParticija temp = Memorija.getParticije().get(odgovarajuci_indeks-1);
            System.out.println("~~~~~~~~~~velicina prethodni~~~~~~~~~~~~~~");
            System.out.println(temp.getVelicina());
            //TODO zasto ne valja?   particija_procesa.setBase(temp.getLimit()+1);
            particija_procesa.setBase(temp.getVelicina());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Setting base to: "+ temp.getVelicina());
            System.out.println(temp);
            System.out.println("Printing out partitions: ");
            particija_procesa.setLimit(particija_procesa.getBase()+proces.getVelicina());

            System.out.println("Particija_procesa base: " + particija_procesa.getBase());
            System.out.println("Particija_procesa limit: " + particija_procesa.getLimit());
            for(MemorijskaParticija mp : Memorija.getParticije())
                System.out.println(mp);
            return particija_procesa;
        }
        //Ako je prvi proces
        if(odgovarajuceParticije.size() > 0) {
            MemorijskaParticija particija_procesa = Memorija.zauzmiParticiju(0, proces);
            Memorija.razdvojiParticije(0);
            System.out.println("Setting base to 0 ~~~~~~~~~~~~~");
            particija_procesa.setBase(0);
            particija_procesa.setLimit(proces.getVelicina());
            System.out.println("Limit newwww ~~~~~~~" + particija_procesa.getLimit());
            return particija_procesa;
        }
        //TODO
        // nema slobodnih memorijskih particija, sada posmatramo dovoljno velike particije koje nisu slobodne
        /*
        odgovarajuceParticije = MemorijskaParticija.getOdgovarajuceParticije(proces);
        for(MemorijskaParticija mp: odgovarajuceParticije)
            if(Memorija.getParticije().indexOf(mp) > pokazivac) {
                odgovarajuci_indeks = Memorija.getParticije().indexOf(mp);
                pokazivac = odgovarajuci_indeks;
                break;
            }

        if(odgovarajuci_indeks != -1) {
            proces.ucitavanjeProcesa(Memorija.getParticije().get(odgovarajuci_indeks));
            return Memorija.getParticije().get(odgovarajuci_indeks);
        }

        if(odgovarajuceParticije.size() > 0) {
            pokazivac = Memorija.getParticije().indexOf(odgovarajuceParticije.get(0));
            proces.ucitavanjeProcesa(Memorija.getParticije().get(pokazivac));
            return Memorija.getParticije().get(pokazivac);
        }

    */
        return null;

    }
}
