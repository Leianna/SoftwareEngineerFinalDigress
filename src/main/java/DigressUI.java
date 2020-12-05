import java.io.IOException;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.*;

public class DigressUI {

    private static HashMap<String, Story> storyCol = new HashMap<>();

    /**
     * Creates a story
     * @throws IllegalArgumentException if the title and content of the story is invalid
     * @post creates the story
     */
    public static void createStory() {
        Scanner in = new Scanner(System.in);
        String title = "", root = "", userIn = "";
        boolean inputValid = true;
        Random ran = new Random();
        int id = ran.nextInt(1000);

        System.out.println("Enter story title: ");
        //todo: need error testing for invalid input here
        title = in.nextLine();
        while (!isTitleValid(title)){
            System.out.println("Invalid input!");
            System.out.print("Enter story title: ");
            title = in.nextLine();
        }
        System.out.println("Enter root node content: ");
        root = in.nextLine();

        while (!isContentValid(root)){
            System.out.println("Invalid input!");
            System.out.print("Enter root node content: ");
            root = in.nextLine();
        }

        Story story = new Story(id, title, root);
        story = editStory(story);
        storyCol.put(story.getTitle(), story);
    }

    /**
     * @post Check to see if title entered is valid
     * @params title: title of the story
     */
    public static boolean isTitleValid(String titleIn){
        if(titleIn.isEmpty())
            return false;
        if(titleIn.matches(".*\\s.*"))
            return true;
        return true;
    }

    /**
     * @post Check to see if content entered is valid
     * @params content: content of the story
     */
    public static boolean isContentValid(String content){
        if(content.isEmpty()  || content.matches("\\d+") || content.matches(".*.*\\s\\d+") || content.matches(".*.*\\d+"))
            return false;
        if(content.matches(".*\\s.*"))
            return true;
        return true;
    }

    /**
     * @post Check to see if story entered is valid
     * @params storyCol, titleEntered, storySelected : Linked list of stories, title entered by the user
     */
    public static boolean isStoryValid(LinkedList<Story> storyCol, String titleEntered){
        for (int i = 0; i < storyCol.size(); i++) {
            if (storyCol.get(i).getTitle().equalsIgnoreCase(titleEntered)) {
                return true;
            }return false;
        }return false;
    }


    /**
     * Enter the conditions for a story
     * @throws IllegalArgumentException if the invalid condition number is chosen
     * @post Enter the conditions for the story
     * @params story, id, choiceVal : story to add more conditions to, id of the node,choice value of the node to connect to
     */
    public static void enterCondition(Story story, int id) throws IllegalArgumentException {
        String condition = "";
        boolean inputValid;
        Scanner in = new Scanner(System.in);
        int parentId;
        do {
            System.out.print("Enter the ID of the node to link to: ");
            inputValid = true;
            parentId = in.nextInt();
            in.nextLine();
            //todo: check to see about node

            if (story.findNode(parentId) == null) {
                System.out.println("ID must exist in the story");
                inputValid = false;
            }
        } while (!inputValid);
        int choiceVal = story.getNodeConditions().get(parentId).size() + 1;

        //todo Give option to go back
        System.out.print("Condition " + choiceVal + ": ");
        condition = in.nextLine();

        if (!Node.checkConditionValid(condition)) {
            do {
                System.out.println("Please enter only letters.");
                System.out.print("Condition " + choiceVal + ": ");
                condition = in.nextLine();
            } while (!Node.checkConditionValid(condition));

        }
        story.linkNodes(parentId, id, condition);
    }

