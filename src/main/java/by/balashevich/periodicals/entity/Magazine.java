package by.balashevich.periodicals.entity;

public class Magazine extends Publication {
    public enum Thematic {
        SOCIAL,
        SCIENCE,
        RELIGIOUS,
        CHILDISH
    }

    private boolean glossy;
    private Thematic thematic;

    public boolean isGlossy() {
        return glossy;
    }

    public void setGlossy(boolean glossy) {
        this.glossy = glossy;
    }

    public Thematic getThematic() {
        return thematic;
    }

    public void setThematic(Thematic thematic) {
        this.thematic = thematic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Magazine magazine = (Magazine) o;

        return super.equals(magazine)
                && glossy == magazine.glossy
                && thematic == magazine.thematic;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * result + super.hashCode();
        result += 37 * result + (glossy ? 1 : 0);
        result += 37 * result + thematic.ordinal();

        return result;
    }

    @Override
    public String toString() {
        return String.format("Magazine: %s, glossy: %s, thematic: %s",
                super.toString(), glossy, thematic.name());
    }
}
