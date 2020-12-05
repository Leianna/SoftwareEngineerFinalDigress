import java.util.HashMap;

public class Node {
    private int id;
    private String storyContent;
    private boolean endNode;


    //used by Json
    public Node(){

    }

    public void setId(int id){
        this.id = id;
    }

    public void setStoryContent(String storyContent){
        this.storyContent = storyContent;
    }

    //root node constructor
    //add character limit here?
    public Node(int id, String storyContent) throws IllegalArgumentException{
        //should throw invalid argument exceptions higher up:
        //id must be unique, each story must have exactly one root node

        if (id < 1){
            throw new IllegalArgumentException("ID must be a positive number");
        }
        this.id = id;
        this.storyContent = storyContent;
    }

    public static boolean checkConditionValid(String conditionIn){
        conditionIn = conditionIn.toLowerCase();
        char[] charArray = conditionIn.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= 'a' && ch <= 'z')) {
                if (ch != ' ') {
                    return false;
                }
            }
        }
        return true;
    }



    public String getStoryContent(){
        return storyContent;
    }

    public boolean isEndNode() { return endNode; }

    public void setEndNode(){ endNode = true; }

    public int getId(){
        return id;
    }

    public void editStoryContent(String newStoryContent){
        storyContent = newStoryContent;
    }

}
