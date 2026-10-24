import java.util.Scanner;

/**
 * Floridian Tooth Records.
 * Records the teeth of one Florida family (maximum 6 people). Each person
 * has an upper and a lower row of up to 8 teeth. The teeth are stored in a
 * three-dimensional array of characters: plane = person, row = layer
 * (upper or lower), column = tooth. A menu then lets the user print the
 * records, extract a tooth, report the family's root canal indices, or exit.
 * @author Maggie Padavan
 * @version 1.1
 */

public class DentalRecords {

    /** Maximum number of people in the family. */
    private static final int MAX_PEOPLE = 6;
    /** Number of tooth layers (uppers and lowers). */
    private static final int LAYER_COUNT = 2;
    /** Maximum number of teeth (including missing) in a layer. */
    private static final int MAX_TEETH = 8;
    /** Index of the uppers layer. */
    private static final int UPPER = 0;
    /** Index of the lowers layer. */
    private static final int LOWER = 1;
    /** Tooth type letter for incisors. */
    private static final char INCISOR = 'I';
    /** Tooth type letter for bicuspids. */
    private static final char BICUSPID = 'B';
    /** Tooth type letter for missing teeth. */
    private static final char MISSING = 'M';
    /** Menu letter for printing the records. */
    private static final char PRINT_OPTION = 'P';
    /** Menu letter for extracting a tooth. */
    private static final char EXTRACT_OPTION = 'E';
    /** Menu letter for reporting the root canal indices. */
    private static final char ROOT_OPTION = 'R';
    /** Menu letter for exiting. */
    private static final char EXIT_OPTION = 'X';
    /** Layer letter for the uppers. */
    private static final char UPPER_LETTER = 'U';
    /** Layer letter for the lowers. */
    private static final char LOWER_LETTER = 'L';
    /** Width of the prompt text before the colon. */
    private static final int PROMPT_WIDTH = 43;
    /** Value used when a family member's name is not found. */
    private static final int NOT_FOUND = -1;
    /** Welcome banner (%n is a line break). */
    private static final String WELCOME_MESSAGE = "Welcome to the Floridian Tooth Records%n" + "--------------------------------------%n";
    /** Goodbye message (%n is a line break). */
    private static final String GOODBYE_MESSAGE =
            "%nExiting the Floridian Tooth Records :-)%n";

    /**
     * Runs the program: displays the welcome, gets the family's teeth,
     * processes the menu until the user exits, and displays the goodbye.
     * There are three branches: displayMessage (used for both messages),
     * getFamilyData, and processMenu.
     *
     * @param args not used
     */

    public static void main(String[] args) {

        Scanner keyboard;
        String[] names;
        char[][][] teeth;

        //----The one Scanner is created here and passed to who needs it
        keyboard = new Scanner(System.in);

        //----Room for the maximum family; only the first familySize used
        names = new String[MAX_PEOPLE];

        displayMessage(WELCOME_MESSAGE);
        teeth = getFamilyData(keyboard, names);
        processMenu(keyboard, names, teeth);
        displayMessage(GOODBYE_MESSAGE);
    }

    //----Output and input helpers

    /**
     * Displays a message (used for both the welcome and the goodbye).
     *
     * @param message the message to display; %n gives a line break
     */

    private static void displayMessage(String message) {

        System.out.printf(message);
    }

    /**
     * Prints a prompt with the colon lined up in a column.
     *
     * @param text the prompt text
     */

    private static void prompt(String text) {

        System.out.printf("%-" + PROMPT_WIDTH + "s : ", text);
    }

    /**
     * Reads a line from the keyboard, trimmed of surrounding spaces.
     *
     * @param keyboard the Scanner to read from
     * @return the trimmed line
     */

    private static String readLine(Scanner keyboard) {

        return keyboard.nextLine().trim();
    }

    //----Getting the family data

    /**
     * Asks for the number of people in the family until it is in range.
     *
     * @param keyboard the Scanner to read from
     * @return the number of people, from 1 to MAX_PEOPLE
     */

