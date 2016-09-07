package bunnyletters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author tisafoster
 */
//TODO: Break into more classes in preparation for GUI
public class BunnyLetters
{

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static Player thePlayer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            FileHandler handler = new FileHandler("BunnyLetters.Log");
            handler.setFormatter(new SimpleFormatter());
            LogManager.getLogManager().reset();
            Logger.getLogger("").addHandler(handler);

            try
            {
                thePlayer = new Player();
            } catch (IOException e)
            {
                Logger.getLogger("").log(Level.SEVERE, null, e);
                System.out.println("Trouble reading saved game file");
            }
            
            if (thePlayer.isReturningPlayer())
            {
                writeWelcomeBackMessage();
            } else
            {
                writeWelcomeMessage();
            }
            String[] levelPhrases = getLevelPhrases();
            for (int j = thePlayer.getCurrentLevel(); j < levelPhrases.length; j++)
            {
                LevelOutcome levelResult = runLevel(levelPhrases[j], j, levelPhrases.length);

                switch (levelResult)
                {
                    case EXIT:
                        writeExitMessage(j);
                        System.out.println("Press Enter to quit");
                        reader.readLine();
                        return;
                    case CORRECT:
                        thePlayer.earnCarrotStep();
                        thePlayer.setCurrentLevel(j + 1);
                        thePlayer.setCurrentMask(null);
                        System.out.println();
                        System.out.println("Correct! You saved the farmer's sign! Press Enter to continue");
                        (new BufferedReader(new InputStreamReader(System.in))).readLine();
                        break;
                    case RESTART:
                        File configFile = new File("Config");
                        configFile.delete();
                        thePlayer = new Player();
                        j = -1;
                        writeWelcomeMessage();
                        break;
                }
            }
            writeWinningMessage();
        } catch (IOException e)
        {
            Logger.getLogger("").log(Level.SEVERE, null, e);
            System.out.println("Sorry, Bunny Farms is undergoing renovations. Please try again later.");
        }
    }

    private static void writeExitMessage(int signCount) throws IOException
    {
        System.out.println();
        if (signCount == 1)
        {
            System.out.println("You saved 1 sign!");
        } else
        {
            System.out.println("You saved " + signCount + " signs!");
        }
        System.out.println();
    }

    private static String createSign(String maskedPhrase)
    {
        StringBuilder sign = new StringBuilder();
        // First row
        sign.append(" ");
        for (int j = 0; j < maskedPhrase.length() + 4; j++)
        {
            sign.append('_');
        }
        sign.append('\n');

        // Second row
        sign.append('|');
        for (int j = 0; j < maskedPhrase.length() + 4; j++)
        {
            sign.append(' ');
        }
        sign.append("|\n");

        // Third row
        sign.append("|  " + maskedPhrase + "  |\n");

        // Fourth row
        sign.append('|');
        for (int j = 0; j < maskedPhrase.length() + 4; j++)
        {
            sign.append('_');
        }
        sign.append("|\n");

        // Fifth row
        for (int j = 0; j < ((maskedPhrase.length() + 6) / 2) - 2; j++)
        {
            sign.append(' ');
        }
        sign.append("| |\n");

        //Sixth row
        for (int j = 0; j < ((maskedPhrase.length() + 6) / 2) - 2; j++)
        {
            sign.append(' ');
        }
        sign.append("|_|\n");

        return sign.toString();
    }

    private static ArrayList<Boolean> reduceMaskDifficulty(ArrayList<Boolean> phraseMask)
    {
        ArrayList<Boolean> result = new ArrayList<>(phraseMask);
        for (int i = 0; i < result.size(); i++)
        {
            if (result.get(i) == true)
            {
                result.set(i, false);
                return result;
            }
        }
        return result;
    }

    private static String promptUser(String theMaskedPhrase) throws IOException
    {
        System.out.println(createSign(theMaskedPhrase));
        if (thePlayer.getCarrotCount() == 1)
        {
            System.out.println("You have " + thePlayer.getCarrotCount() + " carrot\n");
        } else
        {
            System.out.println("You have " + thePlayer.getCarrotCount() + " carrots\n");
        }
        System.out.println("Please enter your guess.\n"
                + "If you need help, type \"Use carrot\" to bribe a bunny to put a letter back.");
        System.out.println("Type \"Exit\" to quit or \"Restart\" to lose all progress and begin a new game");
        System.out.print("Guess: ");
        return reader.readLine().trim();
    }

    private static boolean signIsFull(ArrayList<Boolean> phraseMask)
    {
        for (Boolean currentBool : phraseMask)
        {
            if (currentBool == true)
            {
                return false;
            }
        }
        return true;
    }

    private static void writeWelcomeBackMessage() throws IOException
    {
        System.out.println("\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                + "{}                                                  {}\n"
                + "{}             Welcome Back to Bunny Letters!       {}\n"
                + "{}                                                  {}\n"
                + "{}                      /) /)                       {}\n"
                + "{}                     ( ^.^ )                      {}\n"
                + "{}                    C(\") (\")                      {}\n"
                + "{}                                                  {}\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                + "\n"
                + "Your previous game was saved and we'll start you right where you left off.\n"
                + "You have " + thePlayer.getCarrotCount() + " carrots.\n"
                + "You have saved " + thePlayer.getCurrentLevel() + " signs.\n"
                + "The mischevious bunnies have stolen more letters off the signs at Bunny Farms!\n"
                + "Help the farmer figure out what the signs are supposed to say.\n"
                + "\n"
                + "If you get stuck, you can bribe a bunny with a carrot to put a letter back on the sign.\n"
                + "You can earn a carrot by winning " + thePlayer.CARROT_EARN_MAX + " levels.\n"
                + "You also gradually grow carrots in your carrot patch.\n"
                + "\n"
                + "Press Enter to continue.\n");
        reader.readLine();
    }

    private enum LevelOutcome
    {

        EXIT, CORRECT, RESTART
    }

    private static void writeWinningMessage() throws IOException
    {
        System.out.println("\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                + "{}                                                  {}\n"
                + "{}     Congratulations! You saved Bunny Farms!      {}\n"
                + "{}                                                  {}\n"
                + "{}                      /) /)                       {}\n"
                + "{}                     ( ^.^ )                      {}\n"
                + "{}                    C(\") (\")                      {}\n"
                + "{}                                                  {}\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n");
        System.out.println("Press Enter to leave Bunny Farms");

        reader.readLine();
    }

    /**
     * Masks out letters of phrase with underscores based on mask.
     *
     * @param phrase A string to be masked.
     * @param mask a boolean array where true elements get masked in the phrase
     * string
     * @return masked version of the phrase string.
     */
    private static String removeLetters(String phrase, ArrayList<Boolean> mask)
    {
        if (phrase.length() != mask.size())
        {
            throw new IllegalArgumentException("Mask length must match phrase length");
        }

        StringBuilder result = new StringBuilder(phrase);
        for (int j = 0; j < phrase.length(); j = j + 1)
        {
            if (mask.get(j) && Character.toString(phrase.charAt(j)).matches("\\w"))
            {
                result.setCharAt(j, '_');
            }
        }
        return result.toString();
    }

    /**
     * Generates an array of random booleans
     *
     * @param length Length of the array to be generated
     * @return An array of random booleans of specified length
     */
    private static ArrayList<Boolean> generateMask(String phrase, double progressPercentage)
    {
        double maskProbability = (progressPercentage * 0.2) + 0.4;

        ArrayList<Boolean> workingMask = new ArrayList<>();
        String[] individualWords = phrase.split(" ");
        Random randomGenerator = new Random();

        for (String currentWord : individualWords)
        {
            workingMask.add(false);
            for (int i = 1; i < currentWord.length(); i++)
            {
                if (randomGenerator.nextDouble() < maskProbability)
                {
                    workingMask.add(true);
                } else
                {
                    workingMask.add(false);
                }
            }
            workingMask.add(false);

        }
        workingMask.remove(workingMask.size() - 1);
        return workingMask;
    }

    private static void writeWelcomeMessage() throws IOException
    {
        System.out.println("\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                + "{}                                                  {}\n"
                + "{}             Welcome to Bunny Letters!            {}\n"
                + "{}                                                  {}\n"
                + "{}                      /) /)                       {}\n"
                + "{}                     ( ^.^ )                      {}\n"
                + "{}                    C(\") (\")                      {}\n"
                + "{}                                                  {}\n"
                + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                + "\n"
                + "Some mischevious bunnies stole letters off the signs at Bunny Farms!\n"
                + "Help the farmer figure out what the signs are supposed to say.\n"
                + "\n"
                + "If you get stuck, you can bribe a bunny with a carrot to put a letter back on the sign.\n"
                + "You can earn a carrot by winning " + thePlayer.CARROT_EARN_MAX + " levels.\n"
                + "You also gradually grow carrots in your carrot patch.\n"
                + "\n"
                + "Press Enter to continue.\n");
        reader.readLine();
    }

    private static String[] getLevelPhrases()
    {
        //TODO: Show new levels each game
        String[] levelPhrases = {
        "Welcome to Bunny Farms",
        "Please Close the Gate",
        "Speed Limit 10",
        "Watch Out For Bunnies",
        "Bonfire Tomorrow Night",
        "Petting Zoo This Way",
        "Pumpkin Patch",
        "Hay Rides Five Dollars",
        "Left Turn Only",
        "Pick Your Own Apples",
        "Stop by the Gift Shop",
        "Marmalade on Sale Today"};
        return levelPhrases;
    }

    private static LevelOutcome runLevel(String currentLevelPhrase, int levelNumber, int totalLevels) throws IOException
    {
        System.out.println("Level " + (levelNumber + 1));
        String thePhrase = currentLevelPhrase;
        if (thePlayer.getCurrentMask() == null)
        {
            thePlayer.setCurrentMask(generateMask(thePhrase, (double) (levelNumber) / (double) (totalLevels - 1)));
        }
        String maskedPhrase = removeLetters(thePhrase, thePlayer.getCurrentMask());

        String answer;

        // Guessing loop
        do
        {
            answer = promptUser(maskedPhrase);

            if (answer.equalsIgnoreCase("Exit"))
            {
                return LevelOutcome.EXIT;
            } else if (answer.equalsIgnoreCase("Use Carrot"))
            {
                if (signIsFull(thePlayer.getCurrentMask()))
                {
                    System.out.println("\nThe bunny has no more letters to give you for this sign. Press Enter to continue.");
                    reader.readLine();
                } else if (thePlayer.getCarrotCount() > 0)
                {
                    thePlayer.subtractCarrot();
                    ArrayList<Boolean> newMask = reduceMaskDifficulty(thePlayer.getCurrentMask());
                    thePlayer.setCurrentMask(newMask);
                    maskedPhrase = removeLetters(thePhrase, thePlayer.getCurrentMask());
                    System.out.println("\n"
                            + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n"
                            + "{}                                                  {}\n"
                            + "{}               You bribed a bunny!                {}\n"
                            + "{}                                                  {}\n"
                            + "{}                    /) /)    \\V/                  {}\n"
                            + "{}                   ( $.$ )   (=)                  {}\n"
                            + "{}                  C(\") (\")    V                   {}\n"
                            + "{}                                                  {}\n"
                            + "{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}\n");
                    System.out.println("\nA letter was added. Press Enter to see the sign.");
                    reader.readLine();
                } else
                {
                    System.out.println("\nSorry, you have no carrots left! Press Enter to continue.");
                    reader.readLine();
                }
            } else if (answer.equalsIgnoreCase("Restart"))
            {
                return LevelOutcome.RESTART;
            } else if (!answer.equalsIgnoreCase(thePhrase))
            {
                thePlayer.growCarrot();
                continue;
            } else
            {
                return LevelOutcome.CORRECT;
            }
        } while (true);
    }
}
