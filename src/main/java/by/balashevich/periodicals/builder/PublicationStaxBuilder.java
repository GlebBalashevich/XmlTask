package by.balashevich.periodicals.builder;

import by.balashevich.periodicals.entity.Magazine;
import by.balashevich.periodicals.entity.Newspaper;
import by.balashevich.periodicals.entity.Publication;
import by.balashevich.periodicals.entity.Publisher;
import by.balashevich.periodicals.exception.PublicationProjectException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicationStaxBuilder extends AbstractBuilder {
    private static final String DASH = "\\p{Pd}";
    private static final String UNDERSCORES = "_";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private XMLInputFactory inputFactory;

    public PublicationStaxBuilder() {
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public void buildPublicationSet(String filename) throws PublicationProjectException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputStream);

            while (xmlReader.hasNext()) {
                if (xmlReader.next() == XMLStreamConstants.START_ELEMENT) {
                    if (xmlReader.getLocalName().equals(TagName.MAGAZINE.getTag())
                            || xmlReader.getLocalName().equals(TagName.NEWSPAPER.getTag())) {
                        Publication publication = buildPublication(xmlReader);
                        publicationSet.add(publication);
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Publication buildPublication(XMLStreamReader xmlReader) throws XMLStreamException {
        Publication publication = null;

        if (xmlReader.getLocalName().equals(TagName.MAGAZINE.getTag())) {
            publication = new Magazine();
        }
        if (xmlReader.getLocalName().equals(TagName.NEWSPAPER.getTag())) {
            publication = new Newspaper();
        }

        if (publication != null) {
            publication.setTitle(xmlReader.getAttributeValue(null, TagName.TITLE.getTag()));
            publication.setIssnCode(xmlReader.getAttributeValue(null, TagName.ISSN_CODE.getTag()));

            while (xmlReader.hasNext()) {
                int type = xmlReader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    String elementName = xmlReader.getLocalName().replaceAll(DASH, UNDERSCORES).toUpperCase();
                    switch (TagName.valueOf(elementName)) {
                        case PAGE:
                            publication.setPage(Integer.parseInt(xmlReader.getElementText()));
                            break;
                        case PERIODICITY:
                            publication.setPeriodicity(Publication.Periodicity.valueOf(xmlReader.getElementText().toUpperCase()));
                            break;
                        case COLORED:
                            ((Newspaper) publication).setColored(Boolean.parseBoolean(xmlReader.getElementText()));
                            break;
                        case PUBLISHER:
                            publication.setPublisher(buildPublisher(xmlReader));
                            break;
                        case PRINT_FORMAT:
                            String formatValue = xmlReader.getElementText().replaceAll(DASH, UNDERSCORES).toUpperCase();
                            Newspaper.PrintFormat format = Newspaper.PrintFormat.valueOf(formatValue);
                            ((Newspaper) publication).setPrintFormat(format);
                            break;
                        case GLOSSY:
                            ((Magazine) publication).setGlossy(Boolean.parseBoolean(xmlReader.getElementText()));
                            break;
                        case THEMATIC:
                            Magazine.Thematic thematic = Magazine.Thematic.valueOf(xmlReader.getElementText().toUpperCase());
                            ((Magazine) publication).setThematic(thematic);
                            break;
                    }
                }
                if (type == XMLStreamConstants.END_ELEMENT) {
                    if (xmlReader.getLocalName().equals(TagName.MAGAZINE.getTag())
                            || xmlReader.getLocalName().equals(TagName.NEWSPAPER.getTag())) {
                        break;
                    }
                }
            }
        }

        return publication;
    }

    private Publisher buildPublisher(XMLStreamReader xmlReader) throws XMLStreamException {
        Publisher publisher = new Publisher();
        while (xmlReader.hasNext()) {
            int type = xmlReader.next();
            if (type == XMLStreamConstants.START_ELEMENT) {
                String elementName = xmlReader.getLocalName().replaceAll(DASH, UNDERSCORES).toUpperCase();
                switch (TagName.valueOf(elementName)) {
                    case COUNTRY:
                        Publisher.Country country = Publisher.Country.valueOf(xmlReader.getElementText().toUpperCase());
                        publisher.setCountry(country);
                        break;
                    case LICENSE_EXPIRATION:
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
                            Date licenseExpiration = dateFormat.parse(xmlReader.getElementText());
                            publisher.setLicenseExpiration(licenseExpiration);
                        } catch (ParseException e) {
                            e.printStackTrace(); //todo add log
                        }
                        break;
                }
            }
            if (type == XMLStreamConstants.END_ELEMENT) {
                if (xmlReader.getLocalName().equals(TagName.PUBLISHER.getTag())) {
                    break;
                }
            }
        }

        return publisher;
    }
}