    private static int getFamilySize(Scanner keyboard) {

        int size;

        prompt("Please enter number of people in the family");
        size = Integer.parseInt(readLine(keyboard));

        //----Keep asking until the size is in range
        while (size < 1 || size > MAX_PEOPLE) {
            prompt("Invalid number of people, try again");
            size = Integer.parseInt(readLine(keyboard));
        }
        return size;
    }

    /**
     * Gets the family size, then the name, uppers, and lowers for each
     * family member. The number of planes in the returned array is the
     * number of people in the family.
     *
     * @param keyboard the Scanner to read from
     * @param names array to fill with the family member names
     * @return the three dimensional array of tooth types
     */

    private static char[][][] getFamilyData(Scanner keyboard,
                                            String[] names) {

        int familySize;
        int person;
        char[][][] teeth;

        familySize = getFamilySize(keyboard);

        //----A plane of teeth per person
        teeth = new char[familySize][LAYER_COUNT][];

        for (person = 0; person < familySize; person++) {
            prompt("Please enter the name for family member " + (person + 1));
            names[person] = readLine(keyboard);

            //----Each layer becomes one row of the 3D array
            teeth[person][UPPER] = getTeethRow(keyboard, names[person],
                    "uppers");
            teeth[person][LOWER] = getTeethRow(keyboard, names[person],
                    "lowers");
        }
        return teeth;
    }

    /**
     * Gets one row of teeth for a person, asking again until the types are
     * valid and there are not too many teeth.
     *
     * @param keyboard the Scanner to read from
     * @param name the person's name
     * @param layerName "uppers" or "lowers", used in the prompt
     * @return the teeth as upper case tooth type letters
     */

    private static char[] getTeethRow(Scanner keyboard, String name,
                                      String layerName) {

        String line;
        boolean valid;

        prompt("Please enter the " + layerName + " for " + name);

        //----Upper case so that lower case input is accepted
        line = readLine(keyboard).toUpperCase();
        valid = false;
        while (!valid) {
            if (!isValidTeeth(line)) {
                prompt("Invalid teeth types, try again");
                line = readLine(keyboard).toUpperCase();
            } else if (line.length() > MAX_TEETH) {
                prompt("Too many teeth, try again");
                line = readLine(keyboard).toUpperCase();
            } else {
                valid = true;
            }
        }
        return line.toCharArray();
    }

    /**
     * Checks that a string has at least one tooth and that every character
     * is a valid (upper case) tooth type.
     *
     * @param line the string to check
     * @return true if the string is a valid string of tooth types
     */

    private static boolean isValidTeeth(String line) {

        int index;
        boolean valid;
        char type;

        //----An empty string has no teeth, so it is not valid
        valid = !line.isEmpty();

        //----Every character must be an incisor, bicuspid or missing
        for (index = 0; index < line.length(); index++) {
            type = line.charAt(index);
            if (type != INCISOR && type != BICUSPID && type != MISSING) {
                valid = false;
            }
        }
        return valid;
    }

    //----Menu

    /**
     * Processes menu choices until the user chooses to exit.
     *
     * @param keyboard the Scanner to read from
     * @param names the family member names
     * @param teeth the tooth types for the family (may be updated)
     */

    private static void processMenu(Scanner keyboard, String[] names,
                                    char[][][] teeth) {

        char choice;

        do {
            choice = getMenuChoice(keyboard);
            if (choice == PRINT_OPTION) {
                printRecords(names, teeth);
            } else if (choice == EXTRACT_OPTION) {
                extractTooth(keyboard, names, teeth);
            } else if (choice == ROOT_OPTION) {
                reportRootCanals(teeth);
            }
        } while (choice != EXIT_OPTION);
    }

    /**
     * Gets a valid menu choice (in upper or lower case).
     *
     * @param keyboard the Scanner to read from
     * @return the choice as an upper case letter
     */

