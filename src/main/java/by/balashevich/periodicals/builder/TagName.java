package by.balashevich.periodicals.builder;

public enum TagName {
    PUBLICATIONS("publications"),
    MAGAZINE ("magazine"),
    NEWSPAPER ("newspaper"),
    PUBLISHER("publisher"),
    TITLE("title"),
    ISSN_CODE("issn-code"),
    PAGE("page"),
    PERIODICITY("periodicity"),
    COUNTRY("country"),
    LICENSE_EXPIRATION("license-expiration"),
    COLORED("colored"),
    PRINT_FORMAT("print-format"),
    GLOSSY("glossy"),
    THEMATIC("thematic");

    private String tag;

    TagName(String tag){
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }
}
