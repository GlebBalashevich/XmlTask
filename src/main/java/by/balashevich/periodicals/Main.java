package by.balashevich.periodicals;

import by.balashevich.periodicals.builder.*;
import by.balashevich.periodicals.entity.Publication;
import by.balashevich.periodicals.exception.PublicationProjectException;

import java.util.Set;

public class Main {
    public static void main(String[] args) throws PublicationProjectException {
        System.out.println("SAX:");
        AbstractBuilder builderSax= new PublicationSaxBuilder();
        builderSax.buildPublicationSet("datares/publications.xml");
        Set<Publication> publicationsSax = builderSax.getPublicationSet();
        for(Publication publication : publicationsSax){
            System.out.println(publication);
        }

        System.out.println("DOM:");
        AbstractBuilder builderDom = new PublicationDomBuilder();
        builderDom.buildPublicationSet("datares/publications.xml");
        Set<Publication> publicationsDom = builderDom.getPublicationSet();
        for(Publication publication : publicationsDom){
            System.out.println(publication);
        }

        System.out.println("STAX:");
        AbstractBuilder builderStax = new PublicationStaxBuilder();
        builderStax.buildPublicationSet("datares/publications.xml");
        Set<Publication> publicationsStax = builderStax.getPublicationSet();
        for(Publication publication : publicationsStax){
            System.out.println(publication);
        }
    }
}
