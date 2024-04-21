
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DynamicProgrammingSolver {
    
   public double totalInvestment ;
   public double riskToleranceLevel;


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
    

    public static void main(String[] args) {

        DynamicProgrammingSolver solver= new  DynamicProgrammingSolver();  
        solver.run();
       
    }

    public void  run (){
                // Read input from file
                // some devices need the path of the file !!
                List<Asset> assets = readInputFromFile("C:\\Users\\altwu\\OneDrive\\Desktop\\Optimal-Portfolio-Allocation\\part2\\Example3.txt");
                // Solve optimal allocation problem
                List<Asset> optimalAllocation = findOptimalAllocation(assets, totalInvestment, riskToleranceLevel);
                // Write output to file
                writeOutputToFile(optimalAllocation, "output.txt");
            }
    

    // the dp method :)
    public List<Asset> findOptimalAllocation(List<Asset> assets, double totalInvestment, double riskTolerance) {
      
        int numberOfassets = assets.size();
        double[][] dp = new double[numberOfassets + 1][(int) totalInvestment + 1];
        int[][] quantity = new int[numberOfassets + 1][(int) totalInvestment + 1];
    
        for (int i = 1; i <= numberOfassets; i++) {
      
            for (int j = 0; j <= totalInvestment; j++) {
                dp[i][j] = dp[i - 1][j];
                quantity[i][j] = 0;
    
                for (int k = 0; k <= j; k++) {
                    double potentialReturn = dp[i - 1][j - k] + k * assets.get(i - 1).expectedReturn;
                    double potentialRisk = Math.sqrt(dp[i - 1][j - k] * dp[i - 1][j - k] + k * k * assets.get(i - 1).riskLevel * assets.get(i - 1).riskLevel);
    
                    if (potentialRisk <= riskTolerance && potentialReturn > dp[i][j]) {
                        dp[i][j] = potentialReturn;
                        quantity[i][j] = k;
                    }
                }
            }
        }
    
        List<Asset> optimalAllocation = new ArrayList<>();
        int remainingInvestment = (int) totalInvestment;
       
        //backtrcking
        for (int i = numberOfassets; i > 0; i--) {
            int q = quantity[i][remainingInvestment];
            Asset asset = assets.get(i - 1);
            optimalAllocation.add(new Asset(asset.id, asset.expectedReturn, asset.riskLevel, q));
            remainingInvestment -= q;
        }
    
        return optimalAllocation;
    }


//the  method reading the file
    public  List<Asset> readInputFromFile(String fileName) {
    
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

//the method writing the new output file
    public void writeOutputToFile(List<Asset> allocation, String fileName) {
       
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Optimal Allocation:\n");
            for (Asset asset : allocation) {
                writer.write(asset.id + ": " + "Expected-return"+ asset.expectedReturn + ":"+ asset.quantity + " units\n");
            }
            writer.write("Expected Portfolio Return: " + calculateExpectedReturn(allocation) + "\n");
            writer.write("Portfolio Risk Level: " + calculatePortfolioRisk(allocation));
            writer.flush(); // Ensure data is written
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//helper methods
    public  double calculateExpectedReturn(List<Asset> allocation) {
        double expectedReturn = 0;
        for (Asset asset : allocation) {
            expectedReturn += asset.expectedReturn * asset.quantity;
        }
        return expectedReturn;
    }

    public static double calculatePortfolioRisk(List<Asset> allocation) {
        double portfolioRisk = 0;
        for (Asset asset : allocation) {
            portfolioRisk += Math.pow(asset.riskLevel * asset.quantity, 2);
        }
        return Math.sqrt(portfolioRisk);
    }
}

