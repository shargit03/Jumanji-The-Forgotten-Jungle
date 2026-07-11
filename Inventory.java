import java.util.Vector;

public class Inventory
{
    private Vector<Item> items;

    public Inventory()
    {
        items = new Vector<>();
    }

    public void addItem(Item item)
    {
        items.add(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    public void showInventory()
    {

        if (items.isEmpty())
        {
            System.out.println("Your inventory is empty.");
            return;
        }

        System.out.println("Inventory:");
        for (Item item : items)
        {
            item.describe();
        }
    }

    public boolean hasItem(String itemName)
    {

        for (Item item : items)
        {
            if (item.getName().equals(itemName))
            {
                return true;
            }
        }
        return false;
    }
}