package cpu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import memory.Memorija;
import processes.Proces;

public class CPU {
    private static Register IR = new Register();
    private static Register PC = new Register();
    private static Register R1 = new Register("R1", "10");
    private static Register R2 = new Register("R2", "11");
    private static Register R3 = new Register("R3", "01");
    private static Register F = new Register("Flags", "00");
    private static ArrayList<String> pageTable = new ArrayList<String>();
    private static Proces currentProcess;
    private static boolean executing;

}