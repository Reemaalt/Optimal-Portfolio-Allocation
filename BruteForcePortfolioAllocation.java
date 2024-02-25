import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Class to represent each asset in the portfolio
class Asset {
    String id;
    double expectedReturn;
    double riskLevel;
    int quantity;

    // Constructor to initialize an Asset
    public Asset(String id, double expectedReturn, double riskLevel, int quantity) {
        this.id = id;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
        this.quantity = quantity;
    }
}

// Main class for Brute Force Portfolio Allocation
public class BruteForcePortfolioAllocation {
    static List<Asset> assets;        // List to store all the assets
    static double maxRisk;            // Maximum allowed risk in the portfolio
    static double maxInvestment;      // Maximum allowed investment amount
    static double maxReturn;          // Maximum expected return
    static List<Asset> optimalAllocation;  // Optimal allocation of assets

    // Main method to run the program
    public static void main(String[] args) {
        // Read input from the text file
        readInput("Example.txt");

        // Call the brute force method to find optimal allocation
        bruteForce(0, 0, new ArrayList<>());

        // Write optimal allocation and results to output file
        writeOutput("Output_BruteForce.txt");
    }

    // Brute force algorithm to explore all possible allocations
    static void bruteForce(int currentIndex, double currentRisk, List<Asset> currentAllocation) {
        // Base case: If all assets are considered
        if (currentIndex == assets.size()) {
            // Check if the current allocation is within the risk and investment constraints
            if (currentRisk <= maxRisk && currentRisk <= maxInvestment) {
                // Calculate the current expected return of the allocation
                double currentReturn = currentAllocation.stream().mapToDouble(a -> a.expectedReturn * a.quantity).sum();
                // If the current allocation has a higher expected return, update the optimal allocation
                if (currentReturn > maxReturn) {
                    maxReturn = currentReturn;
                    optimalAllocation = new ArrayList<>(currentAllocation);
                }
            }
            return;
        }

        // Get the current asset
        Asset currentAsset = assets.get(currentIndex);

        // Try different quantities of the current asset in the portfolio
        for (int i = 0; i <= currentAsset.quantity; i++) {
            currentAsset.quantity = i;  // Update the quantity of the current asset in the allocation
            currentAllocation.add(currentAsset);  // Add the current asset to the allocation

            // Recursively call the bruteForce method for the next asset
            bruteForce(currentIndex + 1, currentRisk + currentAsset.riskLevel * i, currentAllocation);

            // Backtrack: Remove the last added asset to explore other possibilities
            currentAllocation.remove(currentAllocation.size() - 1);
        }
    }

    // Method to read input from the text file
    static void readInput(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            assets = new ArrayList<>();
            String line;
            // Read each line from the file and parse asset information
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 4) {
                    System.err.println("Invalid format in line: " + line);
                    continue;  // Skip this line and continue with the next line
                }
                String id = parts[0].trim();
                double expectedReturn = Double.parseDouble(parts[1].trim());
                double riskLevel = Double.parseDouble(parts[2].trim());
                int quantity = Integer.parseInt(parts[3].trim());
                assets.add(new Asset(id, expectedReturn, riskLevel, quantity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to write output to a file
    static void writeOutput(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("Optimal Allocation:\n");
            // Write each asset and its quantity in the optimal allocation
            for (Asset asset : optimalAllocation) {
                bw.write(asset.id + ": " + asset.quantity + " units\n");
            }
            bw.write("Expected Portfolio Return: " + maxReturn + "\n");
            bw.write("Portfolio Risk Level: " + maxRisk + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}