    private static char getMenuChoice(Scanner keyboard) {

        String line;

        System.out.println();
        prompt("(P)rint, (E)xtract, (R)oot, e(X)it");
        line = readLine(keyboard).toUpperCase();

        //----Keep asking until the choice is one of the menu letters
        while (!isMenuOption(line)) {
            prompt("Invalid menu option, try again");
            line = readLine(keyboard).toUpperCase();
        }
        return line.charAt(0);
    }

    /**
     * Checks whether a string is exactly one of the menu letters.
     *
     * @param line the upper case string to check
     * @return true if the string is a menu option
     */

    private static boolean isMenuOption(String line) {

        char letter;
        boolean valid;

        //----It is only valid if it is a single character
        valid = false;
        if (line.length() == 1) {
            letter = line.charAt(0);
            valid = letter == PRINT_OPTION || letter == EXTRACT_OPTION ||
                    letter == ROOT_OPTION || letter == EXIT_OPTION;
        }
        return valid;
    }

    //----Print option

    /**
     * Prints every family member's teeth record.
     *
     * @param names the family member names
     * @param teeth the tooth types for the family
     */

    private static void printRecords(String[] names, char[][][] teeth) {

        int person;

        System.out.println();
        for (person = 0; person < teeth.length; person++) {
            System.out.println(names[person]);
            printRow("Uppers", teeth[person][UPPER]);
            printRow("Lowers", teeth[person][LOWER]);
        }
    }

    /**
     * Prints one row of teeth, with each tooth numbered from 1.
     *
     * @param label the row label, "Uppers" or "Lowers"
     * @param row the teeth in the row
     */

    private static void printRow(String label, char[] row) {

        int tooth;

        System.out.print("  " + label + ":");

        //----Array index 0 is displayed as tooth number 1
        for (tooth = 0; tooth < row.length; tooth++) {
            System.out.print("  " + (tooth + 1) + ":" + row[tooth]);
        }
        System.out.println();
    }

    //----Extract option

    /**
     * Extracts a tooth: asks which person, layer and tooth, checking each
     * answer, and then marks that tooth as missing.
     *
     * @param keyboard the Scanner to read from
     * @param names the family member names
     * @param teeth the tooth types for the family (updated)
     */

    private static void extractTooth(Scanner keyboard, String[] names,
                                     char[][][] teeth) {

        int person;
        int layer;
        int tooth;

        person = getFamilyMember(keyboard, names, teeth.length);
        layer = getLayer(keyboard);

        //----Do not ask for a tooth if every tooth is already missing
        if (!hasTooth(teeth[person][layer])) {
            System.out.println("There are no teeth to extract there.");
        } else {
            tooth = getToothNumber(keyboard, teeth[person][layer]);
            teeth[person][layer][tooth] = MISSING;
        }
    }

    /**
     * Asks for a family member's name (any case) until it is found.
     *
     * @param keyboard the Scanner to read from
     * @param names the family member names
     * @param familySize the number of people in the family
     * @return the index of the family member
     */

    private static int getFamilyMember(Scanner keyboard, String[] names,
                                       int familySize) {

        int found;
        int person;
        String name;

        prompt("Which family member");
        name = readLine(keyboard);
        found = NOT_FOUND;
        while (found == NOT_FOUND) {

            //----Search for the name, ignoring case
            for (person = 0; person < familySize; person++) {
                if (names[person].equalsIgnoreCase(name)) {
                    found = person;
                }
            }
            if (found == NOT_FOUND) {
                prompt("Invalid family member, try again");
                name = readLine(keyboard);
            }
        }
        return found;
    }

    /**
     * Asks for the tooth layer (U or L, any case) until it is valid.
     *
     * @param keyboard the Scanner to read from
     * @return UPPER or LOWER
     */

    private static int getLayer(Scanner keyboard) {

        String line;
        int layer;

        prompt("Which tooth layer (U)pper or (L)ower");
        line = readLine(keyboard).toUpperCase();

        //----Only a single U or L is accepted
        while (!isLayerLetter(line)) {
            prompt("Invalid layer, try again");
            line = readLine(keyboard).toUpperCase();
        }

        //----Convert the letter into the array index of the layer
        if (line.charAt(0) == UPPER_LETTER) {
            layer = UPPER;
        } else {
            layer = LOWER;
        }
        return layer;
    }

