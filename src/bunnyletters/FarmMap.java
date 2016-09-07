package bunnyletters;

/**
 *
 * @author tisafoster
 */
public class FarmMap
{
   
    private Zone[] zones;

    public FarmMap()
    {
        zones = new Zone[] {
            new Zone("Entrance", new String[]{
                "Welcome to Bunny Farms",
                "Please Close the Gate",
                "Speed Limit 10",
                "Watch Out For Bunnies",
                "Barn Parking Ahead"}),
            new Zone("Barn", new String[]{
                "Left Turn Only",
                "Bonfire Tomorrow Night",
                "Hay Rides Five Dollars",
                "Petting Zoo This Way"}),
            new Zone("Petting zoo", new String[]{
                "Feed: 25 cents",
                "Beware of Llama Spit",
                "Sheep",
                "Pony Rides",
                "Don't Feed the Leeches",
                "Carrot Patch Straight Ahead"}),
            new Zone("Carrot patch", new String[]{
                "Two Dollars per Bushel",
                "Watch Your Step - Bunny Holes!",
                "Pick Your Own Apples"}),
            new Zone("Orchard", new String[]{
                "Borrow Ladders and Hats",
                "Apples Five Dollars Per Bushel",
                "Pears Six Dollars Per Bushel",
                "Cherries Two Dollars Per Basket",
                "Pond This Way"}),
            new Zone("Pond", new String[]{
                "Catch and Release Only",
                "Canoe Rentals Available"}),
            new Zone("Corn Maze", new String[]{
                "Pumpkin Patch",
                "Stop by the Gift Shop"}),
            new Zone("Gift Shop", new String[]{
                "Fresh Cherries",
                "Homemade Applesauce",
                "Marmalade on Sale Today"}),
            new Zone("Exit", new String[]{
                "Come Back Soon!"})
        };
    }
    

    /**
     * Get the value of zones
     *
     * @return the value of zones
     */
    public Zone[] getZones()
    {
        return zones;
    }
 
}