    public static Story editStory(Story storyToEdit) {
        boolean creating = true;
        boolean inputValid = true;
        Scanner in = new Scanner(System.in);
        String userIn;
        int currId = 1;
        do {
            System.out.println("Add a node (a), delete a node (d), edit a node(e), view all nodes (v), add tags (t), change title (ct), or finalize story(x)?");
            do {
                inputValid = true;
                userIn = in.nextLine();
                if (!(userIn.equalsIgnoreCase("a") || userIn.equalsIgnoreCase("d") || userIn.equalsIgnoreCase("v") || userIn.equalsIgnoreCase("e") || userIn.equalsIgnoreCase("t") || userIn.equalsIgnoreCase("ct") || userIn.equalsIgnoreCase("x"))) {
                    System.out.println("Please enter a valid option");
                    inputValid = false;
                }
            } while (!inputValid);

            if (userIn.equalsIgnoreCase("a")) {
                System.out.print("Enter the story content for this node: ");
                String storyContent = in.nextLine();
                storyToEdit.addNode(storyContent);
                currId = storyToEdit.getCurrentNode().getId();
                enterCondition(storyToEdit, currId);
            } else if (userIn.equalsIgnoreCase("d")) {
                System.out.print("Enter the ID of the node you'd like to delete: ");

                do {
                    inputValid = true;
                    userIn = in.nextLine();

                    if (!storyToEdit.getStoryNodes().containsKey(Integer.parseInt(userIn))) {
                        System.out.print("Please enter a valid ID:");
                        inputValid = false;
                    }
                } while (!inputValid);

                currId = Integer.parseInt(userIn);
                storyToEdit.deleteNode(currId);
            } else if (userIn.equalsIgnoreCase("e")) {
                String editChoice;
                System.out.println("Would you like to edit nodes (n), delete a node (d), set an end node (s), or change the title of the story (t)? ");
                editChoice = in.nextLine();
                editNodes(storyToEdit, editChoice);
            } else if (userIn.equalsIgnoreCase("v")) {
                System.out.println(storyToEdit.printAllNodes());
            } else if (userIn.equalsIgnoreCase("s")) {
                System.out.print("Enter the ID of the node you'd like to set as an end node: ");
                int nodeId = in.nextInt();
                storyToEdit.findNode(nodeId).setEndNode();
            } else if (userIn.equalsIgnoreCase("t")) {
                System.out.print("Enter individual tags here (or enter 'q' to quit): ");
                String tag = "";
                while (!tag.equalsIgnoreCase("q")) {
                    tag = in.nextLine();
                    if (!tag.equalsIgnoreCase("q")) {
                        storyToEdit.addTag(tag);
                        System.out.print("Tags: ");
                        for (int i = 0; i < storyToEdit.tags.size(); i++) {
                            System.out.print(storyToEdit.tags.get(i) + " ");
                        }
                    }
                }
            } else if (userIn.equalsIgnoreCase("ct")) {
                System.out.print("Enter a new title: ");
                String newTitle = in.nextLine();
                editTitle(storyToEdit, newTitle);
            } else { //if user selects quit
                //todo: Should call a helper function here to make sure every node has at least one child
                creating = false;
                //makes sure story ends somewhere
                boolean end = false;
                Iterator<Map.Entry<Integer, Node>> itr = storyToEdit.getStoryNodes().entrySet().iterator();
                while (!end) {
                    if (itr.next().getValue().isEndNode()) {
                        end = true;
                    }
                }
                if (!end) {
                    System.out.println("You must have a node that ends the story before finalization");
                    creating = true;
                }
            }
        } while (creating);
        return storyToEdit;
    }

    public static void editNodes(Story storyToEdit, String editChoice){
        Scanner scanner = new Scanner(System.in);
        if (editChoice.equalsIgnoreCase("n")) {
            System.out.print("Enter the ID of the node to edit: ");
            int nodeID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Would you like to edit the 'story content' or the 'children'?: ");
            String choice = scanner.nextLine();
            storyToEdit.editNode(nodeID, choice);
        } else if (editChoice.equalsIgnoreCase("t")) {
            System.out.print("Enter a new title: ");
            String newTitle = scanner.nextLine();
            editTitle(storyToEdit, newTitle);
        } else if (editChoice.equalsIgnoreCase("d")) {
            System.out.print("Enter the ID of the node you'd like to delete: ");
            int nodeId = scanner.nextInt();
            scanner.nextLine();
            storyToEdit.deleteNode(nodeId);
        } else if (editChoice.equalsIgnoreCase("s")) {
            System.out.print("Enter the ID of the node you'd like to set as an end node: ");
            int nodeId = scanner.nextInt();
            scanner.nextLine();
            storyToEdit.findNode(nodeId).setEndNode();
        }
    }

