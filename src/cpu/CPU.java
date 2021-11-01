package cpu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import memory.Memorija;
import processes.Proces;

public class CPU {
    private static Register IR = new Register();
    public static int d = 0;
    public static Register PC = new Register();             //TODO vratiti na private
    public static Register R1 = new Register("R1", "10");
    public static Register R2 = new Register("R2", "11");
    public static Register R3 = new Register("R3", "01");
    private static Proces currentProcess;
    private static boolean executing;
    private static boolean timeout = false;   //TODO ovo mozda u ProcessScheduler klasu...
    private static long prvoVrijemeMillis;
    private static long drugoVrijemeMillis;


    public static void execute(Proces process) {

        prvoVrijemeMillis=System.currentTimeMillis();
        System.out.println("~~++~~++~~++~~++~~+++");
        System.out.println(":::::::VRIJEME:::::");
        System.out.println(prvoVrijemeMillis);  //kad se ucita proces uzme svakako prvo vrijeme

        executing=true;
        System.out.println("Process "+process.getPid()+" started its execution.");
        CPU.currentProcess=process;

        PC.setValue(process.trenutniPC);
        d = process.trenutnid;
        R1.setValue(process.trenutniR1);
        R2.setValue(process.trenutniR2);
        R3.setValue(process.trenutniR3);

        while(executing) {

            if(d < currentProcess.codeAndData.size()) {
                String instruction = currentProcess.codeAndData.get(d);
                //System.out.println("~~~~~~~EXE~~~~~~~~~");
                //System.out.println(instruction);
                //System.out.println(PC.getValue());
                IR.setValue(instruction);
                PC.increment();
                executeInstruction(IR.getValue());
            } else
                break;
            d++;
        }
    }

