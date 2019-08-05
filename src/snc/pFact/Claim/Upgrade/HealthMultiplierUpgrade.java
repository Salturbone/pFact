package snc.pFact.Claim.Upgrade;

/**
 * MultiplierUpgrade
 */
public class HealthMultiplierUpgrade extends ClaimUpgrade {

    public double multiplier;

    @Override
    public String getName() {
        return "healthMultiplierUpgrade";
    }

    public double getMultiplier() {
        return multiplier;
    }

}