    public static Story importUI() throws IOException, NullPointerException {
//        Scanner fileName = new Scanner(System.in);
//        Scanner filePath = new Scanner(System.in);
//        JFileChooser fc = new JFileChooser();
//        File directory = new File("user.home");
//        fc.setCurrentDirectory(new File(System.getProperty(String.valueOf(directory))));
//        JFrame frame = new JFrame();
//        frame.toFront();
//        frame.setVisible(true);
//        int result = fc.showOpenDialog(frame.getContentPane());
//
//        fc.setFileFilter(new FileFilter() {
//            @Override
//            public String getDescription() {
//
//                return "json File (*.json)";
//            }
//            @Override
//            public boolean accept(File file) {
//                if (file.isDirectory()) {
//                    return true;
//                } else {
//                    String filename = file.getName().toLowerCase();
//                    return filename.endsWith(".json");
//                }
//            }
//        });
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            // user selects a file
//            File selectedFile = fc.getSelectedFile();
//            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
//            Story newJson = JsonUtil.fromJsonFile(selectedFile.getAbsolutePath(), Story.class);
//            frame.setVisible(false);
//            return newJson;
//        }
//        else if (result == JFileChooser.ERROR_OPTION){
//            // user selects a wrong file
//        }
//        return null;
        System.out.print("Enter the path of the file (EX: src/main/java/test.json): ");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        return JsonUtil.fromJsonFile(fileName, Story.class);
    }


    public static void exportUI(Story storyToExport) throws IOException, FileNotFoundException {
//        Scanner fileName = new Scanner(System.in);
//        String filePath = "Desktop";
//        JFileChooser fc1 = new JFileChooser();
//        fc1.setCurrentDirectory(new File(System.getProperty("user.home")));
//        JFrame frame = new JFrame();
//        frame.toFront();
//        frame.setVisible(true);
//        int result = fc1.showOpenDialog((Component) JFrame);
//
//        fc1.setFileFilter(new FileFilter() {
//            @Override
//            public String getDescription() {
//                return "TXT File (*.txt)";
//            }
//
//            @Override
//            public boolean accept(File file) {
//                if (file.isDirectory()) {
//                    return true;
//                } else {
//                    String filename = file.getName().toLowerCase();
//                    return filename.endsWith(".txt");
//
//                }
//            }
//        });
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            // user selects a file
//            File selectedFile = fc1.getSelectedFile();
//            System.out.println("Selected file: " + selectedFile.getCanonicalPath());
//            frame.setVisible(false);
//            Story exFile = new Story(1, selectedFile.getName(), selectedFile.toString());
//
//            JFileChooser fc2 = new JFileChooser();
//            fc2.setDialogTitle("Specify a file to save");
//            int userSelection = fc2.showSaveDialog(frame);
//
//            if (userSelection == JFileChooser.APPROVE_OPTION) {
//                File fileToSave = fc2.getSelectedFile();
//                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
//                exFile.exportStory(fileToSave.getAbsolutePath());
//            }
//            storyFile newStoryFile = (storyFile) selectedFile;
//            Story newStory = new Story(1, selectedFile.getName(), Files.readAllBytes(Paths.get(((storyFile) selectedFile).getFileName())));
//            newStoryFile.outputFile(newStory);
//        }

        System.out.print("Enter the path of the file (EX: src/main/java): ");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        storyToExport.exportStory(filePath);

    }

