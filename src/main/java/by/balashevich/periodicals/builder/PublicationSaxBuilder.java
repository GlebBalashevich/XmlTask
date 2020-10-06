package by.balashevich.periodicals.builder;

import by.balashevich.periodicals.builder.handler.PublicationHandler;
import by.balashevich.periodicals.exception.PublicationProjectException;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;

public class PublicationSaxBuilder extends AbstractBuilder{
    private PublicationHandler publicationHandler;
    private SAXParser saxParser;

    public PublicationSaxBuilder(){
        publicationSet = new HashSet<>();
        publicationHandler = new PublicationHandler();
        saxParser = new SAXParser();
        saxParser.setContentHandler(publicationHandler);
    }

    @Override
    public void buildPublicationSet(String filename) throws PublicationProjectException {
        try {
            saxParser.parse(filename);
            publicationSet = publicationHandler.getPublicationSet();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}
