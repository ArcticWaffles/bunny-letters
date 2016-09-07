package bunnyletters;

/**
 *
 * @author tisafoster
 */
public class Zone
{
    private final String name;
    private final String[] levelPhrases;

    public Zone(String name, String[] levelPhrases)
    {
        this.name = name;
        this.levelPhrases = levelPhrases;
    }
    
    /**
     * Get the value of levelPhrases
     *
     * @return the value of levelPhrases
     */
    public String[] getLevelPhrases()
    {
        return levelPhrases;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName()
    {
        return name;
    }

}
