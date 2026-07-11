import java.util.Scanner;

public class GameEngine {

    private boolean gameRunning;
    private Scanner scanner;
    private Location currentLocation;
    private Inventory inventory;
    private int playerHealth = 100;

    private boolean villageEventDone = false;

    public GameEngine() {
        scanner = new Scanner(System.in);
        gameRunning = true;
        inventory = new Inventory();
    }

    public void startGame() {
        showIntro();
        createWorld();
        gameLoop();
    }

    private void showIntro() {
        System.out.println("=================================");
        System.out.println("           JUMANJI");
        System.out.println(" The Forgotten Jungle Adventure");
        System.out.println("=================================");

        System.out.println("You find an ancient board game...");
        System.out.println("As you touch it, the world around you changes.");
        System.out.println("You are now inside JUMANJI.");
    }

    private void createWorld() {

        Location village = new Location("Abandoned Village",
                "Ruins of an old village covered in vines.");

        Location river = new Location("Crocodile River",
                "A dangerous river filled with crocodiles.");

        // Connect locations (initial)
        village.setNorth(river, true);

        river.setSouth(village, true);
        river.setNorth(null, false);

        // Create items
        Item machete = new Item("machete", "Useful for cutting vines.");
        Item rope = new Item("rope", "Can help cross obstacles.");

        // Place items
        village.addItem(machete);
        river.addItem(rope);

        // Create companions
        Companion bravestone = new Companion("bravestone",
                "We must stay strong and fight our way through.");

        Companion ruby = new Companion("ruby",
                "Stay alert. This jungle is full of traps.");

        Companion finbar = new Companion("finbar",
                "I can sense danger nearby.");

        Companion oberon = new Companion("oberon",
                "Knowledge will guide us through this jungle.");

        // Place companions
        village.addCompanion(bravestone);
        village.addCompanion(ruby);
        village.addCompanion(finbar);
        village.addCompanion(oberon);

        currentLocation = village;
    }

    private void gameLoop() {

        while (gameRunning) {

            currentLocation.describe();
            showHealth();
            triggerEvent();

            System.out.print("\n>> ");
            String input = scanner.nextLine();

            processCommand(input);
        }
    }

    private void processCommand(String input) {

        input = input.toLowerCase();

        if (input.startsWith("move ")) {
            move(input.substring(5));
            return;
        }

        if (input.startsWith("take ")) {
            takeItem(input.substring(5));
            return;
        }

        if (input.startsWith("use ")) {
            useItem(input.substring(4));
            return;
        }

        if (input.startsWith("talk ")) {
            talkTo(input.substring(5));
            return;
        }

        switch (input) {

            case "inventory":
                inventory.showInventory();
                break;

            case "help":
                showHelp();
                break;

            case "quit":
                System.out.println("Exiting game...");
                gameRunning = false;
                break;

            default:
                System.out.println("Unknown command.");
        }
    }

    private void move(String direction) {

        Location nextLocation = currentLocation.getExit(direction);

        if (nextLocation == null) {
            System.out.println("You can't go that way.");
        } else {
            currentLocation = nextLocation;
            System.out.println("You move " + direction + ".");

            if (currentLocation.getName().toLowerCase().contains("river")) {
                System.out.println("The river is dangerous!");
                damagePlayer(20);
            }
        }
    }

    private void takeItem(String itemName) {

        Item item = currentLocation.removeItem(itemName);

        if (item == null) {
            System.out.println("No such item here.");
        } else {
            inventory.addItem(item);
        }
    }

    private void useItem(String itemName) {

        if (!inventory.hasItem(itemName)) {
            System.out.println("You don't have that item.");
            return;
        }

        String locationName = currentLocation.getName().toLowerCase();

        if (locationName.contains("river") && itemName.equals("rope")) {
            System.out.println("You use the rope to cross the river.");
            System.out.println("A path to the temple is revealed!");

            Location temple = new Location("Final Temple",
                    "The heart of Jumanji. The magical crystal lies here.");

            currentLocation.setNorth(temple, true);
            temple.setSouth(currentLocation, true);

            return;
        }

        if (locationName.contains("village") && itemName.equals("machete")) {
            System.out.println("You cut through thick vines and discover hidden paths.");
            return;
        }

        System.out.println("Nothing happens.");
    }

    private void talkTo(String name) {

        Companion companion = currentLocation.getCompanion(name);

        if (companion == null) {
            System.out.println("No such person here.");
        } else {
            companion.speak();

            if (name.equals("finbar") && currentLocation.getName().toLowerCase().contains("river")) {
                System.out.println("Finbar: Maybe a rope could help us cross.");
            }
        }
    }

    private void triggerEvent() {

        String locationName = currentLocation.getName().toLowerCase();

        if (locationName.contains("village") && !villageEventDone) {

            villageEventDone = true;

            int choice = showChoices(
                    "You see abandoned huts. What will you do?",
                    new String[]{
                            "Search the huts",
                            "Leave immediately",
                            "Ask companions"
                    });

            handleVillageChoice(choice);
        }

        if (locationName.contains("temple")) {

            int choice = showChoices(
                    "You see the magical crystal. What will you do?",
                    new String[]{
                            "Take the crystal",
                            "Leave it",
                            "Destroy it"
                    });

            handleFinalChoice(choice);
        }
    }

    private int showChoices(String question, String[] options) {

        System.out.println("\n" + question);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.print("Enter choice: ");

        int choice = -1;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }

        return choice;
    }

    private void handleVillageChoice(int choice) {

        switch (choice) {

            case 1:
                System.out.println("You search the huts and find useful supplies.");
                break;

            case 2:
                System.out.println("You decide it's too dangerous and step back.");
                break;

            case 3:
                System.out.println("Your companions share their thoughts:");
                talkTo("bravestone");
                talkTo("ruby");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private void handleFinalChoice(int choice) {

        switch (choice) {

            case 1:
                System.out.println("You take the crystal and escape Jumanji!");
                endGame(true);
                break;

            case 2:
                System.out.println("You hesitate... the jungle consumes you.");
                endGame(false);
                break;

            case 3:
                System.out.println("You destroy the crystal and break the curse!");
                endGame(true);
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private void showHealth() {
        System.out.println("Health: " + playerHealth);
    }

    private void damagePlayer(int damage) {

        playerHealth -= damage;

        System.out.println("You lost " + damage + " health!");

        if (playerHealth <= 0) {
            System.out.println("You have been defeated...");
            endGame(false);
        }
    }

    private void endGame(boolean win) {

        if (win) {
            System.out.println("\n🏆 You have escaped JUMANJI!");
        } else {
            System.out.println("\n💀 Game Over!");
        }

        gameRunning = false;
    }

    private void showHelp() {
        System.out.println("\nCommands:");
        System.out.println("move north/south/east/west");
        System.out.println("take <item>");
        System.out.println("use <item>");
        System.out.println("talk <character>");
        System.out.println("inventory");
        System.out.println("help");
        System.out.println("quit");
    }
}