    public static void executeInstruction(String instruction) {
        drugoVrijemeMillis=System.currentTimeMillis(); //TODO prije izvrsavanja svake instrukcije izmjeri drugo vrijeme, ako je qvant prevelik, oznaci timeout=true
        System.out.println("~~++~~++~~++~~++~~+++");
        System.out.println(":::::::VRIJEME:::::");
        System.out.println(drugoVrijemeMillis);
        if(drugoVrijemeMillis-prvoVrijemeMillis>3) {
            timeout = true;
            prvoVrijemeMillis = drugoVrijemeMillis;
        } else {
            timeout = false;
            prvoVrijemeMillis = drugoVrijemeMillis;
        }

        String opCode=instruction.substring(0,4);
        if(timeout){
            currentProcess.contextSwitch();
        } else {
            if (opCode.equals("0000")) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("The result of process " + currentProcess.getPid() + ": " + Integer.parseInt(R1.getValue(), 2));
                if (currentProcess.getFile() != null)
                    writeToFile();
                executing = false;
                //printRM();
                System.out.println("Process " + currentProcess.getPid() + " has completed its execution.");
                System.out.println();
                currentProcess.exit();
            } else if (opCode.equals("0001")) {  //TODO JMP
                String newInstruction = "";
                String address = instruction.substring(4);
                //TODO ovdje vidjeti sta radi i da li mora ovo ...vjerovatno ne
                int length = Memorija.powerOfTwo(Memorija.getVelicina()) - address.length();
                for (int i = 0; i < length; i++) {
                    newInstruction += "0";
                }
                newInstruction += address;
                PC.setValue(newInstruction);
            } else if (opCode.equals("0010")) { //LOAD
                String register = instruction.substring(4, 6);
                String memoryLocation = instruction.substring(6);
                int length = Memorija.powerOfTwo(Memorija.getVelicina());
                String dataLocation = "";
                for (int i = 0; i < length - memoryLocation.length(); i++) {
                    dataLocation += "0";
                }
                dataLocation += memoryLocation;
                //TODO povecati prostor memorije za podatke da nije samo 4 bita....
                String offsetForData = dataLocation.substring(4, 8);
                String bitoviOffset[] = offsetForData.split("");
                int offset = 0;
                if (Integer.parseInt(bitoviOffset[0]) == 1) offset += 8;
                if (Integer.parseInt(bitoviOffset[1]) == 1) offset += 4;
                if (Integer.parseInt(bitoviOffset[2]) == 1) offset += 2;
                if (Integer.parseInt(bitoviOffset[3]) == 1) offset += 1;


                System.out.println(offsetForData);
                System.out.println(offset);
                //System.out.println("Datalocation ~~~~~~~`" + dataLocation);

                String data = currentProcess.codeAndData.get(offset);

                //System.out.println("~~~~~~~~~`data~````````");
                //System.out.println(data);

                if (register.equals(R1.getAddress()))
                    R1.setValue(data);
                else if (register.equals(R2.getAddress()))
                    R2.setValue(data);
                else
                    R3.setValue(data);
            }
            //TODO vidjeti sta cemo za store ali ne treba nam....zasad...

            //ADD    SUB   MUL DIV
            else if (opCode.equals("0100") || opCode.equals("0101") || opCode.equals("0110") || opCode.equals("0111")) {
                String register1 = instruction.substring(8, 10);
                String register2 = instruction.substring(14);
                String data1 = "";
                String data2 = "";

                if (register1.equals(R1.getAddress()))
                    data1 = R1.getValue();
                else if (register1.equals(R2.getAddress()))
                    data1 = R2.getValue();
                else
                    data1 = R3.getValue();

                if (register2.equals(R1.getAddress()))
                    data2 = R1.getValue();
                else if (register2.equals(R2.getAddress()))
                    data2 = R2.getValue();
                else
                    data2 = R3.getValue();

                int dataNumber1 = Integer.parseInt(data1, 2);
                int dataNumber2 = Integer.parseInt(data2, 2);
                int result = 0;

                if (opCode.equals("0100"))
                    result = dataNumber1 + dataNumber2;
                else if (opCode.equals("0101"))
                    result = dataNumber1 - dataNumber2;
                else if (opCode.equals("0110"))
                    result = dataNumber1 * dataNumber2;
                else
                    result = dataNumber1 / dataNumber2;

                String binaryNumber = "";
                if (result == 0)
                    binaryNumber = "0";
                else
                    binaryNumber = Memorija.decToBinary(result);

                if (register1.equals(R1.getAddress()))
                    R1.setValue(binaryNumber);
                else
                    R2.setValue(binaryNumber);
            }
        }
    }

    public static void setToZero() {
        R1.setValue("");
        R2.setValue("");
        R3.setValue("");
        PC.setValue("0");
        IR.setValue("");
    }
    public static void writeToFile(){
        String result=R1.getValue();
        String file=currentProcess.getFile();
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(String.valueOf(Integer.parseInt(result,2)));
            myWriter.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void print() {
        System.out.println("State of memory and registers:");
        System.out.println();
        System.out.println("IR: "+IR.getValue());
        System.out.println("PC: "+PC.getValue());
        System.out.println("R1: "+R1.getValue());
        System.out.println("R2: "+R2.getValue());
        System.out.println("R3: "+R3.getValue());
    }
    /*
    public static void printRM() {
        System.out.println("State of memory and registers:");
        System.out.println();
        System.out.print("IR:"+binaryToHex(IR.getValue())+" ");
        System.out.print("PC:"+binaryToHex(PC.getValue())+" ");
        System.out.print("R1:"+binaryToHex(R1.getValue())+" ");
        System.out.print("R2:"+binaryToHex(R2.getValue())+" ");
        System.out.print("R3:"+binaryToHex(R3.getValue()));
        System.out.println();
    }
    public static String binaryToHex(String value) {
        try {
            int number=Integer.parseInt(value,2);
            String hex=Integer.toHexString(number);
            return hex;
        }catch(Exception e) {
            return null;
        }
    }*/

}