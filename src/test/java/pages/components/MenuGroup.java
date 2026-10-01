package pages.components;

public enum MenuGroup {
    ELEMENTS("//div[text()='Elements']"),
    FORMS("//div[text()='Forms']"),
    FRAMES("//div[text()='Alerts, Frame & Windows']"),
    WIDGETS("//div[text()='Widgets']"),
    INTERRACTIONS("//input[text()='Interactions']"),
    BOOK_STORE("//div[text()='Book Store Application']");

    public final String xpath;
    MenuGroup(String xpath) {this.xpath = xpath;}
}
