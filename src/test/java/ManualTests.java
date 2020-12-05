import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ManualTests {



    public static void editNodeChildrenTest(){
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Child 1 content");
        testStory.addNode("Child 2 content");
        testStory.addNode("Child 3 content");
        testStory.linkNodes(1, 2, "First choice");
        testStory.linkNodes(1, 3, "Second choice");
        testStory.linkNodes(1, 4, "Third choice");
        testStory.editNodeChildren(1, 1, "delete");
        System.out.println("Should be 2: " + testStory.getNodeConnections().get(1).size());
        //first prompt type "story content" second type "New Content"
        testStory.editNodeChildren(1, 1, "edit");
        testStory.setCurrentNode(testStory.getRoot());
        System.out.println("Should be New Content: " + testStory.getNext(1).getStoryContent());
    }

    public static void deleteNodeTest(){
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Child 1 content");
        testStory.addNode("Child 2 content");
        testStory.addNode("Child 3 content");
        testStory.linkNodes(1, 2, "First choice");
        testStory.linkNodes(2, 3, "Second choice");
        testStory.linkNodes(2, 4, "Third choice");
        //delete 3, relink 4 to 1
        testStory.deleteNode(2);
        System.out.println("Should be 4: " + testStory.getNodeConnections().get(1).get(0));
    }

    public static void main(String[] args) {
        //editNodeChildrenTest();
        deleteNodeTest();
    }

}
