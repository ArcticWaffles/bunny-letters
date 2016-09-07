package bunnyletters;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yoyoconundrum
 */
public class Player
{

    //Player state

    private int carrotCount = 1;
    private int carrotGrowth;
    private int carrotEarnSteps;
    private int currentLevel;

    private ArrayList<Boolean> currentMask;

    private final int CARROT_GROWTH_MAX = 5;
    public final int CARROT_EARN_MAX = 3;
    //TODO: Make the above ints static?

    //Not part of saved game
    private boolean returningPlayer = false;

    public Player() throws IOException
    {
        FileInputStream file = null;
        try
        {
            file = new FileInputStream("Config");
            BufferedReader input = new BufferedReader(new InputStreamReader(file));
            String line = null;
            for (int j = 0; j <= 4; j++)
            {
                line = input.readLine();
                switch (j)
                {
                    case 0:
                        carrotCount = Integer.parseInt(line);
                        break;
                    case 1:
                        carrotGrowth = Integer.parseInt(line);
                        break;
                    case 2:
                        carrotEarnSteps = Integer.parseInt(line);
                        break;
                    case 3:
                        currentLevel = Integer.parseInt(line);
                        break;
                    case 4:
                        currentMask = parseMask(line);
                        break;
                }
            }
            setReturningPlayer(true);
        } catch (FileNotFoundException ex)
        {
            //Saved game not found; assume it's the first run. Default settings remain.
        } finally
        {
            if (file != null)
            {
                file.close();
            }
        }
    }

    public void growCarrot()
    {
        carrotGrowth++;
        if (carrotGrowth == CARROT_GROWTH_MAX)
        {
            addCarrot();
            carrotGrowth = 0;
        }
        saveGame();
    }

    public void earnCarrotStep()
    {
        carrotEarnSteps++;
        if (carrotEarnSteps == CARROT_EARN_MAX)
        {
            addCarrot();
            carrotEarnSteps = 0;
        }
        saveGame();
    }

    /**
     * @return the carrotCount
     */
    public int getCarrotCount()
    {
        return carrotCount;
    }

    private void addCarrot()
    {
        carrotCount = carrotCount + 1;
        saveGame();
    }

    public void subtractCarrot()
    {
        if (carrotCount > 0)
        {
            carrotCount = carrotCount - 1;
            saveGame();
        }
    }

    /**
     * @return the currentLevel
     */
    public int getCurrentLevel()
    {
        return currentLevel;
    }

    /**
     * @param currentLevel the currentLevel to set
     */
    public void setCurrentLevel(int currentLevel)
    {
        this.currentLevel = currentLevel;
        saveGame();
    }

    public ArrayList<Boolean> getCurrentMask()
    {
        return currentMask;
    }

    public void setCurrentMask(ArrayList<Boolean> currentMask)
    {
        this.currentMask = currentMask;
        saveGame();
    }

    private void saveGame()
    {
        FileWriter writer = null;
        try
        {
            writer = new FileWriter("Config", false);
            String n = System.lineSeparator();
            writer.write(carrotCount + n + carrotGrowth + n + carrotEarnSteps + n + currentLevel + n);
            if (getCurrentMask() != null)
            {
                for (Boolean currentBool : currentMask)
                {
                    writer.write(currentBool + ",");
                }
            }
        }
        catch (IOException e)
        {
            Logger.getLogger("").log(Level.SEVERE, null, e);
            System.out.println("Sorry, there was a problem saving your game! :(");
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                } catch (Exception e)
                {
                    Logger.getLogger("").log(Level.SEVERE, null, e);
                }
            }
        }
    }

    private ArrayList<Boolean> parseMask(String line)
    {
        try
        {
            ArrayList<Boolean> result = new ArrayList<>();
            for (String currentString : line.split(","))
            {
                switch (currentString)
                {
                    case "true":
                        result.add(true);
                        break;
                    case "false":
                        result.add(false);
                        break;
                    default:
                        throw new Exception("Invalid value found for saved mask:" + currentString);
                }
            }
            return result;
        }
        catch(Exception e)
        {
            Logger.getLogger("").log(Level.SEVERE, null, e);
            System.out.println("Trouble reading saved game file. You'll resume at the same level but you may be seeing different letters missing.");
            return null;
        }
    }

    /**
     * @return the returningPlayer
     */
    public boolean isReturningPlayer()
    {
        return returningPlayer;
    }

    /**
     * @param returningPlayer the returningPlayer to set
     */
    private void setReturningPlayer(boolean returningPlayer)
    {
        this.returningPlayer = returningPlayer;
    }
}
