package file_system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileSystem {
    FileNode root;
    public FileSystem(){
        root = new FileNode("",true);
    }


    public boolean mkdir(String path){
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            return false;
        }
        String[] tokens = path.split("/");
        FileNode curr = root;
        boolean isCreated = false;
        for(int i=1; i<tokens.length; i++){
            if(!curr.children.containsKey(tokens[i])){
                curr.children.put(tokens[i], new FileNode(tokens[i], true));
                isCreated = true;
            }
            curr = curr.children.get(tokens[i]);
        }
        return isCreated;
    }

    public List<String> ls(String path){
        List<String> list = new ArrayList<>();
        FileNode curr = goToCurrDir(path);
        list.addAll(curr.children.keySet());
        Collections.sort(list);
        return list;
    }

    public FileNode goToCurrDir(String path) {
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
        }
        FileNode curr = root;
        String[] tokens = path.split("/");
        for(int i=1; i<tokens.length; i++){
            if(!curr.children.containsKey(tokens[i])){
                throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
            }
            curr = curr.children.get(tokens[i]);
        }
        return curr;
    }

    public void appendToFile(String path, String fileName, String content){
        if(path == null || path.isEmpty() || path.equals("/") || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path. Please provide valid absolute path");
        }
        FileNode curr = goToCurrDir(path).children.get(fileName);
        if(curr==null){
                throw new IllegalArgumentException("File not found");
        }
        curr.content.append(content);
    }

    public void createFile(String path, String fileName, String content){
        FileNode curr = goToCurrDir(path);
        if(curr.children.containsKey(fileName)){
            appendToFile(path, fileName, content);
        }else{
            curr.children.put(fileName, new FileNode(fileName, false));
            appendToFile(path, fileName, content);
        }
    }
    //Print out contents of the file
    public String catFile(String path, String fileName) {
        FileNode curr = goToCurrDir(path);
        return curr.children.get(fileName).content.toString();
    }

    public void loadAssemblerFiles(){

        this.mkdir("/home");
        this.mkdir("/home/Assembler Files");
        File directory = new File("C:\\Users\\PC\\Desktop\\OS-Projekat\\Assembler files");
        for(int i = 0; i<directory.list().length - 1; i++){
            StringBuffer data = new StringBuffer("");
            try {

                Scanner reader = new Scanner(new File("C:\\Users\\PC\\Desktop\\OS-Projekat\\Assembler files\\pr"+(i+1)+".txt"));
                while (reader.hasNextLine()){
                    data.append(reader.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("U suck");
                e.printStackTrace();
            }
            this.createFile("/home/Assembler Files", "pr"+(i+1)+".txt", data.toString());
        }

    }
}
