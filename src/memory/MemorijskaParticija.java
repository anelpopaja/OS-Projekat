package memory;

import processes.Proces;

import java.util.ArrayList;

public class MemorijskaParticija {
    private Proces proces;
    private final int VELICINA;
    private int zauzeto;


    public MemorijskaParticija(int velicina){
        this.VELICINA = velicina;
        zauzeto = 0;
    }

    public MemorijskaParticija( MemorijskaParticija p1, MemorijskaParticija p2){
        VELICINA = p1.getVelicina() + p2.getVelicina();
        zauzeto = 0;
    }

    public MemorijskaParticija zauzmiMemoriju(Proces proces){
        oslobodiMemoriju();
        if(proces.getVelicina() > this.VELICINA)
            return null;

        this.proces = proces;
        zauzeto = proces.getVelicina();
        return this;
    }
    public MemorijskaParticija zauzmiMemoriju(int velicina){
        if(velicina > this.VELICINA)
            return null;

        zauzeto = velicina;
        return this;
    }
    public void oslobodiMemoriju(){
        if(this.proces!=null){
            this.proces.setParticija(null);
            this.proces = null;
            this.zauzeto = 0;
        }
    }


    public Proces getProces() {
        return proces;
    }
    public int getVelicina() {
        return VELICINA;
    }
    public int getZauzeto() {
        return zauzeto;
    }
    public int getSlobodno(){
        return VELICINA - zauzeto;
    }

    @Override
    public String toString() {
        int k, z, s;
        k = (VELICINA%10 < 5) ? VELICINA/10 : VELICINA/10+1;
        z = (zauzeto%10 < 5) ? zauzeto/10 : zauzeto/10+1;
        s = k - z;

        String res = "|";
        for(int i = 0; i<z; i++)
            res += "x";
        for(int i = 0; i<s; i++)
            res += " ";
        res += "|";
        return res;
    }
    public String info() {
        String nazivProcesa = (proces==null) ? "N.P." : proces.getNaziv();
        return String.format("Naziv procesa: %s; velicina: %d; (zauzeto: %d; slobodno: %d)", nazivProcesa, VELICINA, zauzeto, VELICINA - zauzeto);
    }








    //Trazimo slobodne odgovarajuce particije
     public static ArrayList<MemorijskaParticija> getSlobodneParticije(Proces proces) {
        ArrayList<MemorijskaParticija> slobodneParticije = new ArrayList<>();
        for(MemorijskaParticija particija: Memorija.getParticije())
            if(particija.getZauzeto() == 0 && particija.getVelicina() >= proces.getVelicina() && particija.getProces() == null)
                slobodneParticije.add(particija);
        return slobodneParticije;
    }

    //Trazimo zauzete odgovarajuce particije
     public static ArrayList<MemorijskaParticija> getOdgovarajuceParticije(Proces proces) {
        ArrayList<MemorijskaParticija> odgovarajuceParticije = new ArrayList<>();
        for(MemorijskaParticija particija: Memorija.getParticije())
            if(particija.getVelicina() >= proces.getVelicina())
                odgovarajuceParticije.add(particija);
        return odgovarajuceParticije;
    }

     public static ArrayList<MemorijskaParticija> getZauzeteParticije() {
        ArrayList<MemorijskaParticija> zauzeteParticije = new ArrayList<>();
        for(MemorijskaParticija particija: Memorija.getParticije())
            if(particija.getProces() != null)
                zauzeteParticije.add(particija);
        return zauzeteParticije;
    }
}
