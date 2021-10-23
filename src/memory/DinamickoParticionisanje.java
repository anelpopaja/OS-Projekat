package memory;

import java.util.ArrayList;

public class DinamickoParticionisanje {
    private ArrayList<Proces> proces;

    public DinamickoParticionisanje(){
        Memorija.init();

        MemorijskaParticija mp = SljedeciOdgovarajuci.ucitajProces(neaktivniProcesi.get(i));

        if(mp != null) {
            System.out.println("Pokrenut proces " + neaktivniProcesi.get(i).getNaziv());
            Memorija.razdvojiParticije(mp);
        }
        else {
            System.out.println("Pokrenanje procesa " + neaktivniProcesi.get(i).getNaziv() + " nije uspjelo");

			/*
			ArrayList<MemorijskaParticija> zauzeteParticije = MemorijskaParticija.getZauzeteParticije();
			int brZauzetihParticija = zauzeteParticije.size();
			i = rand.nextInt(brZauzetihParticija);
			zauzeteParticije.get(i).oslobodiMemoriju();
			Memorija.spojSlobodneParticije(zauzeteParticije.get(i));
			*/
        }

    }
}
