package by.balashevich.periodicals.builder;

import by.balashevich.periodicals.entity.Publication;
import by.balashevich.periodicals.exception.PublicationProjectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractBuilder {

    public static final Logger logger = LogManager.getLogger();
    protected Set<Publication> publicationSet;

    public AbstractBuilder(){
        publicationSet = new HashSet<>();
    }

    public Set<Publication> getPublicationSet(){
        return publicationSet;
    }

    public abstract void buildPublicationSet(String filename) throws PublicationProjectException;

}
