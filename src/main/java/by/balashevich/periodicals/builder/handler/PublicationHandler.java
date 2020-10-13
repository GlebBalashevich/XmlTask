package by.balashevich.periodicals.builder.handler;

import by.balashevich.periodicals.builder.TagName;
import by.balashevich.periodicals.entity.Magazine;
import by.balashevich.periodicals.entity.Newspaper;
import by.balashevich.periodicals.entity.Publication;
import by.balashevich.periodicals.entity.Publisher;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class PublicationHandler extends DefaultHandler {
    private static final Logger logger = LogManager.getLogger();
    private static final String DASH = "\\p{Pd}";
    private static final String UNDERSCORES = "_";
    private static final String EMPTY_VALUE = "";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private Set<Publication> publicationSet;
    private EnumSet<TagName> valuesTags;
    private Publication publication;
    private TagName currentTag;

    public PublicationHandler() {
        publicationSet = new HashSet<>();
        valuesTags = EnumSet.range(TagName.PAGE, TagName.THEMATIC);
    }

    public Set<Publication> getPublicationSet() {
        return publicationSet;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals(TagName.NEWSPAPER.getTag()) || localName.equals(TagName.MAGAZINE.getTag())) {
            if (localName.equals(TagName.NEWSPAPER.getTag())) {
                publication = new Newspaper();
            }
            if (localName.equals(TagName.MAGAZINE.getTag())) {
                publication = new Magazine();
            }
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getLocalName(i).equals(TagName.TITLE.getTag())) {
                    publication.setTitle(attributes.getValue(i));
                }
                if (attributes.getLocalName(i).equals(TagName.ISSN_CODE.getTag())) {
                    if (attributes.getValue(i) != null) {
                        publication.setIssnCode(attributes.getValue(i));
                    } else {
                        publication.setIssnCode(EMPTY_VALUE);
                    }
                }
            }
        } else {
            TagName tag = TagName.valueOf(localName.toUpperCase().replaceAll(DASH, UNDERSCORES));
            if (valuesTags.contains(tag)) {
                currentTag = tag;
            }
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals(TagName.NEWSPAPER.getTag()) || localName.equals(TagName.MAGAZINE.getTag())) {
            publicationSet.add(publication);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String elementValue = new String(ch, start, length).trim();
        if (currentTag != null) {
            switch (currentTag) {
                case PAGE:
                    publication.setPage(Integer.parseInt(elementValue));
                    break;
                case PERIODICITY:
                    publication.setPeriodicity(Publication.Periodicity.valueOf(elementValue.toUpperCase()));
                    break;
                case COUNTRY:
                    Publisher.Country country = Publisher.Country.valueOf(elementValue.toUpperCase());
                    publication.getPublisher().setCountry(country);
                    break;
                case LICENSE_EXPIRATION:
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
                        Date licenseExpiration = dateFormat.parse(elementValue);
                        publication.getPublisher().setLicenseExpiration(licenseExpiration);
                    } catch (ParseException e) {
                        logger.log(Level.WARN, "incorrect data value for License expiration, set current date", e);
                        publication.getPublisher().setLicenseExpiration(new Date());
                    }
                    break;
                case COLORED:
                    ((Newspaper) publication).setColored(Boolean.parseBoolean(elementValue));
                    break;
                case PRINT_FORMAT:
                    String formatValue = elementValue.replaceAll(DASH, UNDERSCORES).toUpperCase();
                    Newspaper.PrintFormat format = Newspaper.PrintFormat.valueOf(formatValue);
                    ((Newspaper) publication).setPrintFormat(format);
                    break;
                case GLOSSY:
                    ((Magazine) publication).setGlossy(Boolean.parseBoolean(elementValue));
                    break;
                case THEMATIC:
                    Magazine.Thematic thematic = Magazine.Thematic.valueOf(elementValue.toUpperCase());
                    ((Magazine) publication).setThematic(thematic);
                    break;
            }
        }
        currentTag = null;
    }
}
