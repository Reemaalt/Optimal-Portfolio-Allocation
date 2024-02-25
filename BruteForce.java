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
public class BruteForce {
    static List<Asset> assets;        // List to store all the assets
    static int totalInvestment;       // Total investment amount
    static double riskToleranceLevel;// Risk tolerance level
    static double maxReturn;          // Maximum expected return
    static double maxRisk;            // Maximum allowed risk in the portfolio
    static List<Asset> optimalAllocation;  // Optimal allocation of assets

    // Main method to run the program
    public static void main(String[] args) {
        optimalAllocation = new ArrayList<>();

        readInput("Example.txt");
        bruteForce(0, 0, 0, new ArrayList<>());
        writeOutput("Output_BruteForce.txt");
    }

    // Method to read input from the text file
    static void readInput(String fileName) {
        assets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    String id = parts[0].trim();
                    double expectedReturn = Double.parseDouble(parts[1].trim());
                    double riskLevel = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());
                    assets.add(new Asset(id, expectedReturn, riskLevel, quantity));
                } else {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length >= 4 && tokens[0].equalsIgnoreCase("Total") && tokens[1].equalsIgnoreCase("investment")) {
                        totalInvestment = Integer.parseInt(tokens[tokens.length - 2]);
                    } else if (tokens.length >= 5 && tokens[0].equalsIgnoreCase("Risk") && tokens[1].equalsIgnoreCase("tolerance") && tokens[2].equalsIgnoreCase("level")) {
                        riskToleranceLevel = Double.parseDouble(tokens[tokens.length - 1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Brute force algorithm to explore all possible allocations
    static void bruteForce(int currentIndex, double currentInvestment, double currentRisk, List<Asset> currentAllocation) {
        if (currentIndex == assets.size()) {
            if (currentRisk <= maxRisk && currentInvestment <= totalInvestment) {
                double currentReturn = currentAllocation.stream().mapToDouble(a -> a.expectedReturn * a.quantity).sum();
                if (currentReturn > maxReturn) {
                    maxReturn = currentReturn;
                    optimalAllocation = new ArrayList<>(currentAllocation);
                }
            }
            return;
        }

        Asset currentAsset = assets.get(currentIndex);

        for (int i = 0; i <= currentAsset.quantity; i++) {
            Asset newAsset = new Asset(currentAsset.id, currentAsset.expectedReturn, currentAsset.riskLevel, i);
            currentAllocation.add(newAsset);
            bruteForce(currentIndex + 1, currentInvestment + i, currentRisk + currentAsset.riskLevel * i, currentAllocation);
            currentAllocation.remove(currentAllocation.size() - 1);
        }
    }

    // Method to write output to a file
    static void writeOutput(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("Optimal Allocation:\n");
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
