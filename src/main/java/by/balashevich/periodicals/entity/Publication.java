package by.balashevich.periodicals.entity;

public abstract class Publication {
    public enum Periodicity {
        DAILY,
        WEEKLY,
        MONTHLY,
    }

    private String title;
    private String issnCode;
    private int page;
    private Periodicity periodicity;
    private Publisher publisher;

    public Publication(){
        this.publisher = new Publisher();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssnCode() {
        return issnCode;
    }

    public void setIssnCode(String issnCode) {
        this.issnCode = issnCode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Publication publication = (Publication) o;

        return title.equals(publication.title)
                && issnCode.equals(publication.issnCode)
                && page == publication.page
                && periodicity == publication.periodicity
                && publisher.equals(publication.publisher);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * result + title.hashCode();
        result += 37 * result + (issnCode != null ? issnCode.hashCode() : 0);
        result += 37 * result + page;
        result += 37 * result + periodicity.ordinal();
        result += 37 * result + publisher.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return String.format("title: %s, issnCode: %s, page: %d, periodicity: %s, publisher: %s",
                title, issnCode, page, periodicity.name(), publisher);
    }
}
