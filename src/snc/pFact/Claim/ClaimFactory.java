package snc.pFact.Claim;

public class ClaimFactory {
	
    //use getShape method to get object of type shape 
    public static Claim getClaim(String a) {
        if(a == null){
            return null;
         }	
        if (a.equalsIgnoreCase("main")) {
            return new FMC();
        }
        if (a.equalsIgnoreCase("add")) {
            return new FAC();
        }
        return null;
    }
 }