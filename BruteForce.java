import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BruteForce  {

double MAXreturn;
double MAXRisk;
double totalinvestment;
List<asset> optimalInvsment;
static List<asset> Allassets;

/*Loop through all possible asset allocations using nested loops.
 a. Calculate the total return and total risk for the current allocation.
b. If the risk is within the specified tolerance and return is greater than maxReturn:
i. Update maxReturn, minRisk, and optimalAllocation. 
    public void assetAlocater(int currentID,  double currentReturn, double currentRisk, List<asset> currentAllocation) {
    // Base case: all assets allocated
    if (currentID == Allassets.size()) {
        // Check if the current allocation is within the risk and investment constraints
        if (currentRisk <= MAXRisk && currentReturn > MAXreturn) {
            // Update maxReturn, maxRisk, and optimalAllocation
            MAXreturn = currentReturn;
            MAXRisk = currentRisk;
            optimalInvsment = new ArrayList<>(currentAllocation);
        }
        return;
    }

    // Get the current asset
    asset currentAsset = Allassets.get(currentID);

    // Try different quantities of the current asset in the portfolio
    for (int i = 0; i <= currentAsset.quantity; i++) {
        currentAsset.quantity = i;  // Update the quantity of the current asset in the allocation
        currentAllocation.add(currentAsset);  // Add the current asset to the allocation

        // Calculate the new risk and return after adding the current asset
        double newRisk = currentRisk + currentAsset.riskTolerance * i;
        double newReturn = currentReturn + currentAsset.expectedReturn * i;

        // Recursively call the assetAlocater method for the next asset
        assetAlocater(currentID + 1, newRisk, newReturn, currentAllocation);

        // Remove the current asset from the allocation for backtracking
        currentAllocation.remove(currentAllocation.size() - 1);
    }
}*/

public void assetAlocater(int currentID, double currentReturn, double currentRisk, List<asset> currentAllocation) {
        // Base case: all assets allocated
    if (currentID == Allassets.size()) {
        if (currentRisk <= MAXRisk && currentReturn > MAXreturn) {
            MAXreturn = currentReturn;
            MAXRisk = currentRisk;
            optimalInvsment = new ArrayList<>(currentAllocation);
        }
        return;
    }

    asset currentAsset = Allassets.get(currentID);

    for (int i = 0; i <= currentAsset.quantity; i++) {
        asset copyOfCurrentAsset = new asset(currentAsset.ID, currentAsset.expectedReturn, currentAsset.riskTolerance, currentAsset.quantity);
        copyOfCurrentAsset.quantity = i;
        currentAllocation.add(copyOfCurrentAsset);

        double newRisk = currentRisk + copyOfCurrentAsset.riskTolerance * i;
        double newReturn = currentReturn + copyOfCurrentAsset.expectedReturn * i;

        assetAlocater(currentID + 1, newReturn, newRisk, currentAllocation);

        currentAllocation.remove(currentAllocation.size() - 1);
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

    static void readInput(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Allassets = new ArrayList<>(); //new node
            String line;
            // Read each line from the file and parse asset information
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                // Check if there are enough parts
                if (parts.length < 4) {
                    System.err.println("Invalid format in line: " + line);
                    continue;  // Skip this line and continue with the next line
                }
    
                String id = parts[0].trim();
                double expectedReturn;
                double riskLevel;
                int quantity;
                try {
                    expectedReturn = Double.parseDouble(parts[1].trim());
                    riskLevel = Double.parseDouble(parts[2].trim());
                    quantity = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numeric values in line: " + line);
                    continue;  // Skip this line and continue with the next line
                }
                Allassets.add(new asset(id, expectedReturn, riskLevel, quantity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    









    public void runprogram(){
// Read input from the text file نورة 
        readInput("Example.txt");
// Call the brute force method to find optimal allocation ريما
        assetAlocater(0, 0, 0, Allassets );
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