package kernel;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import assembler.Assembler;
//import cpu.CPU;
import assembler.Assembler1;
import file_system.FileSystem;
import memory.Memorija;
import memory.MemorijskaParticija;
import processes.Proces;

public class Kernel {
    private Scanner scan;
    private final static String[] commands= {"exe", "exit", "list","print"};
    private static FileSystem fs;


    //Needs to be cleaned up
    public Kernel() {
        FileSystem fs = new FileSystem();
        fs.mkdir("/home");
        scan=new Scanner(System.in);
        Memorija.init();
        this.start2();
    }
    public void start() {
        Memorija.prikazMemorije();
        try {
            while(scan.hasNextLine()) {
                String command=scan.nextLine();
                if(isValid(command)){
                    executeCommand(command);
                    Memorija.prikazMemorije();}
                //else if(isValidFs(command)){
                //}
                else

                    System.out.println("Error! '"+command.split(" ")[0]+"' is not recognized as a command!");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void start2() {
        String[]list= {"exe pr1.txt","exe pr2.txt res.txt","exe pr3.txt res.txt",
                "exe pr4.txt res.txt","exe pr5.txt res.txt","list"};
        /*
        for(int i=0; i<list.length; i++)
            executeCommand(list[i]);

         */
        executeCommand(list[4]);
        //executeCommand(list[0]);
        executeCommand(list[4]);
        executeCommand(list[1]);
        //executeCommand(list[1]);
        //executeCommand(list[1]);
        executeCommand(list[5]);
        start();
    }

    //Creates a new Process (Or executes other commands)
    public static void executeCommand(String command) {
        String[]list=command.split(" ");
        if(list[0].equals(commands[0]) && (list.length == 3 || list.length == 2)) {
            File file=new File(list[1]);
            if(file.exists()) {
                //ArrayList<String>codeAndData = new ArrayList<>();
                ArrayList<String> codeAndData = Assembler1.convert(list[1]);
                System.out.println();
                System.out.println(codeAndData);
                int index=list[1].indexOf('.');
                String name=list[1].substring(0,index)+".asm";
                if(list.length == 3)
                    new Proces(codeAndData,name,list[2]);
                else
                    new Proces(codeAndData,name,null);
            }else {
                System.out.println("Error! File '"+list[1]+"' does not exist!");
            }
        }
        else if(list[0].equals(commands[1]) && list.length == 1);
            //exit();

        else if(list[0].equals(commands[2]) && list.length == 1)
            Proces.list();

        else if(list[0].equals(commands[3]) && list.length == 1)
            System.out.println(Memorija.prikazMemorije());

        else
            System.out.println("Error! Invalid parameters!");
    }
    private boolean isValid(String command) {
        String[]list=command.split(" ");
        if(!list[0].equals(commands[0]) && !list[0].equals(commands[1]) && !list[0].equals(commands[2]) && !list[0].equals(commands[3]))
            return false;
        return true;
    }
    /*
    private boolean isValidFs(String command){
        if(command == null || command.isEmpty() || command.equals("/")) {
            System.out.println("Pogresna komanda!");
            return false;
        }
        String command1 = command.split(" ")[0];
        System.out.println(command1);
        if(command1 .equals("mkdir") ){
            if(fs.mkdir(command.split(" ")[1]))
                System.out.println("Direktorij uspjesno kreiran!");
            else{
                System.out.println("Greska u putanji!");

                return false;
            }

        }
        else if(command1.equals("ls")){
            String command2 = command.split(" ")[1];
            System.out.println(fs.ls(command2));
            return true;
        }
        else if (command1.equals("createFile")){
            fs.createFile(command.split(" ")[1], command.split(" ")[2], command.split(" ")[3]);
            System.out.println("Created a file!");
            return true;

        }
        else if (command1.equals("append")  ){
            fs.appendToFile(command.split(" ")[1], command.split(" ")[2], command.split(" ")[3]);
            return true;
        }
        else if(command1.equals("read")){
            System.out.println(fs.catFile(command.split(" ")[1], command.split(" ")[2]));
            return true;
        }
        return false;
    }*/
    public void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }


    public static void main(String[]args) {

        Kernel cmd = new Kernel();
    }
}
