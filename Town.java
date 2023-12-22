/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean easyMode;
    public String[] treasureResults;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean easyMode, boolean samurai) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        treasureResults = new String[]{"a crown", "a trophy", "a gem", "dust"};

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        if (easyMode) {
            this.easyMode = easyMode;
        }
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            if (item.equals("machete") && hunter.hasItemInKit("sword")) {
                printMessage = "You used your sword to cross the " + terrain.getTerrainName() + ".";
            } else {
                printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            }
            if (checkItemBreak() && !(item.equals("machete") && hunter.hasItemInKit("sword"))) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            if (hunter.hasItemInKit("sword")) {
                int goldDiff = (int) (Math.random() * 10) + 1;
                printMessage = "the brawler, seeing your sword, realizes he picked a losing fight and gives you his gold";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
                int goldDiff = (int) (Math.random() * 10) + 1;
                double victory = Math.random();
                if (easyMode) {
                    victory *= 2;
                }
                if (victory > noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }
    public void huntForTreasure() {
        String temp = "";
        int i = (int)(Math.random() * 4 + 1);
        if (i == 1) {
            printMessage = "You found " + treasureResults[0] + "!";
            temp = treasureResults[0];
            if (hunter.hasItemInTreasures(temp) == true) {
                printMessage += "\nDon't be too greedy...";
            } else {
                hunter.addTreasure(temp);
            }
        } else if (i == 2) {
            printMessage = "You found " + treasureResults[1] + "!";
            temp = treasureResults[1];
            if (hunter.hasItemInTreasures(temp) == true) {
                printMessage += "\nDon't be too greedy...";
            } else {
                hunter.addTreasure(temp);
            }
        } else if (i == 3) {
            printMessage = "You found " + treasureResults[2] + "!";
            temp = treasureResults[2];
            if (hunter.hasItemInTreasures(temp) == true) {
                printMessage += "\nDon't be too greedy...";
            } else {
                hunter.addTreasure(temp);
            }
        } else {
            printMessage = "You found " + treasureResults[3] + "!";
        }
        if (printMessage.contains("dust")) {
            printMessage += "\nThere's nothing here you wee little lad.";
        }
    }
    public void cancelMessage() {
        printMessage = "";
    }
    public void digForGold() {
        if (hunter.hasItemInKit("shovel")) {
            if (Math.random() > 0.5) {
                int goldDiff = (int) (Math.random() * 20) + 1;
                printMessage = "You dug up " + Colors.YELLOW + goldDiff + " gold!" + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage = "You dug but only found dirt";
            }
        } else {
            printMessage = "You can't dig for gold without a shovel!";
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = (int)(Math.random() * 6 + 1);
        if (rnd == 1) {
            return new Terrain(Colors.WHITE + "Mountains" + Colors.RESET, "Rope");
        } else if (rnd == 2) {
            return new Terrain(Colors.BLUE + "Ocean" + Colors.RESET, "Boat");
        } else if (rnd == 3) {
            return new Terrain(Colors.YELLOW + "Plains" + Colors.RESET, "Horse");
        } else if (rnd == 4) {
            return new Terrain(Colors.YELLOW + "Desert" + Colors.RESET, "Water");
        } else if (rnd == 5){
            return new Terrain(Colors.GREEN + "Jungle" + Colors.RESET, "Machete");
        } else {
            return new Terrain(Colors.GREEN + "Marsh" + Colors.RESET, "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        if (easyMode) {
            rand = 1;
        }
        return (rand < 0.5);
    }
}