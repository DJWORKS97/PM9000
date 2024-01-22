import java.util.*;
import java.io.IOException;

public class PM9000 {
    public static void validate(String input) {
        // Create an array of cards by splitting string by comma, remove whitespace either before and after comma.
        String[] hand = input.split("\\s*,\\s*");

        // Check that user entered correct number of cards (5)
        if (hand.length != 5) { System.out.println("You can only enter 5 cards, a card is seperated by a comma (,)"); System.exit(0); }

         // Validate inputs are correct for each card
         for (int i = 0; i < 5; i++ ) {
            // Parse input for ranks, then check they are between 0-13.
            int rank = Integer.parseInt(hand[i].replaceAll("[^0-9]", ""));
            if (rank < 0 || rank > 13) {System.out.println("The numbers entered must be between 0-13 to match a card rank (Ace-King = 0-13)."); System.exit(0); }

            // Parse input for suits
            String suitCheck = hand[i].replaceAll("[0-9]","");
            // Validate only one letter was entered for each card
            if (suitCheck.length() != 1) {System.out.println("You can only enter one letter for each card."); System.exit(0);}
            // convert any lowercase to uppercase
            suitCheck = suitCheck.toUpperCase();
            // Convert string to char
            char suit = suitCheck.charAt(0);
            // Validate only D H S and C were used for input.
            if (suit != 'D' && suit != 'H' && suit != 'S' && suit != 'C' ) {System.out.println("Letter entered does not correspond to a suit, only D, H, S and C are acceptable."); System.exit(0); }
        }

        // Convert user input into a list, to then convert it to a hashset for use of collections.frequency to check for duplicate entries.
        List<String> asList = Arrays.asList(hand);
        Set<String> mySet = new HashSet<>(asList);

        for(String s: mySet) {
            if (Collections.frequency(asList,s) > 1) {
                System.out.println("You cannot enter the same card twice.");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        // Program Introduction
        System.out.println("Welcome to The PokerMatic9000 - a program that outputs the highest poker rank from the inputted poker hand.");

        // Receive input in the form of a comma delimited string containing 5 values ("11C, 11D, 11S, 0S, 3H")
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your hand in the form of a comma delimited string containing 5 cards e.g. \"11C, 11D, 11S, 0S, 3H\"");
        // User input
        String input = scan.nextLine();
        // Close system.in to ensure no leaks.
        scan.close();

        // Ensure input meets requirements
        validate(input);

        // A string array for ranks and suits to check for conditions in a loop.
        String[] handRanks = input.split("\\s*,\\s*");
        String[] handSuits = input.split("\\s*,\\s*");
        // Initialise an int array for sorting ranks to check for straights / royal flush
        int[] sortRanks = {0,0,0,0,0};
        // Initialise Flush/Four of a kind booleans for future conditions.
        boolean flush = false;
        boolean fourOfAKind = false;


        // initialise suitCount - which is used to counut the number of suits in the hand
        int suitCount = 0;
        for (int i = 0; i < 5; i++ ) {
            // Fill int array with ranks, removing suit for sorting ranks.
            // Fill sting array with ranks, removing suit for checking for duplicate ranks (checking for pairs/trios)
            int rank = Integer.parseInt(handRanks[i].replaceAll("[^0-9]", ""));
            handRanks[i] = String.valueOf(rank);
            sortRanks[i] = rank;

            // Fill suit array with just suits, removing ranks.
            String suit = handSuits[i].replaceAll("[0-9]","");
            handSuits[i] = suit;

            // Checks all suits are the same by comparing current and next suits in the loop.
            if (i >= 1) {
                if (handSuits[i].charAt(0) == handSuits[i-1].charAt(0)) {
                    suitCount += 1;
                    if (suitCount == 4 ) { 
                        flush = true;
                    }
                }
            }
        }

        // Sort int array of ranks to 
        Arrays.sort(sortRanks);

        // Straight Flush (5 cards in a sequence all in the same suit) (0 1 2 3 4 5)
        int straightCount = 0;
        for (int i = 0; i < 4; i++ ) {
            if(sortRanks[i] == sortRanks[i+1]-1) {
                straightCount += 1;
            }
        }
        
        // Convert string array of ranks to a list and then a HashSet to count duplicate ranks using Collections.frequency()
        LList<String> asList = Arrays.asList(handRanks);
        Set<String> mySet = new HashSet<>(asList);
        int pairCount = 0;
        int trioCount = 0;

        for(String s: mySet) {
            if(Collections.frequency(asList,s) == 2) {
                pairCount += 1;
            }
            if(Collections.frequency(asList,s) == 3) {
                trioCount += 1;
            }
            if(Collections.frequency(asList,s) == 4) {
                fourOfAKind = true;
            }
        }
        
        // if else block to traverse ranks in the correct order from highest to lowest rank. Check rank conditions with each if.
        if (sortRanks[0] == 0 && sortRanks[1] == 10 && sortRanks[2] == 11 && sortRanks[3] == 12 && sortRanks[4] == 13 && flush == true) {
            System.out.println("Royal Flush");
        } else if (straightCount == 4 && flush == true ) {  // check for a Straight flush
            System.out.println("Straight Flush"); 
        } else if (fourOfAKind == true ) {                  // Check for a four of a kind
            System.out.println("Four of a Kind");
        } else if (pairCount == 1 && trioCount == 1) {      // Check for a full house (a pair and a trio)
            System.out.println("Full House"); 
        } else if (straightCount < 4 && flush == true) {    // Check for a flush
            System.out.println("Flush"); 
        } else if (straightCount == 4 && flush == false ) { // Check for a straight
            System.out.println("Straight"); 
        } else if (pairCount == 0 && trioCount == 1) {      // Check for three of a kind
            System.out.println("Three of a Kind"); 
        } else if (pairCount == 2) {                        // Check for two pairs
            System.out.println("Two Pair"); 
        } else if (pairCount == 1 && trioCount == 0) {      // Check for a single pair
            System.out.println("Pair"); 
        } else {                                            // If nothing else it is a High Card.
            System.out.println("High Card");
        }
    }
}
