import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoryTests {

    @Test
    void constructorTest(){
        Story testStory = new Story(0, "Title", "Root content");
        assertEquals(0, testStory.getID());
        assertEquals("Title", testStory.getTitle());
        assertFalse(testStory.getRoot() == null);
        assertEquals("Root content", testStory.getRoot().getStoryContent());
    }

    @Test
    void getNextTest(){
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Content for the first child");
        testStory.linkNodes(1, 2, "First choice");
        testStory.addNode("additional content for another node");
        testStory.linkNodes(1, 3, "Second choice");
        testStory.addNode("more content");
        testStory.linkNodes(1, 4, "Third choice");
        testStory.setCurrentNode(testStory.getRoot());
        Node testNode1 = testStory.getNext(1);
        assertEquals("Content for the first child", testNode1.getStoryContent());
        testStory.setCurrentNode(testStory.getRoot());
        Node testNode2 = testStory.getNext(2);
        assertEquals("additional content for another node", testNode2.getStoryContent());
        testStory.setCurrentNode(testStory.getRoot());
        Node testNode3 = testStory.getNext(3);
        assertEquals("more content", testNode3.getStoryContent());
        testStory.setCurrentNode(testStory.getRoot());
        assertThrows(IllegalArgumentException.class, ()-> testStory.getNext(4));

    }


    @Test
    void editNodeStoryContentTest(){
        Story testStory = new Story(0,"Story", "This is test content for the root");
        testStory.addNode("Content for the first child");
        testStory.linkNodes(1, 2, "First choice");
        testStory.addNode("additional content for another node");
        testStory.linkNodes(1, 3, "Second choice");
        testStory.editNodeStoryContent(3, "this is the changed story content");
        assertEquals("this is the changed story content", testStory.findNode(3).getStoryContent());
        assertEquals("This is test content for the root", testStory.getRoot().getStoryContent());
        assertEquals("Content for the first child", testStory.findNode(2).getStoryContent());
        assertFalse(testStory.findNode(2).getStoryContent().contentEquals("additional content for another node"));

        assertThrows(IllegalArgumentException.class, ()-> testStory.editNodeStoryContent(4, "valid story content"));
        assertThrows(IllegalArgumentException.class, ()-> testStory.editNodeStoryContent(2, ""));
        assertThrows(IllegalArgumentException.class, ()-> testStory.editNodeStoryContent(2, " "));
    }



    @Test
    void addNodeTest(){
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Content for the first child");
        testStory.addNode("additional content for another node");
        assertEquals("Content for the first child", testStory.findNode(2).getStoryContent());
        assertEquals("additional content for another node", testStory.findNode(3).getStoryContent());
    }


    @Test
    void findNodeTest(){
        Story testStory1 = new Story(1,"Story", "This is test content for the root");
        testStory1.addNode("Content for the first child");
        testStory1.addNode("additional content for another node");
        testStory1.addNode("more content");
        assertEquals("This is test content for the root", testStory1.findNode(1).getStoryContent());
        assertEquals("Content for the first child", testStory1.findNode(2).getStoryContent());
        assertEquals("additional content for another node", testStory1.findNode(3).getStoryContent());
        assertEquals("more content", testStory1.findNode(4).getStoryContent());
        assertNull(testStory1.findNode(5));
    }

    @Test
    void printCurrentNodeTest(){
        Story testStory2 = new Story(1,"Story", "This is test content for the root");
        testStory2.addNode("Content for the first child");
        testStory2.printCurrentNode();

        testStory2.addNode("additional content for another node");
        testStory2.printCurrentNode();
    }


}
