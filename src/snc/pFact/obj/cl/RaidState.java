package snc.pFact.obj.cl;

/**
 * RaidState
 */
public enum RaidState {
    NO_RAID(false), ATTACK_HELPING(false), DEFENG_HELPING(false), DEFENDING(true), ATTACKING(true);

    public boolean canBreak;

    RaidState(boolean canbreak) {
        this.canBreak = canbreak;
    }

}