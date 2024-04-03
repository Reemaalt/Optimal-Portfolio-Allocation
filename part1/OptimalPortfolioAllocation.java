import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Asset {
    String id;
    double expectedReturn;
    double riskLevel;
    int quantity;

    public Asset(String id, double expectedReturn, double riskLevel, int quantity) {
        this.id = id;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
        this.quantity = quantity;
    }
}

public class OptimalPortfolioAllocation {
    static List<Asset> assets;
    static int totalInvestment;
    static double riskToleranceLevel;
    static double maxReturn;
    static double maxRisk;
    static List<Asset> optimalAllocation;

    public static void main(String[] args) {
        optimalAllocation = new ArrayList<>();
        readInput("Example.txt");
        maxRisk = riskToleranceLevel;

        bruteForce(0, 0, 0, new ArrayList<>());
        writeOutput("Output_OptimalPortfolio.txt");
    }

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

    static void writeOutput(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("Optimal Allocation:\n");
            for (Asset asset : optimalAllocation) {
                bw.write(asset.id + asset.expectedReturn+": Expected-return\n" +asset.riskLevel +" : Risk-level\n"  + asset.quantity + " units\n");
            }
            bw.write("Expected Portfolio Return: " + maxReturn + "\n");
            bw.write("Portfolio Risk Level: " + maxRisk + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
