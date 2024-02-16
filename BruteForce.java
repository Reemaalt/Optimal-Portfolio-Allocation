public class BruteForce  {
 
 
 
public static void runprogram()
    {
// Read input from the text file نورة 
        readInput("Example.txt");

        // Call the brute force method to find optimal allocation ريما
        bruteForce(0, 0, new ArrayList<>());

        // Write optimal allocation and results to output file نوف
        writeOutput("Output_BruteForce.txt");

    }

// Main method to run the program
    public static void main(String[] args) {
        runprogram();
        System.out.println("done!");
    }


}