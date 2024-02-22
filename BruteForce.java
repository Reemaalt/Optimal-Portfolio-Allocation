
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// what each assets has

class asset{

    String ID;
    double expectedReturn;
    double riskTolerance;
    int quantity;


public asset( String ID, double expectedReturn, double riskTolerance, int quantity ){
    this.ID = ID;
    this.expectedReturn = expectedReturn;
    this.riskTolerance = riskTolerance;
    this.quantity = quantity;

}

}


public class BruteForce  {

double MAXreturn;
double MAXRisk;
double totalinvestment;
List<asset> optimalInvsment;
List<asset> Allassets;

/*Loop through all possible asset allocations using nested loops.
 a. Calculate the total return and total risk for the current allocation.
b. If the risk is within the specified tolerance and return is greater than maxReturn:
i. Update maxReturn, minRisk, and optimalAllocation. */

public void assetAlocater ( int currnetID , double currentRisk, double currentReturn , List<asset> currentAllocation ){

//bass cass : all assets alocated
if (currnetID == Allassets.size()) {

    // Check if the current allocation is within the risk and investment constraints
    if (currentRisk <= MAXRisk && currentRisk <= totalinvestment) {
        // Calculate the current expected return of the allocation
         currentReturn = currentAllocation//expectedReturn * quantity sum();
        // If the current allocation has a higher expected return, update the optimal allocation
        if (currentReturn > MAXreturn) {
            MAXreturn = currentReturn;
            optimalInvsment =  (currentAllocation);
             }
           }
           return;
         }
          // Get the current asset
        asset currentAsset = Allassets.get(currentID);

        // Try different quantities of the current asset in the portfolio
        for (int i = 0; i <= currentAsset.quantity; i++) {
            currentAsset.quantity = i;  // Update the quantity of the current asset in the allocation
            currentAllocation.add(currentAsset);  // Add the current asset to the allocation

            // Recursively call the bruteForce method for the next asset
        }
    }

    public void writeOutput(String FileName){

        try (PrintWriter pw = new PrintWriter(new FileWriter(FileName))) {
        pw.write("Optimal Allocation:\n");
        // Write each asset and its quantity in the optimal allocation
        for (asset asset : optimalInvsment) {
            pw.write(asset.ID + ": " + asset.quantity + " units\n");
        }
        pw.write("Expected Portfolio Return: " + MAXreturn + "\n");
        pw.write("Portfolio Risk Level: " + MAXRisk + "\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
        
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