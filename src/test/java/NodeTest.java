import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    @Test
    void constructorTest() throws IllegalArgumentException{
        //working root node
        Node testNode = new Node(1, "testing");
        assertEquals("testing", testNode.getStoryContent());

        //check throws exception if id is not a positive number
        assertThrows(IllegalArgumentException.class, ()-> new Node(-1, "testing"));

        //regular node with parent
        Node testNode2 = new Node(2, "testing2");

        //check throws exception if id is not a positive number
        assertThrows(IllegalArgumentException.class, ()-> new Node(-1, "testing"));


        //check throws exception if choiceValue is not a positive number
        assertThrows(IllegalArgumentException.class, ()-> new Node(-1, "testing"));
    }

    @Test
    //there's no way to test this because the maps are set to null by default
    void checkConditionExistsTest(){
        //check if condition does not exist

        //check if condition does not exist when there are multiple conditions

        //check if condition already exists in parent

    }



    @Test
    void editStoryContentTest(){
        //new node
        Node testNode = new Node(10, "testing1");
        //new story content
        testNode.editStoryContent("newContent1");
        //check new story content
        assertEquals("newContent1", testNode.getStoryContent());

        //new node
        Node testNode2 = new Node(10000, "This is the old content of the story");
        //new story content
        testNode2.editStoryContent("This is the new content of the story");
        //check new story content
        assertEquals("This is the new content of the story", testNode2.getStoryContent());
    }
}
