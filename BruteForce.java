
import java.util.List;

public class BruteForce  {
 
/*Loop through all possible asset allocations using nested loops.
 a. Calculate the total return and total risk for the current allocation.
b. If the risk is within the specified tolerance and return is greater than maxReturn:
i. Update maxReturn, minRisk, and optimalAllocation. */

public void assetAlocater (String ID , double MAXreturn ,double minRisk ,List<asset> optimalInvsment ){

}
 
public void runprogram()
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
        BruteForce BruteForce = new BruteForce();
        BruteForce.runprogram();
        System.out.println("done!");
    }


}