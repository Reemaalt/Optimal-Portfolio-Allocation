package part2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


   


class DynamicProgrammingSolver {

    //main method
    public static void main(String[] args) throws FileNotFoundException {
        DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
        solver.solve("try1.txt");
    }
    // Asset class
    static class Asset {
        String id;
        double expectedReturn;
        double riskLevel;
        int quantity;

        Asset(String id, double expectedReturn, double riskLevel, int quantity) {
            this.id = id;
            this.expectedReturn = expectedReturn;
            this.riskLevel = riskLevel;
            this.quantity = quantity;
        }
    }

    // Dynamic programming algorithm to find the optimal asset allocation
    public Map<String, Integer> dynamicProgramming(List<Asset> assets, double totalInvestment, double riskToleranceLevel) {
        // Initialize the memoization table
        int[][] dp = new int[assets.size() + 1][(int) totalInvestment + 1];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        // Base case initialization
        for (int i = 0; i <= assets.size(); i++) {
            dp[i][0] = 0;
        }

        // Fill the memoization table
        for (int i = 1; i <= assets.size(); i++) {
            for (int j = 1; j <= totalInvestment; j++) {
                Asset asset = assets.get(i - 1);
                if (j >= asset.quantity && asset.riskLevel <= riskToleranceLevel) {
                    dp[i][j] = (int) Math.max(dp[i - 1][j], dp[i - 1][j - asset.quantity] + asset.expectedReturn);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // Reconstruct the optimal allocation
        Map<String, Integer> allocation = new HashMap<>();
        int remainingInvestment = (int) totalInvestment;
        for (int i = assets.size(); i > 0 && remainingInvestment > 0; i--) {
            Asset asset = assets.get(i - 1);
            if (dp[i][remainingInvestment] != dp[i - 1][remainingInvestment]) {
                allocation.put(asset.id, asset.quantity);
                remainingInvestment -= asset.quantity;
            }
        }

        return allocation;
    }

    // Output the optimal allocation, expected return, and risk level
    public void outputOptimalAllocation(Map<String, Integer> allocation, List<Asset> assets) {
        double totalExpectedReturn = 0;
        double totalRiskLevel = 0;
        System.out.println("Optimal Allocation:");
        for (Map.Entry<String, Integer> entry : allocation.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " units");
            for (Asset asset : assets) {
                if (asset.id.equals(entry.getKey())) {
                    totalExpectedReturn += asset.expectedReturn * entry.getValue();
                    totalRiskLevel += asset.riskLevel * entry.getValue();
                }
            }
        }
        System.out.println("Expected Portfolio Return: " + totalExpectedReturn);
        System.out.println("Portfolio Risk Level: " + totalRiskLevel);
    }

    // Main method to execute the asset allocation solver
    public void solve(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            List<Asset> assets = new ArrayList<>();
            double totalInvestment = 0;
            double riskToleranceLevel = 0;
            int assetCount = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains("Total investment")) {
                    totalInvestment = Double.parseDouble(line.split(" ")[3]);
                } else if (line.contains("Risk tolerance level")) {
                    riskToleranceLevel = Double.parseDouble(line.split(" ")[5]);
                } else if (line.matches("\\d+")) {
                    assetCount = Integer.parseInt(line);
                } else {
                    String[] parts = line.split(" : ");
                    assets.add(new Asset(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Integer.parseInt(parts[3])));
                }
            }
            sc.close();

            // Call the dynamic programming algorithm
            Map<String, Integer> optimalAllocation = dynamicProgramming(assets, totalInvestment, riskToleranceLevel);

            // Output the optimal allocation, expected return, and risk level
            outputOptimalAllocation(optimalAllocation, assets);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in the file.");
            e.printStackTrace();
        }
    }
}



/*
 * package part2;
import java.io.*;
import java.util.*;

public class DynamicProgramming {
   //asset claass
    static class Asset {
        String id;
        double expectedReturn;
        double riskLevel;
        int quantity;

        Asset(String id, double expectedReturn, double riskLevel, int quantity) {
            this.id = id;
            this.expectedReturn = expectedReturn;
            this.riskLevel = riskLevel;
            this.quantity = quantity;
        }
    }

// Dynamic programming algorithm to find the optimal asset allocation
    public static Map<String, Integer> dynamicProgramming(List<Asset> assets, double totalInvestment, double riskToleranceLevel) {
        // Initialize the memoization table
        double[][] dp = new double[assets.size() + 1][(int) totalInvestment + 1];
        for (double[] row : dp) {
            Arrays.fill(row, -1);
        }

        // Base case initialization
        for (int i = 0; i <= assets.size(); i++) {
            dp[i][0] = 0;
        }

        // Fill the memoization table
        for (int i = 1; i <= assets.size(); i++) {
            for (int j = 1; j <= totalInvestment; j++) {
                Asset asset = assets.get(i - 1);
                if (j >= asset.quantity && asset.riskLevel <= riskToleranceLevel) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - asset.quantity] + asset.expectedReturn);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // Reconstruct the optimal allocation
        Map<String, Integer> allocation = new HashMap<>();
        int remainingInvestment = (int) totalInvestment;
        for (int i = assets.size(); i > 0 && remainingInvestment > 0; i--) {
            Asset asset = assets.get(i - 1);
            if (dp[i][remainingInvestment] != dp[i - 1][remainingInvestment]) {
                allocation.put(asset.id, asset.quantity);
                remainingInvestment -= asset.quantity;
            }
        }

        return allocation;
    }

// Output the optimal allocation, expected return, and risk level
    public static void outputOptimalAllocation(Map<String, Integer> allocation, List<Asset> assets) {
        double totalExpectedReturn = 0;
        double totalRiskLevel = 0;
        System.out.println("Optimal Allocation:");
        for (Map.Entry<String, Integer> entry : allocation.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " units");
            for (Asset asset : assets) {
                if (asset.id.equals(entry.getKey())) {
                    totalExpectedReturn += asset.expectedReturn * entry.getValue();
                    totalRiskLevel += asset.riskLevel * entry.getValue();
                }
            }
        }
        System.out.println("Expected Portfolio Return: " + totalExpectedReturn);
        System.out.println("Portfolio Risk Level: " + totalRiskLevel);
    }

    public static void main(String[] args) throws FileNotFoundException {
        
        // Read the file
        Scanner sc = new Scanner(new File("try1.txt"));
        List<Asset> assets = new ArrayList<>();
        double totalInvestment = 0;
        double riskToleranceLevel = 0;
        int assetCount = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.contains("Total investment")) {
                totalInvestment = Double.parseDouble(line.split(" ")[3]);
            } else if (line.contains("Risk tolerance level")) {
                riskToleranceLevel = Double.parseDouble(line.split(" ")[5]);
            } else if (line.matches("\\d+")) {
                assetCount = Integer.parseInt(line);
            } else {
                String[] parts = line.split(" : ");
                assets.add(new Asset(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Integer.parseInt(parts[3])));
            }
        }
        sc.close();

        // Call the dynamic programming algorithm
        Map<String, Integer> optimalAllocation = dynamicProgramming(assets, totalInvestment, riskToleranceLevel);

        // Output the optimal allocation, expected return, and risk level
        outputOptimalAllocation(optimalAllocation, assets);
    }

}
*/