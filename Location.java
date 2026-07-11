import java.util.Vector ;

public class Location {

    private Vector <Item> items = new Vector <>();
    private Vector <Companion> companions = new Vector <>();

    private String name;
    private String description;

    private Location north;
    private Location south;
    private Location east;
    private Location west;

    private boolean northUnlocked = false;
    private boolean southUnlocked = false;
    private boolean eastUnlocked = false;
    private boolean westUnlocked = false;

    
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setNorth(Location location, boolean unlocked) {
        this.north = location;
        this.northUnlocked = unlocked;
    }

    public void setSouth(Location location, boolean unlocked) {
        this.south = location;
        this.southUnlocked = unlocked;
    }

    public void setEast(Location location, boolean unlocked) {
        this.east = location;
        this.eastUnlocked = unlocked;
    }

    public void setWest(Location location, boolean unlocked) {
        this.west = location;
        this.westUnlocked = unlocked;
    }

    public void unlockNorth() { northUnlocked = true; }
    public void unlockSouth() { southUnlocked = true; }
    public void unlockEast() { eastUnlocked = true; }
    public void unlockWest() { westUnlocked = true; }

    public Location getExit(String direction) {

        switch (direction) {
            case "north":
                return northUnlocked ? north : null;
            case "south":
                return southUnlocked ? south : null;
            case "east":
                return eastUnlocked ? east : null;
            case "west":
                return westUnlocked ? west : null;
            default:
                return null;
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String itemName) {

        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public void showItems() {

        if (items.isEmpty()) {
            System.out.println("No items here.");
            return;
        }

        System.out.println("You see:");
        for (Item item : items) {
            item.describe();
        }
    }

    public void addCompanion(Companion companion) {
        companions.add(companion);
    }

    public Companion getCompanion(String name) {

        for (Companion c : companions) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public void showCompanions() {

        if (!companions.isEmpty()) {
            System.out.print("You see: ");
            for (Companion c : companions) {
                System.out.print(c.getName() + " ");
            }
            System.out.println();
        }
    }

    public void describe() {

        System.out.println("\nYou are at: " + name);
        System.out.println(description);

        System.out.print("Available paths: ");

        if (north != null && northUnlocked) System.out.print("north ");
        if (south != null && southUnlocked) System.out.print("south ");
        if (east != null && eastUnlocked) System.out.print("east ");
        if (west != null && westUnlocked) System.out.print("west ");

        System.out.println();

        showItems();
        showCompanions();
    }

    public String getName() {
        return name;
    }
}