    /**
     * Checks whether a string is exactly one of the layer letters.
     *
     * @param line the upper case string to check
     * @return true if the string is a layer letter
     */

    private static boolean isLayerLetter(String line) {

        char letter;
        boolean valid;

        //----It is only valid if it is a single character
        valid = false;
        if (line.length() == 1) {
            letter = line.charAt(0);
            valid = letter == UPPER_LETTER || letter == LOWER_LETTER;
        }
        return valid;
    }

    /**
     * Checks whether a row has at least one tooth that is not missing.
     *
     * @param row the teeth in the row
     * @return true if there is a tooth that can be extracted
     */

    private static boolean hasTooth(char[] row) {

        int tooth;
        boolean found;

        found = false;
        for (tooth = 0; tooth < row.length; tooth++) {
            if (row[tooth] != MISSING) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Asks for a tooth number until it is in range and the tooth is there.
     *
     * @param keyboard the Scanner to read from
     * @param row the teeth in the row
     * @return the array index (number minus 1) of the tooth to extract
     */

    private static int getToothNumber(Scanner keyboard, char[] row) {

        int number;
        boolean valid;

        prompt("Which tooth number");
        number = Integer.parseInt(readLine(keyboard));
        valid = false;
        while (!valid) {
            if (number < 1 || number > row.length) {
                //----There is no such tooth in this row
                prompt("Invalid tooth number, try again");
                number = Integer.parseInt(readLine(keyboard));
            } else if (row[number - 1] == MISSING) {
                //----The tooth is in range but nothing is there to remove
                prompt("Missing tooth, try again");
                number = Integer.parseInt(readLine(keyboard));
            } else {
                valid = true;
            }
        }

        //----Convert the displayed number back to an array index
        return number - 1;
    }

    //----Root canal option

    /**
     * Reports the family's root canal indices, which are the roots of
     * I*x*x + B*x - M where I, B and M are the family's totals of
     * incisors, bicuspids and missing teeth.
     *
     * @param teeth the tooth types for the family
     */

    private static void reportRootCanals(char[][][] teeth) {

        int incisors;
        int bicuspids;
        int missing;
        double discriminant;
        double root;
        double otherRoot;

        //----Totals for the whole family
        incisors = countType(teeth, INCISOR);
        bicuspids = countType(teeth, BICUSPID);
        missing = countType(teeth, MISSING);

        if (incisors > 0) {
            //----Quadratic; I and M are not negative so the roots are real
            discriminant = bicuspids * bicuspids + 4.0 * incisors * missing;
            root = (-bicuspids + Math.sqrt(discriminant)) / (2.0 * incisors);
            otherRoot = (-bicuspids - Math.sqrt(discriminant)) /
                    (2.0 * incisors);
            System.out.printf("One root canal at    %5.2f%n", root);
            System.out.printf("Another root canal at %5.2f%n", otherRoot);
        } else if (bicuspids > 0) {
            //----No incisors, so Bx - M = 0 has a single root
            root = (double) missing / bicuspids;
            System.out.printf("One root canal at    %5.2f%n", root);
        } else {
            System.out.println("No root canals (no incisors or bicuspids)");
        }
    }

    /**
     * Counts how many teeth of one type the whole family has.
     *
     * @param teeth the tooth types for the family
     * @param type the tooth type letter to count
     * @return the number of teeth of that type
     */

    private static int countType(char[][][] teeth, char type) {

        int person;
        int layer;
        int tooth;
        int count;

        count = 0;

        //----Visit every tooth: each person, each layer, each column
        for (person = 0; person < teeth.length; person++) {
            for (layer = 0; layer < LAYER_COUNT; layer++) {
                for (tooth = 0; tooth < teeth[person][layer].length;
                     tooth++) {
                    if (teeth[person][layer][tooth] == type) {
                        count++;
                    }
                }
            }
        }
        return count;
    }


} // end of the DentalRecords class