    /**
     * Plays a story
     * @throws IllegalArgumentException if the title and content of the story is invalid
     * @post Plays the story
     * @params story : story user chose to play
     */
    public static void playStory(Story story) {
        String choice;
        String userIn = "y";
        Scanner in = new Scanner(System.in);
        do {
            boolean playing = true;
            story.setCurrentNode(story.getRoot());

            while (playing) {
                System.out.println(story.printCurrentNode());
                do {
                    try {
                        choice = in.nextLine();
                        Integer.parseInt(choice);
                    } catch (Exception e) {
                        System.out.println("Please enter a valid number");
                        System.out.println(story.printCurrentNode());
                        choice = in.nextLine();
                    }

                   /* if (Integer.parseInt(choice) > story.getNodeConditions().size() || Integer.parseInt(choice) < 1) {
                        System.out.println("Please enter a valid number");
                        System.out.println(story.printCurrentNode());
                        choice = in.nextLine();
                    }*/
                } while (Integer.parseInt(choice) > story.getNodeConditions().size() || Integer.parseInt(choice) < 1);
                if (Integer.parseInt(choice) > story.getNodeConditions().size()) {
                    System.out.println("This condition number doesn't exist.");
                } else {
                    story.getNext(Integer.parseInt(choice));
                }
                if (story.getCurrentNode().isEndNode()) {
                    System.out.println(story.getCurrentNode().getStoryContent());
                    playing = false;
                }
            }
            System.out.println("Done with story");
            System.out.println("Play again? (Y/N)");
            userIn = in.nextLine();
        } while (!userIn.equalsIgnoreCase("n"));
    }


