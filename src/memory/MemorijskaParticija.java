package memory;

import processes.Proces;

import java.util.ArrayList;

public class MemorijskaParticija {
    private int base;
    private int limit;
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
    public void setZauzeto(int zauzeto){
        this.zauzeto = zauzeto;
    }
    public int getSlobodno(){
        return VELICINA - zauzeto;
    }
    public int getBase(){
        return base;
    }
    public int getLimit(){
        return limit;
    }
    public void setLimit(int limit){
        this.limit = limit;

    }
    public void setBase(int base){
        this.base = base;
    }

    @Override
    public String toString() {
        if(this.zauzeto==0){


            String res = "|";
            for(int i = 0; i<this.getVelicina()/10; i++) {
                if( i % 20 == 0 && i!=0)
                    res+="\n ";
                res += ("o");
            }
            res += "|";
            return res;
        }
        int k, z, s;
        k = (VELICINA%10 < 5) ? VELICINA/10 : VELICINA/10+1;
        z = (zauzeto%10 < 5) ? zauzeto/10 : zauzeto/10+1;
        s = k - z;

        String res = "|";
        for(int i = 0; i<z; i++)
            res += "x";
        for(int i = 0; i<s; i++)
            res += " ";
        res += "|\n";
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
