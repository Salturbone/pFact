package snc.pFact.Claim.Upgrade;

/**
 * GainMultiplierUpgrade
 */
public class GainMultiplierUpgrade extends ClaimUpgrade {

    private double multiplier;

    public GainMultiplierUpgrade(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * @param multiplier the multiplier to set
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public String getName() {
        return "gainMultiplierUpgrade";
    }

    public double getMultiplier() {
        return multiplier;
    }

}