    /**
     * Uploads a story to play or edit
     * @throws IllegalArgumentException if the title and content of the story is invalid
     * @post creates the story
     * @params fileForUpload : file name user wants to play or edit
     */
    public static void uploadStory () throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter file name");
        String fileName = in.next();
        int count;
        FileReader fileEx=null;
        try {
            fileEx = new FileReader(fileName);
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println("Enter valid file name:");
            fileName = in.next();
            fileEx = new FileReader(fileName);
        }
        //assert fileEx != null;
        while ((count = fileEx.read()) != -1)
            System.out.print((char) count);
        fileEx.close();
    }

    /**
     * Creates a test story for the UI
     * @post creates the test story
     */
    public static Story getTestStory(){
        String node1 = "You are walking down a dark, dingy hallway. Where would you like to go?";
        String node2 = "You see cobwebs in front of you and a dark shadowy figure. What would you like to do next?";
        String node3 = "There is a giant spider coming down the hallway towards you.";
        String node4 = "You see a vial on the ground with a strange glowing liquid.";
        String node5 = "You approach the figure and you can see nothing but darkness beneath his hood. 'What brings you this way, traveler?'  he asks.";
        String node6 = "You have no weapons and were slain by the spider. Try again?";
        String node7 = "You charmed the spider with your great personality. He will now fight alongside you.";
        String node8 = "You picked up the vial and put it in your bag.";
        String node9 = "For some reason you decided to drink the weird liquid. You died. Whoops.";
        String node10 = "You continue down the hallway. You see a glowing door to your right.";
        String node11 = "He pulls out a dagger and slays you. Stronger than he looks!";
        String endNode = "I don't feel like writing this story anymore to tell you the truth";

        Story testStory = new Story(44, "Labyrinth", node1);

        testStory.addNode(node2);
        testStory.linkNodes(1, 2, "Continue straight");

        testStory.addNode(node3);
        testStory.linkNodes(1, 3,"Take a left");

        testStory.addNode(node4);
        testStory.linkNodes(1, 4, "Turn right");

        testStory.addNode(node5);
        testStory.linkNodes(2, 5, "Approach the figure");

        testStory.linkNodes(2, testStory.getRoot().getId(), "Retreat backwards");

        testStory.addNode(node6);
        testStory.linkNodes(3, 6, "Fight the spider");
        testStory.findNode(6).setEndNode();

        testStory.addNode(node7);
        testStory.linkNodes(3, 7, "Befriend the spider");

        testStory.linkNodes(3, testStory.getRoot().getId(), "Run back");

        testStory.addNode(node8);
        testStory.linkNodes(4, 8, "Pick it up for later");

        testStory.addNode(node9);
        testStory.linkNodes(4, 9, "Drink it");
        testStory.findNode(9).setEndNode();

        testStory.addNode(node10);
        testStory.linkNodes(4, 10, "Keep moving");

        testStory.addNode(node11);
        testStory.linkNodes(5, 11, "'Shut up and fight me fool'");
        testStory.findNode(11).setEndNode();

        testStory.addNode(endNode);
        testStory.findNode(12).setEndNode();

        String node13 = "'Well you've come to the right place. Allow me to aid you in your search.'";
        testStory.addNode(node13);
        testStory.linkNodes(5, 13, "'I am looking for the holy grail'");

        String node14 = "You continue down the hallway with the mysterious stranger at your feet.";
        testStory.addNode(node14);
        testStory.linkNodes(13, 14, "'Let's do this thing!'");

        String node15 = "'Well have it your way then.'";
        testStory.addNode(node15);
        testStory.linkNodes(13, 15, "'No way dude.'");

        String node16 = "Suddenly he pulls a knife out of his pocket and lunges at you! You sense his attack and are able to parry and throw him onto the ground.";
        testStory.addNode(node16);
        testStory.linkNodes(14, 16, "Continue walking");

        String node17 = "You spare the stranger. He thanks you, and offers you a potion as an apology. He claims it grants you super strength.";
        testStory.addNode(node17);
        testStory.linkNodes(16, 17, "Spare him");

        String node18 = "You murder the stranger with his own knife. Karma shall get you soon...";
        testStory.addNode(node18);
        testStory.linkNodes(16, 18, "Kill him");

        String n19 = "You drink the potion and immediately feel an unbelievable strength flowing through your veins.";
        testStory.addNode(n19);
        testStory.linkNodes(17, 19, "Drink the potion");

        String n20 = "You shatter the vial in his face. He immediately grows super strong and rips through his shirt. He grabs your throat and strangles you.";
        testStory.addNode(n20);
        testStory.linkNodes(17, 20, "Shatter it in his face");
        testStory.findNode(20).setEndNode();

        String n21 = "It seems he cast some sort of curse on you. You find yourself getting weaker and weaker until you can hardly stand up. Soon you pass away in great agony.";
        testStory.addNode(n21);
        testStory.linkNodes(18, 21, "Continue walking");
        testStory.findNode(21).setEndNode();

        testStory.linkNodes(19, 12, "Continue");

        testStory.linkNodes(15,10, "Continue walking");

        String n22 = "It's locked.";
        testStory.addNode(n22);
        testStory.linkNodes(10, 22, "Try the handle to the door");

        String n23 = "You kick down the door. There's a huge explosion- seems like you set off some sort of nuclear reaction. You died.";
        testStory.addNode(n23);
        testStory.linkNodes(10, 23, "Kick down the door");
        testStory.findNode(23).setEndNode();

        String n24 = "You round the corner and come across a strange.. bowling ball?";
        testStory.addNode(n24);
        testStory.linkNodes(10, 24, "Continue moving");

        testStory.linkNodes(22, 23, "Kick down the door");
        testStory.linkNodes(22, 24, "Continue moving");

        testStory.linkNodes(24, 12, "Continue");

        String n25 = "You and your new spider friend continue on your way. You run into a creepy-looking old man who offers you 4000 rupees for the spider.";
        testStory.addNode(n25);
        testStory.linkNodes(7, 25, "Continue moving");

        String n26 = "'Well uhh.. I'll trade you this really cute puppy for him'";
        testStory.addNode(n26);
        testStory.linkNodes(25, 26, "'He's not for sale'");

        String n27 = "The spider turns around and bites your head off for your betrayal.";
        testStory.addNode(n27);
        testStory.linkNodes(25, 27, "'Deal!");
        testStory.findNode(27).setEndNode();

        String n28 = "You trade the giant spider for a really cute puppy. Now the puppy accompanies you on your way. 'I get it,' says the spider";
        testStory.addNode(n28);
        testStory.linkNodes(26, 28, "'Okay you got yourself a trade.");

        String n29 = "'Okay whatever man,' he says, and lets you on your way.";
        testStory.addNode(n29);
        testStory.linkNodes(26, 29, "No way Jose!");

        String n30 = "A huge marshmallow appears in front of you and starts speaking Polish. You have no idea what he's saying because you don't speak Polish.";
        testStory.addNode(n30);
        testStory.linkNodes(8, 30, "Continue down the hallway");
        testStory.linkNodes(29, 30, "Continue down the hallway");

        testStory.linkNodes(30, 12, "um... what");
        return testStory;
    }


    /**
     * Edits title of the story
     * @throws IllegalArgumentException if the title of the story is invalid
     * @post edits title of the story
     * @params storyChosen, newTitle  : story chosen by user to alter title and the new title
     */
    public static void editTitle (Story storyChosen, String newTitle) throws IllegalArgumentException {
        if (newTitle == " " || newTitle == "") throw new IllegalArgumentException("Title cannot be empty");
        else if (storyChosen.getTitle().equalsIgnoreCase(newTitle))
            throw new IllegalArgumentException("New title entered is same as previous title");
        else if (!storyChosen.getTitle().equalsIgnoreCase(newTitle)){
            storyChosen.setTitle(newTitle);
        }
    }

    public static void checkStoryComplete(Story story){
        Scanner in = new Scanner(System.in);
        int userIn;
        Map missingConns = new HashMap<>();
        Iterator itr = story.getNodeConnections().entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry thisNode = (Map.Entry)itr.next();
            int thisId = (Integer)thisNode.getKey();
            ArrayList thisConns = (ArrayList)thisNode.getValue();
            if(thisConns.size() == 0 && !story.findNode(thisId).isEndNode()){
                missingConns.put(thisNode.getKey(), thisNode.getValue());
            }
        }
        //if there are nodes with missing connections
        if(missingConns.size() > 0) {
            System.out.println("Each node must have at least one connection before the story can be complete.");
            int i = missingConns.size();
            while(i > 0) {
                System.out.println("These nodes are still missing connections:");
                itr = story.getNodeConnections().entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry thisNode = (Map.Entry) itr.next();
                    int thisId = (Integer) thisNode.getKey();
                    if(thisNode.getValue()!= null) {
                        System.out.println("(" + thisNode.getKey() + ") " + story.findNode(thisId).getStoryContent());
                    }
                }
                System.out.println("Enter node to add connection: ");
                do {
                    userIn = in.nextInt();
                    if (!missingConns.containsKey(userIn)) {
                        System.out.println("Please enter a valid ID: ");
                    }
                } while(!missingConns.containsKey(userIn));
                missingConns.remove(userIn);
                enterCondition(story, userIn);
                i--;
            }
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Welcome to Digress!");
        System.out.println("A software for reading and writing non-linear text-based stories.");
        System.out.println("-------------------------------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        Story storySelected = null;
        storyCol.put(getTestStory().getTitle(), getTestStory());
        Story hellscape = JsonUtil.fromJsonFile("src/main/java/HELLSCAPE/HELLSCAPE.json", Story.class);
        storyCol.put(hellscape.getTitle(), hellscape);
        do {
            System.out.println("╔.★. .══════════════════════════════════════╗");
            System.out.println(" What would you like to do? (Choose a #)");
            System.out.println("1. Create a Story");
            System.out.println("2. Load a Story (2 already included!)");
            System.out.println("3. File Manager");
            System.out.println("4. Help");
            System.out.println("5. Exit");
            System.out.println("╚════════════════════════════════════════. .★.╝");
            try{
                userChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                userChoice = scanner.nextInt();
                scanner.nextLine();
            }

            if (userChoice == 1) {
                createStory();
            } else if (userChoice == 2) {
                System.out.println("How you want to read the story?");
                System.out.println("1. Read a story already in the database");
                System.out.println("2. Read an uploaded story");
                System.out.print("--> ");
                int userChoice2 = scanner.nextInt();
                scanner.nextLine();
                if (userChoice2 == 1) {
                    System.out.println("How you want to find the story");
                    System.out.println("1. Search through the name");
                    System.out.println("2. Search through database");
                    int storyChoice1 = scanner.nextInt();
                    scanner.nextLine();
                    if (storyChoice1 == 1) {
                        System.out.println("Enter title of the story:");
                        String titleEntered = scanner.nextLine();
                        try {
                            storySelected = storyCol.get(titleEntered);
                        } catch (IllegalArgumentException e) {
                            System.out.println("No story exist with that title");
                            System.out.println("Please Enter a valid story title: ");
                            titleEntered = scanner.nextLine();
                        }

                        System.out.println("What do you want to do with the story? (Play/Edit)");
                        String storyChoice2 = scanner.nextLine();
                        if (storyChoice2.equalsIgnoreCase("Play")) {
                            playStory(storySelected);
                        } else if (storyChoice2.equalsIgnoreCase("Edit")) {
                            editStory(storySelected);
                        }
                    } else if (storyChoice1 == 2) {
                        Iterator<Map.Entry<String, Story>> itr = storyCol.entrySet().iterator();
                        int i = 0;
                        String possTitle;
                        while (itr.hasNext()) {
                            possTitle = itr.next().getKey();
                            System.out.println((i + 1) + ". " + possTitle);
                        }
                        System.out.println("Enter title of the story:");
                        String storyChoice = scanner.nextLine();
                        storySelected = storyCol.get(storyChoice);

                        //todo: check to see if the story doesnt exist

                        System.out.println("What do you want to do with the story? (Play/Edit)");
                        String storyChoice2 = scanner.nextLine();
                        if (storyChoice2.equalsIgnoreCase("Play")) {
                            playStory(storySelected);
                        } else if (storyChoice2.equalsIgnoreCase("Edit")) {
                            editStory(storySelected);
                        }
                    }
                }
                if (userChoice2 == 2) {
                    storySelected = importUI();
//                    uploadStory();
                    System.out.println("What do you want to do with the story? (Play/Edit)");
                    String storyChoice2 = scanner.nextLine();
                    if (storyChoice2.equalsIgnoreCase("Play")) {
                        playStory(storySelected);
                    } else if (storyChoice2.equalsIgnoreCase("Edit")) {
                        editStory(storySelected);
                    }
                }

            } else if (userChoice == 3) {
                System.out.println("What would you like to do with you files?");
                System.out.println("1. Import");
                System.out.println("2. Export");
                int fileChoice = scanner.nextInt();
                scanner.nextLine();
                if (fileChoice == 1) {
                    storySelected = importUI();
                    System.out.println("What do you want to do with the story? (Play/Edit)");
                    String storyChoice3 = scanner.nextLine();
                    if (storyChoice3.equalsIgnoreCase("play")) {
                        playStory(storySelected);
                    } else if (storyChoice3.equalsIgnoreCase("Edit")) {
                        editStory(storySelected);
                    }
                } else if (fileChoice == 2) {
                    System.out.print("Enter the title of the story to export (case sensitive): ");
                    String exportTitle = scanner.nextLine();
                    exportUI(storyCol.get(exportTitle));
                }

            }else if (userChoice == 4) {
                int helpChoice = 0;
                while (helpChoice != 4) {
                    System.out.println("What would you like to learn more about?");
                    System.out.println("1. Creating Stories");
                    System.out.println("2. Loading Stories");
                    System.out.println("3. Editing Stories");
                    System.out.println("4. Exit to Main Menu");
                    helpChoice = scanner.nextInt();
                    if (helpChoice == 1) {
                        System.out.println("When creating a story, the first thing you must choose is a title.");
                        System.out.println("Then you must write the story content for the root (first) story node.");
                        System.out.println("Neither field can be blank.");
                        System.out.println("You can then add a node (a), delete a node (d), edit a node(e), view all nodes (v), add tags (t), change title (ct), or finalize story(x).");
                    } else if (helpChoice == 2) {
                        System.out.println("When loading a story, you can load from the database or an external file.");
                        System.out.println("When loading from the database you can immediately play a story by providing the title.");
                        System.out.println("If you're not sure of the title, you can display all the available titles to play.");
                        System.out.println("When loading from an external file, providing the file path of the story will load it in.");
                    } else if (helpChoice == 3) {
                        System.out.println("When editing a story, you can edit nodes or change the title of the story.");
                        System.out.println("Editing nodes allows you to change story content, rearrange nodes and children, set end nodes, or delete nodes.");
                    }
                }
            }



        } while (userChoice != 5);

        System.out.print("Thank you for using Digress!");
    }
}