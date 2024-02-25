public class asset {
    
// what each assets has



String ID;
double expectedReturn;
double riskTolerance;
int quantity;


public void setID(String iD) {
    ID = iD;
}


public void setExpectedReturn(double expectedReturn) {
    this.expectedReturn = expectedReturn;
}


public void setRiskTolerance(double riskTolerance) {
    this.riskTolerance = riskTolerance;
}


public void setQuantity(int quantity) {
    this.quantity = quantity;
}


public String getID() {
    return ID;
}


public double getExpectedReturn() {
    return expectedReturn;
}


public double getRiskTolerance() {
    return riskTolerance;
}


public int getQuantity() {
    return quantity;
}


public asset( String ID, double expectedReturn, double riskTolerance, int quantity ){
this.ID = ID;
this.expectedReturn = expectedReturn;
this.riskTolerance = riskTolerance;
this.quantity = quantity;

}




}
