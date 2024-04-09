import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
    static double totalInvestment;
    static double riskToleranceLevel;
    static double maxReturn;
    static double maxRisk;
    static List<Asset> optimalAllocation;

    public static void main(String[] args) {
      
     
       OptimalPortfolioAllocation SOLVE  = new OptimalPortfolioAllocation();
       SOLVE.run();
       
    }

    public void run() {
    assets = readInput("Example2.txt");
   
    optimalAllocation = new ArrayList<>();
    maxRisk = riskToleranceLevel; // Update maxRisk here
    bruteForce(0, 0, 0, new ArrayList<>());
    }

  
    public  List<Asset> readInput(String fileName) {
    
        List<Asset> assets = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 4) {
                String id = parts[0].trim();
                try {
                    double expectedReturn = Double.parseDouble(parts[1].trim());
                    double riskLevel = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());
                    assets.add(new Asset(id, expectedReturn, riskLevel, quantity));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing values for line: " + line);
                }
            } else {
                String[] tokens = line.split("\\s+");
                if (tokens.length >= 4 && tokens[0].equalsIgnoreCase("Total") && tokens[1].equalsIgnoreCase("investment")) {
                    totalInvestment = Double.parseDouble(tokens[tokens.length - 2]);
                } else if (tokens.length >= 5 && tokens[0].equalsIgnoreCase("Risk") && tokens[1].equalsIgnoreCase("tolerance") && tokens[2].equalsIgnoreCase("level")) {
                    riskToleranceLevel = Double.parseDouble(tokens[tokens.length - 1]);
                }
            }

        } // end while
        
    }  //end try
         catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in the file.");
        } catch (IllegalArgumentException e) {
            System.err.println("Input file format error: " + e.getMessage());
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Print to verify values were read correctly
    System.out.println("Total Investment: " + totalInvestment);
    System.out.println("Risk Tolerance Level: " + riskToleranceLevel);

    return assets;
}


    public void bruteForce(int currentIndex, double currentInvestment, double currentRisk, List<Asset> currentAllocation) {
    if (currentIndex == assets.size()) {
        if (currentRisk <= riskToleranceLevel && currentInvestment <= totalInvestment) {
            double currentReturn = currentAllocation.stream().mapToDouble(a -> a.expectedReturn * a.quantity).sum();
            if (currentReturn > maxReturn) {
                maxReturn = currentReturn;
                maxRisk = currentRisk; // Update maxRisk when a feasible allocation is found
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
    writeOutput("Output_OptimalPortfolio.txt");

}


    public void writeOutput(String fileName) {
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
