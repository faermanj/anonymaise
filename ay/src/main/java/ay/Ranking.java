package ay;

public enum Ranking {
    HIGHEST(0.99999F),
    HIGH(0.999F),
    LOW(0.05F),
    LOWEST(0.001F),
    IGNORED(-1F);

    private final float value;

    Ranking(float value) {
        this.value = value;
    }
    public float getValue() {
        return value;
    }
}