public class Companion
{
    private String name;
    private String dialogue;

    public Companion(String name, String dialogue)
    {
        this.name = name.toLowerCase();
        this.dialogue = dialogue;
    }

    public String getName()
    {
        return name;
    }

    public void speak()
    {
        System.out.println(name.toUpperCase() + ": " + dialogue);
    }
}