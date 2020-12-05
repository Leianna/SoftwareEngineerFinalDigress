import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class storyFile extends File {

    String fileName;
    String path;
    //constructor
    public storyFile(String fileNameIn, String pathIn) throws IllegalArgumentException{
        super(fileNameIn);
        if(fileNameIn!=" " && pathIn!=" " && fileNameIn!="" && pathIn!=""){
            fileName = fileNameIn;
            path = pathIn;
        }else {
            throw new IllegalArgumentException("Wrong file name or path");
        }

    }

    /**
     * Check if there's a file in this path
     * @return true if file is here, false if can't find the file
     */
    public FileInputStream checkPath() throws IOException, NullPointerException {
        FileInputStream testFile = new FileInputStream(path);
        if(testFile!=null){
            return testFile;
        }else{
            throw new FileNotFoundException("Incorrect Path");
        }
    }

    /**
     * input txt file
     * @throws IOException if path is empty
     * @throws IllegalArgumentException if the file have no content
     */
    public Story importFile() throws IOException,IllegalArgumentException {
        FileInputStream readFile = checkPath();
        System.setIn(readFile);
        Scanner scanFile = new Scanner(System.in);
        String line = scanFile.nextLine();
        if(scanFile.hasNextLine()) {
            //ID
            int parentID = 1;

            //create story node
            //int id, String title, String rootCont
            Story newStory = new Story(parentID, fileName, line);

            //for loop check key
            boolean isOpen = false;
            boolean isClose = false;

            //choice value and condition
            String choice = "";
            String value = "";

            //check if there's child node
            while (scanFile.hasNextLine()) {
                line = scanFile.nextLine();

                //reset counting value
                choice = "";
                value = "";
                isOpen = false;
                isClose = false;

                //check the choice value
                for (int j = 0; j < line.length(); j++){
                    if (line.charAt(j)=='['){
                        isOpen = true;
                    }else if(line.charAt(j)==']'){
                        isClose = true;
                        isOpen = false;
                    }else if(isClose==false && isOpen==true){
                        choice = choice + String.valueOf(line.charAt(j));

                    }else {
                        value = value + String.valueOf(line.charAt(j));

                    }
                }

                //add child node
                if(choice.isEmpty()==false && value.isEmpty()==false) {
                    //String storyContent, int parentID, int choiceValue, String condition
                    //ask for boolean endNode
                    newStory.addNode(value);
                    newStory.linkNodes(parentID, Integer.parseInt(choice)+1, value);
                }
            }
            return newStory;
        }else{
            throw new IllegalArgumentException("File is empty");
        }

    }

    /**
     * output a story
     * @param outputStory a story node
     * @throws IllegalArgumentException if the story node is empty
     * @throws IOException
     */
    public void outputFile(Story outputStory) throws IllegalArgumentException, IOException {
        //check the inout format
        if(outputStory==null){
            throw new IllegalArgumentException("Can not find the story");
        }
        //content
        FileWriter outputFile = new FileWriter(path);
        outputFile.write(outputStory.getRootContent());
        //count how many children
        int count = outputStory.getCount();

        //write file
        for(int i = 1; i <= count; i++){
            outputFile.write("\n["+i+"]"+outputStory.findNode(i+1).getStoryContent());
        }
        outputFile.close();
    }

    /**
     * delete file from directory
     * @throws IOException
     */
    public void deleteFile() throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    public void editFile(String content) throws IOException,IllegalArgumentException {
        if(content=="" || content==" "){
            throw new IllegalArgumentException("The content can not be empty");
        }
        FileInputStream readFile = checkPath();
        System.setIn(readFile);
        Scanner scanFile = new Scanner(System.in);
        String line = scanFile.nextLine();

        //print out old content
        System.out.println("Current content: ");
        while (scanFile.hasNextLine()) {
            System.out.println(line);
            line = scanFile.nextLine();
        }

        System.out.println("New content: ");
        //Break content by line
        String lines[] = content.split("\\r?\\n");

        //rewrite file
        FileWriter newContent = new FileWriter(path);
        newContent.write(lines[0]);

        //print out new content
        System.out.println(lines[0]);
        for (int i = 1; i < lines.length; i++){
            newContent.write("\n"+lines[i]);
            System.out.println(lines[i]);
        }

        newContent.close();

        //import the file
        importFile();

    }

    //getters
    public String getPath() {
        return path;
    }
    public String getFileName(){
        return fileName;
    }





}
