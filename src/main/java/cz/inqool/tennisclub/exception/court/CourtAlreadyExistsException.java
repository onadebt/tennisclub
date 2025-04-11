package cz.inqool.tennisclub.exception.court;

public class CourtAlreadyExistsException extends RuntimeException {
    public CourtAlreadyExistsException(String courtName) {
        super("court with name " + courtName + " already exists");
    }
}
