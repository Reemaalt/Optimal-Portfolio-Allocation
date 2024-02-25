import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BruteForce {

    double MAXreturn = Double.MIN_VALUE;
    double MAXRisk = Double.MAX_VALUE;
    double totalinvestment;
    List<asset> optimalInvsment = new ArrayList<>();
   static List<asset> Allassets;

   public void assetAlocater(int currentID, double currentReturn, double currentRisk, List<asset> currentAllocation) {
    if (currentID == Allassets.size()) {
        if (currentRisk <= MAXRisk && currentReturn > MAXreturn) {
            MAXreturn = currentReturn;
            MAXRisk = currentRisk;
            optimalInvsment = new ArrayList<>(currentAllocation); // Deep copy the current allocation
        }
        return;
    }

    asset currentAsset = Allassets.get(currentID);

    // Fixed quantity of each asset
    int quantity = currentAsset.quantity;

    asset copyOfCurrentAsset = new asset(currentAsset.ID, currentAsset.expectedReturn, currentAsset.riskTolerance, quantity);
    currentAllocation.add(copyOfCurrentAsset);

    // Update the total risk and return for the portfolio
    double newRisk = currentRisk + copyOfCurrentAsset.riskTolerance * quantity;
    double newReturn = currentReturn + copyOfCurrentAsset.expectedReturn * quantity;

    assetAlocater(currentID + 1, newReturn, newRisk, currentAllocation);

    currentAllocation.remove(currentAllocation.size() - 1);
}

    public void writeOutput(String fileName) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
        bw.write("Optimal Allocation:\n");
        for (asset asset : optimalInvsment) {
            bw.write(asset.ID + ": " + asset.quantity + " units\n");
        }
        bw.write("Expected Portfolio Return: " + MAXreturn + "\n");
        bw.write("Portfolio Risk Level: " + MAXRisk + "\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    static void readInput(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Allassets = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 5 ) {
                    System.err.println("Invalid format in line: " + line);
                    continue;
                }
                String id = parts[0].trim();
                double expectedReturn = Double.parseDouble(parts[1].trim());
                double riskLevel = Double.parseDouble(parts[2].trim());
                int quantity = Integer.parseInt(parts[3].trim());
                Allassets.add(new asset(id, expectedReturn, riskLevel, quantity));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void runProgram() {
        readInput("Example.txt");
        assetAlocater(0, 0, 0, new ArrayList<>());
        writeOutput("Output_BruteForce.txt");
    }

    public static void main(String[] args) {
         BruteForce BruteForce = new BruteForce();
        BruteForce.runProgram();
        System.out.println("done!");
    }
}