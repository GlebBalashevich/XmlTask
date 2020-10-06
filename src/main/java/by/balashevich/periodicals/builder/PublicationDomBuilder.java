package by.balashevich.periodicals.builder;

import by.balashevich.periodicals.entity.Magazine;
import by.balashevich.periodicals.entity.Newspaper;
import by.balashevich.periodicals.entity.Publication;
import by.balashevich.periodicals.entity.Publisher;
import by.balashevich.periodicals.exception.PublicationProjectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static by.balashevich.periodicals.builder.TagName.*;

public class PublicationDomBuilder extends AbstractBuilder {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private DocumentBuilder docBuilder;

    public PublicationDomBuilder() throws PublicationProjectException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PublicationProjectException("Error while creating builder", e);
        }
    }

    @Override
    public void buildPublicationSet(String filename) throws PublicationProjectException {
        try {
            Document document = docBuilder.parse(filename);
            Element root = document.getDocumentElement();
            NodeList publicationList = root.getChildNodes();
            for (int i = 0; i < publicationList.getLength(); i++) {
                if (publicationList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) publicationList.item(i);
                    Publication publication = buildPublication(element);
                    publicationSet.add(publication);
                }
            }
        } catch (SAXException | IOException | ParseException e) {
            throw new PublicationProjectException("error while parsing XML", e);
        }
    }

    private Publication buildPublication(Element publicationElement) throws ParseException {
        Publication publication = null;
        if (publicationElement.getTagName().equals(TagName.NEWSPAPER.getTag())) {
            publication = buildNewspaper(publicationElement);
        }
        if (publicationElement.getTagName().equals(MAGAZINE.getTag())) {
            publication = buildMagazine(publicationElement);
        }
        publication.setTitle(publicationElement.getAttribute(TITLE.getTag()));
        publication.setIssnCode(publicationElement.getAttribute(ISSN_CODE.getTag()));
        publication.setPage(Integer.parseInt(getElementByTagName(publicationElement, PAGE.getTag())));
        String periodicity = getElementByTagName(publicationElement, PERIODICITY.getTag()).toUpperCase();
        publication.setPeriodicity(Publication.Periodicity.valueOf(periodicity));
        Node publisherElement = publicationElement.getElementsByTagName(PUBLISHER.getTag()).item(0);
        publication.setPublisher(buildPublisher((Element) publisherElement));

        return publication;
    }

    private Newspaper buildNewspaper(Element newspaperElement) throws ParseException {
        Newspaper newspaper = new Newspaper();
        newspaper.setColored(Boolean.parseBoolean(getElementByTagName(newspaperElement, COLORED.getTag())));
        String printFormat = getElementByTagName(newspaperElement, PRINT_FORMAT.getTag()).toUpperCase();
        newspaper.setPrintFormat(Newspaper.PrintFormat.valueOf(printFormat));

        return newspaper;
    }

    private Magazine buildMagazine(Element magazineElement) throws ParseException {
        Magazine magazine = new Magazine();
        magazine.setGlossy(Boolean.parseBoolean(getElementByTagName(magazineElement, GLOSSY.getTag())));
        String thematic = getElementByTagName(magazineElement, THEMATIC.getTag()).toUpperCase();
        magazine.setThematic(Magazine.Thematic.valueOf(thematic));

        return magazine;
    }

    private Publisher buildPublisher(Element publisherElement) throws ParseException {
        Publisher publisher = new Publisher();
        String country = getElementByTagName(publisherElement, COUNTRY.getTag()).toUpperCase();
        publisher.setCountry(Publisher.Country.valueOf(country));
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String dateData = getElementByTagName(publisherElement, LICENSE_EXPIRATION.getTag());
        Date licenseExpiration = dateFormat.parse(dateData);
        publisher.setLicenseExpiration(licenseExpiration);

        return publisher;
    }

    private String getElementByTagName(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Element targetElement = (Element) nodeList.item(0);

        return targetElement.getTextContent();
    }

}
