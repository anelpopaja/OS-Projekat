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
            proces.ucitavanjeProcesa(Memorija.getParticije().get(odgovarajuci_indeks));

            /*
                public boolean ucitavanjeProcesa(int indexParticije) {
                    this.particija = Memorija.zauzmiParticiju(indexParticije, this);
                    if(particija == null)
                        return false;
                    return true;
                    }

                    public boolean ucitavanjeProcesa(MemorijskaParticija particija) {
                        this.particija = particija;
                        particija.zauzmiMemoriju(this);
                        if(this.particija == null)
                            return false;
                        return true;
                }
                */
            return Memorija.getParticije().get(odgovarajuci_indeks);
        }

        if(odgovarajuceParticije.size() > 0) {
            pokazivac = Memorija.getParticije().indexOf(odgovarajuceParticije.get(0));
            proces.ucitavanjeProcesa(Memorija.getParticije().get(pokazivac));
            return Memorija.getParticije().get(pokazivac);
        }

        // nema slobodnih memorijskih particija, sada posmatramo dovoljno velike particije koje nisu slobodne
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

        return null;

    }

}
