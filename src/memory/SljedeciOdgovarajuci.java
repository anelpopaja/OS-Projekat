package memory;

import processes.Proces;

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
            Memorija.razdvojiParticije(odgovarajuci_indeks);
            return particija_procesa;
        }
        //Ako je prvi proces
        if(odgovarajuceParticije.size() > 0) {
            MemorijskaParticija particija_procesa = Memorija.zauzmiParticiju(0, proces);
            Memorija.razdvojiParticije(0);
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
