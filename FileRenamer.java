import java.io.*;
import java.util.*;

import javax.swing.JTextArea;

import java.lang.StringBuffer;
import java.io.PrintStream;

public class FileRenamer{

    //directory field
    private String dir;
    private ArrayList<String> al;

    //constructor, getter, setter
    public String getDirectory(){return dir;}
    public void setDirectory(String directory){this.dir = directory;}

    //renamer tool
    public void rename(ArrayList<String> newFileNames){
        ArrayList<File> listOfFiles = originalFiles();

        for(int i = 0; i < listOfFiles.size(); i++){
            if(listOfFiles.get(i).isFile()){
                File f = new File(dir + listOfFiles.get(i).getName());
                f.renameTo(new File(dir + newFileNames.get(i) + extensionList().get(i)));
            }
        }
    }

    //rename Confirm Tool:allows user to confirm if new filenames are satisfactory
    public void renameConfirmTool(ArrayList<String> newFileNames){
        ArrayList<File> listOfFiles = originalFiles();
        ArrayList<String> listOfExtensions = extensionList();

        System.out.println("New File Names: ");
        for(int i = 0; i < listOfFiles.size(); i++){
            if(listOfFiles.get(i).isFile()){
                System.out.println(newFileNames.get(i) + listOfExtensions.get(i));
            }
        }
    }

    //Collects original files in ArrayList of files
    public ArrayList<File> originalFiles(){
        ArrayList<File> files = new ArrayList<>();
        File folder = new File(dir);
        File[] fileList = folder.listFiles();

        for(int i = 0; i < fileList.length; i++){
            if(fileList[i].isFile()){
                files.add(fileList[i]);
            }
        }
        Collections.sort(files);
        return files;
    }

    //collects the filename without extension, used to
    //append in rename tool
    public ArrayList<String> filenameList(){
        ArrayList<File> files = originalFiles();
        al = new ArrayList<>();
        ArrayList<String> al2 = new ArrayList<>();

        for(int i = 0; i < files.size(); i++){
            if(files.get(i).isFile()){
                al.add(files.get(i).getName().toString());
            }
        }
        //removes extension and keeps only the name of the file
        for(int i = 0; i < al.size(); i++){
            al2.add(al.get(i).substring(0, al.get(i).lastIndexOf('.')));
        }
        return al2;
    }

    //collections extensions of original files, appended to new filename
    //in renamer tool
    public ArrayList<String> extensionList(){
        ArrayList<File> files = originalFiles();
        ArrayList<String> extensionList = new ArrayList<>();

        for(File file : files){
            String fileName = file.toString();
            int index = fileName.lastIndexOf('.');

            if(index > 0){
                String extension = fileName.substring(index);
                extensionList.add(extension);
            }
        }
        return extensionList;
    }

    //prints a list of files
    public void listFiles(){
        ArrayList<File> files = originalFiles();
        System.out.println("Files in Directory: " + getDirectory());
        for(int i = 0; i < files.size(); i++){
            if(files.get(i).isFile()){
                System.out.println(files.get(i).getName());
            }
        }
    }

    //FUNCTIONS, return ArrayList<String> and feed to renameTool as parameter
    //add keyword to files
    public ArrayList<String> replaceWithKeyword(String keyword){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        for(int i = 0; i < listOfFiles.size(); i++){
            if(i < 9){
                al.add("0" + (i+1) + " " + keyword);
            }else{
                al.add((i+1) + " " + keyword);
            }
        }
        return al;
    }
    
    //remove chars by keyword
    public ArrayList<String> removeByKeyword(String keyword){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        //get name of files to arr
        for(int i = 0; i < listOfFiles.size(); i++){
            al.add(listOfFiles.get(i).replace(keyword, ""));
        }        
        return al;
    }

    //add prefix keyword
    public ArrayList<String> addPrefix(String keyword){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        //add prefix to string
        for(int i = 0; i < listOfFiles.size(); i++){
            al.add(keyword + listOfFiles.get(i));
        }
        return al;
    }

    //add postfix keyword
    public ArrayList<String> addPostfix(String keyword){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        //add postfix to string
        for(int i = 0; i < listOfFiles.size(); i++){
            al.add(listOfFiles.get(i) + keyword);
        }
        return al;
    }

    //remove chars by index
    public ArrayList<String> removeByIndex(int index1, int index2){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        for(int i = 0; i < listOfFiles.size(); i++){
            StringBuffer sb = new StringBuffer(listOfFiles.get(i));
            al.add(sb.replace(index1, index2, "").toString());
        }
        return al;
    }
    
    //insert keyword at index
    public ArrayList<String> insertKeywordAtIndex(int index, String keyword){
        ArrayList<String> listOfFiles = filenameList(); 
        al = new ArrayList<>();

        for(int i = 0; i < listOfFiles.size(); i++){
            StringBuffer sb = new StringBuffer(listOfFiles.get(i));
            al.add(sb.insert(index, keyword).toString());
        }
        return al;
    }
}