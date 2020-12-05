import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class storyFileTest {
    @Test
    public void storyFileTest() throws IllegalArgumentException {
        storyFile test1 = new storyFile("testFile","src/main/java/testFilePackage/testFile.txt");

        //check file name
        assertEquals("testFile",test1.getFileName());

        //check file path
        assertEquals("src/main/java/testFilePackage/testFile.txt",test1.getPath());
    }

    @Test
    public void checkPathTest() throws IOException {
        //check exception error
        assertThrows(FileNotFoundException.class, ()-> new storyFile("testFile","/main/java/falseFile.txt").checkPath());
    }
    @Test
    public void importFileTest() throws IOException {
        storyFile test1 = new storyFile("testFile","src/main/java/testFilePackage/testFile.txt");
        //check the parent root
        assertEquals(1,test1.importFile().getID());
        assertEquals("testFile",test1.importFile().getTitle());
        assertEquals("You are walking down a dark, dingy hallway. Where would you like to go?",test1.importFile().getRootContent());

        //check how many children
        assertEquals(10,test1.importFile().getCount());

        //check child
        //key
        assertEquals("Continue straight",test1.importFile().findNode(2).getStoryContent());
        assertEquals("Take a left",test1.importFile().findNode(3).getStoryContent());
        assertEquals("test ten",test1.importFile().findNode(11).getStoryContent());

        //choice value
//        assertEquals("test ten",test1.importFile().getNext(10).getStoryContent());
//        assertEquals("test seven",test1.importFile().getNext(7).getStoryContent());
//        assertEquals("test four",test1.importFile().getNext(4).getStoryContent());

        //check empty file
        assertThrows(FileNotFoundException.class, ()-> new storyFile("test_file","/main/java/test_file.txt").checkPath());
    }

    @Test
    public void deleteFileTest() throws IOException {
        //create a random file for test
        File deleteFile = new File("src/main/java/testFilePackage/deleteFile.txt");
        deleteFile.createNewFile();

        //delete the file
        storyFile test1 = new storyFile("deleteFile","src/main/java/testFilePackage/deleteFile.txt");
        test1.deleteFile();

        //check if the file is still exist
        assertThrows(FileNotFoundException.class, ()-> new storyFile("deleteFile","/main/java/deleteFile.txt").checkPath());
    }
    @Test
    public void outputFileTest() throws IOException,IllegalArgumentException {
        //create a story
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Content for the first child");
        testStory.addNode("additional content for another node");
        testStory.addNode("more content");

        //create a story file
        storyFile test1 = new storyFile("outputFile","src/main/java/testFilePackage/outputFile.txt");
        test1.outputFile(testStory);

    }
    @Test
    public void editFileTest() throws IOException,IllegalArgumentException {
        storyFile test1 = new storyFile("editFile","src/main/java/testFilePackage/editFile.txt");
        test1.editFile("Test onw two or three?\n" +
                "[1]one\n" +
                "[2]two\n" +
                "[3]three");

        //check error
        assertThrows(IllegalArgumentException.class, ()->  test1.editFile(""));
        assertThrows(IllegalArgumentException.class, ()->  test1.editFile(" "));

    }

}
