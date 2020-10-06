package by.balashevich.periodicals.entity;

public class Newspaper extends Publication {
    public enum PrintFormat {
        A2,
        A3,
        A4
    }

    private boolean colored;
    private PrintFormat printFormat;

    public boolean isColored() {
        return colored;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    public PrintFormat getPrintFormat() {
        return printFormat;
    }

    public void setPrintFormat(PrintFormat printFormat) {
        this.printFormat = printFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Newspaper newsPaper = (Newspaper) o;

        return super.equals(newsPaper)
                && colored == newsPaper.colored
                && printFormat == newsPaper.printFormat;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * result + super.hashCode();
        result += 37 * result + (colored ? 1 : 0);
        result += 37 * result + printFormat.ordinal();

        return result;
    }

    @Override
    public String toString() {
        return String.format("Newspaper: %s, colored: %s, printFormat: %s",
                super.toString(), colored, printFormat.name());
    }
}
