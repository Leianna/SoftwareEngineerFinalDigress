import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Story {

    private int id;
    private String title;
    private String rootContent;
    private Node root;
    private Node currentNode;
    private HashMap<Integer, Node> storyNodes;
    private HashMap<Integer, ArrayList<Integer>> nodeConnections;
    private HashMap<Integer, ArrayList<String>> nodeConditions;
    LinkedList<String> tags;
    public int count;

    //used by Json
    public Story(){

    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setRootCont(String rootCont){
        this.rootContent = rootCont;
    }

    public void setNodeConditions(HashMap<Integer, ArrayList<String>> nodeConditions){
        this.nodeConditions = nodeConditions;
    }

    public void setNodeConnections(HashMap<Integer, ArrayList<Integer>> nodeConnections){
        this.nodeConnections = nodeConnections;
    }


    public Story(int id, String title, String rootCont){
        this.id = id;
        if (title.equals(" ") || title.equals("")){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
        if (rootCont.equals(" ") || rootCont.equals("")){
            throw new IllegalArgumentException("Root cannot be empty");
        }
        this.rootContent = rootCont; //holds the beginning content outside of node
        root = new Node(1, rootCont);
        currentNode = root;
        storyNodes = new HashMap();
        nodeConnections = new HashMap<>();
        nodeConditions = new HashMap<>();
        storyNodes.put(1, root);
        nodeConnections.put(root.getId(), new ArrayList<>());
        nodeConditions.put(root.getId(), new ArrayList<>());
        tags = new LinkedList<>();
    }

    public Node getNext(int choiceValue){
        if (nodeConnections.get(currentNode.getId()).isEmpty()){
            throw new IllegalArgumentException("Node has no children");
        }
        if (choiceValue > nodeConnections.get(currentNode.getId()).size()){
            throw new IllegalArgumentException("Choice value doesn't exist");
        }
        if (choiceValue < 1){
            throw new IllegalArgumentException("Choice value must be a positive number greater than 0");
        }
        int ID = nodeConnections.get(currentNode.getId()).get(choiceValue-1);
        currentNode = findNode(ID);
        return currentNode;
    }

    public void editNode(int nodeID, String editChoice){
        Scanner scanner = new Scanner(System.in);
        if (editChoice.equals("story content")){
            System.out.print("Enter what you would like the new story content to be: ");
            String newContent = scanner.nextLine();
            editNodeStoryContent(nodeID, newContent);
        }
        else if (editChoice.equals("children")){
            System.out.print("Would you like to edit, assign, or delete a child? ");
            String choice = scanner.nextLine();
            System.out.print("Which # child? (0 if assigning) ");
            int nodeNum = scanner.nextInt();
            editNodeChildren(nodeID, nodeNum, choice);
        }
        else{
            throw new IllegalArgumentException("Invalid choice");
        }
    }

    public void editNodeStoryContent(int nodeID, String newStoryContent){
        Node nodeToChange = findNode(nodeID);
        if (nodeToChange == null){
            throw new IllegalArgumentException("A node with this ID does not exist");
        }
        if (newStoryContent.equals("") || newStoryContent.equals(" ")){
            throw new IllegalArgumentException("Story content cannot be empty");
        }
        nodeToChange.editStoryContent(newStoryContent);
    }

    public void editNodeChildren(int nodeID, int childNum, String choice){
        Scanner scanner = new Scanner(System.in);
        if (choice.equals("edit")){
            System.out.print("Story content or children? ");
            choice = scanner.nextLine();
            if (choice.equals("story content")){
                editNode(nodeConnections.get(nodeID).get(childNum-1), "story content");
            }
            else if (choice.equals("children")){
                editNode(nodeConnections.get(nodeID).get(childNum-1), "children");
            }
            else{
                throw new IllegalArgumentException("Invalid choice");
            }
        }
        if (choice.equals("assign")){
            System.out.print("Enter the ID of the node you want to add as a child: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the condition: ");
            String condition = scanner.nextLine();
            linkNodes(nodeID, id, condition);
        }
        if (choice.equals("delete")){
            deleteNode(nodeConnections.get(nodeID).get(childNum-1));
        }
    }

    public void addNode(String storyContent){
        if (storyContent.equals("") || storyContent.equals(" ")){
            throw new IllegalArgumentException("Story content cannot be empty");
        }
        int nodeID = storyNodes.size() + 1;
        while (storyNodes.get(nodeID) != null){
            nodeID += 1;
        }
        Node sNode = new Node(nodeID, storyContent);
        storyNodes.put(nodeID, sNode);
        nodeConditions.put(nodeID, new ArrayList<>());
        nodeConnections.put(nodeID, new ArrayList<>());
        currentNode = storyNodes.get(nodeID); //Every time a node gets added, it becomes the current node
        count = count +1;
    }

    public void linkNodes(int parentID, int childID, String condition){
        if (parentID == childID){
            throw new IllegalArgumentException("Cannot set Node as it's own child");
        }
        if (findNode(parentID) == null) {
            throw new IllegalArgumentException("Parent does not exist");
        }
        if (findNode(childID) == null) {
            throw new IllegalArgumentException("Child does not exist");
        }
        if (nodeConnections.get(parentID).contains(childID)){
            throw new IllegalArgumentException("This node is already a child");
        }
        nodeConnections.get(parentID).add(childID);
        nodeConditions.get(parentID).add(condition);

    }

    public void removeReferences(int nodeID){
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> itr = nodeConnections.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<Integer, ArrayList<Integer>> entry = itr.next();
            if (entry.getValue().contains(nodeID)){
                int idx = entry.getValue().indexOf(nodeID);
                entry.getValue().remove(idx);
                nodeConditions.get(entry.getKey()).remove(idx);
                }
            }
        }

    public void deleteNode(int nodeID) throws IllegalArgumentException{
        Scanner scanner = new Scanner(System.in);
        if (nodeID == 1){
            throw new IllegalArgumentException("Cannot delete root");
        }
        if (findNode(nodeID)==null){
            throw new IllegalArgumentException("Node does not exist");
        }else{
            storyNodes.remove(nodeID);
            removeReferences(nodeID);

            if (! (nodeConnections.get(nodeID).isEmpty())) {
                int size = nodeConnections.get(nodeID).size();
                for (int i = 0; i < size; i++) {
                    System.out.print("Would you like to permanently delete or relink Node #" + nodeConnections.get(nodeID).get(0) + "? (delete or relink) ");
                    String choice = scanner.nextLine();
                    if (choice.equals("delete")) {
                        deleteNode(nodeConnections.get(nodeID).get(i));
                    }
                    if (choice.equals("relink")) {
                        System.out.print("What Node would you like to link it to? ");
                        int nodeChoice = scanner.nextInt();
                        linkNodes(nodeChoice, nodeConnections.get(nodeID).get(0), nodeConditions.get(nodeID).get(0));
                    }
                }
            }
            nodeConnections.remove(nodeID);
        }
    }
    Node findNode(int nodeID) throws IllegalArgumentException{ //hardcoded test to supplement addnode
        if(storyNodes.size() < 1) {
            throw new IllegalArgumentException("There has to be at least one story node");
        }
        if (storyNodes.get(nodeID) == null){
            return null;
        }
        currentNode = storyNodes.get(nodeID); //Every time findNode is used, the node becomes the current node
        return storyNodes.get(nodeID);
    }

    public String printCurrentNode(){
        String node = currentNode.getStoryContent();
        ArrayList nextConditions = getNextConditions();
        for (int i = 1; i <= nextConditions.size(); i++){
            node += "\n(" + i + ") " + nextConditions.get(i-1);
        }
        return node;
    }

    public String printAllNodes(){
        String allNodes = "";
        for(int i = 1; i <= storyNodes.size(); i++){
            if(i > 1){
                allNodes += "\n\n";
            }
            allNodes += "(" + i + ") " + storyNodes.get(i).getStoryContent();
            if (!nodeConditions.get(i).isEmpty()) {
                for (int j = 0; j < nodeConditions.get(i).size(); j++) {
                    allNodes += "\n\t(" + (j+1) + ") " + nodeConditions.get(i).get(j);
                }
            }
        }
        return allNodes;
    }

    //Json
    public void exportStory(String filepath) throws IOException {
        filepath += "/" + this.getTitle();
        File file = new File(filepath);
        file.mkdir();
        JsonUtil.toJsonFile(filepath + "/" + this.getTitle() + ".json", this);
    }

    public void addTag(String tagToAdd){
        tags.add(tagToAdd);
    }

    public int getCount(){
        return count;
    }
    public Node getRoot(){
        return root;
    }

    public Node getCurrentNode(){ //returns the currentNode as Node(numbers) as a location
        return currentNode;
    }

    public void setCurrentNode(Node nodeToAssign){
        this.currentNode = nodeToAssign;
    }

    public HashMap getStoryNodes(){
        return storyNodes;
    }

    public int getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getRootContent(){
        return rootContent;
    }

    public LinkedList<String> getTags(){
        return tags;
    }

    public HashMap<Integer, ArrayList<String>> getNodeConditions(){
        return nodeConditions;
    }

    public HashMap<Integer, ArrayList<Integer>> getNodeConnections(){
        return nodeConnections;
    }

    public ArrayList<String> getNextConditions(){
        return nodeConditions.get(currentNode.getId());
    }

}