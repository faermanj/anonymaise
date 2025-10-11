package ay;

public enum Ranking {
    HIGHEST(0.99F),
    LOWEST(0.01F),
    IGNORED(0.0F);

    private final float value;

    Ranking(float value) {
        this.value = value;
    }
    public float getValue() {
        return value;
    }
}