import java.io.*;
import java.util.*;

import javax.swing.JTextArea;

import java.lang.StringBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map.Entry;



public class FileRenamer{

    //directory field
    private String dir;
    private final String tmpdir = System.getProperty("java.io.tmpdir");
    private ArrayList<String> al;

    //constructor, getter, setter
    public String getDirectory(){return dir;}
    public void setDirectory(String directory){this.dir = directory;}

    //renamer tool: new and improved!!!
    //side note: you need to find a way to stop files with duplicate names to overwrite themselves, otherwise some error
    //would occur
    public void rename(ArrayList<String> newFileNames){
        ArrayList<File> files = originalFiles();
        HashMap<String, String> hash = extensionHash();

        try{
            for(int i = 0; i < files.size(); i++){
                Iterator<Entry<String, String>> it = hash.entrySet().iterator();
                String path = files.get(i).toString();
                Path file = Paths.get(path);
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class); 
                System.out.println(file.toString());  

                while(it.hasNext()){
                    Map.Entry<String, String> set = (Map.Entry<String, String>) it.next();
                    if(String.valueOf(attr.size()).equals(set.getKey()) && files.get(i).isFile()){
                        File f = new File(dir + files.get(i).getName());
                        f.renameTo(new File(dir + newFileNames.get(i) + set.getValue()));
                        break;
                    }
                }
            }
        }catch(IOException io){System.out.println("IOException");}
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

    //extension hash
    public HashMap<String, String> extensionHash(){
        ArrayList<File> files = originalFiles();
        HashMap<String, String> extensionHash = new HashMap<>();

        try{
            for(int i = 0; i < files.size(); i++){
                String path = originalFiles().get(i).toString();
                Path filePath = Paths.get(path);
                BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

                for(int j = 0; j < files.size(); j++){
                    String fileName = files.get(i).toString();
                    int index = fileName.lastIndexOf(".");

                    if(index > 0){
                        String extension = fileName.substring(index);
                        extensionHash.put(String.valueOf(attr.size()), extension);
                    }
                }
            }
        }catch(IOException io){
            System.out.println("IOException");
        }
        return extensionHash;
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
    //Consider refactoring renameConfirmTool(); to deprecate this method
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

    public ArrayList<String> simpleNumericalPrefix(){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        for(int i = 0; i < listOfFiles.size(); i++){
            al.add((i+1) + " " + listOfFiles.get(i).toString());
        }
        return al;
    }

    public ArrayList<String> simpleNumericalSuffix(){
        ArrayList<String> listOfFiles = filenameList();
        al = new ArrayList<>();

        for(int i = 0; i < listOfFiles.size(); i++){
            al.add(listOfFiles.get(i).toString() + " " + "(" + (i+1) + ")");
        }
        return al;
    }

    //hashmap to txt
    public void hashmapToTxt(){
        File file = new File(tmpdir + "/Temp_FR012934.tmp");
        HashMap<String, String> hash = new HashMap<String, String>();
        BufferedWriter bf = null;

        try{
            //get file size and file name to hashmap
            for(int i = 0; i < originalFiles().size(); i++){
                String path = originalFiles().get(i).toString();
                Path filePath = Paths.get(path);
                BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                hash.put(String.valueOf(attr.size()), path.toString().replace(dir, ""));
            }

            bf = new BufferedWriter(new FileWriter(file));

            for(Map.Entry<String, String> entry : hash.entrySet()){
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        }catch(IOException io){
            io.printStackTrace();
        }finally{
            try{bf.close();}catch(Exception e){System.out.println("lol something happened");}
        }
    }

    //renames files to their original names by utilizing hashmap in TEMP.txt
    public void renameToOriginal(){
        ArrayList<File> files = originalFiles();

        try{
            BufferedReader reader;
            for(int i = 0; i < files.size(); i++){
                reader = new BufferedReader(new FileReader(tmpdir + "/Temp_FR012934.tmp"));
                String line = reader.readLine();

                String path = files.get(i).toString();
                Path file = Paths.get(path);
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                System.out.println(file.toString());

                while(line != null){
                    if(line.startsWith(String.valueOf(attr.size()))){
                        File f = new File(files.get(i).toString());
                        //consider filesize and filename into a hashmap
                        f.renameTo(new File(dir + line.replace(String.valueOf(attr.size()) + ":", "")));
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        }catch(IOException io){System.out.println("error");}
    }

    public static void main(String[] args){
        FileRenamer fr = new FileRenamer();
        fr.setDirectory("/home/lain/Pictures/Random/Yeule/");
        System.out.println(fr.simpleNumericalSuffix());
    }
}