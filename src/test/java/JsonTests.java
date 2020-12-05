import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTests {

    @Test
    void test() throws IOException {
        Story testStory = new Story(1,"Story", "This is test content for the root");
        testStory.addNode("Content for the first child");
        testStory.linkNodes(1, 2, "First choice");
        testStory.addNode("additional content for another node");
        testStory.linkNodes(2, 3, "Second choice");
        testStory.addNode("more content");
        testStory.linkNodes(1, 4, "Third choice");
        testStory.exportStory("src/main/java");
        Story newStory = JsonUtil.fromJsonFile("src/main/java/Story/Story.json", Story.class);
        assertEquals("This is test content for the root", newStory.findNode(1).getStoryContent());
        assertEquals("Content for the first child", newStory.findNode(2).getStoryContent());
        assertEquals("additional content for another node", newStory.findNode(3).getStoryContent());
        assertEquals("more content", newStory.findNode(4).getStoryContent());
        assertEquals(2, newStory.getNodeConnections().get(1).size());
        assertEquals(1, newStory.getNodeConnections().get(2).size());
        assertEquals(0, newStory.getNodeConnections().get(3).size());
        assertEquals(0, newStory.getNodeConnections().get(4).size());
        assertEquals(4, newStory.getStoryNodes().size());
    }
}
