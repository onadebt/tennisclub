package cz.inqool.tennisclub.exception.court;

public class CourtNotFoundException extends RuntimeException {
    public CourtNotFoundException(String courtName) {
        super("Court with name " + courtName + " not found");
    }

    public CourtNotFoundException(Long id) {
        super("Court with id " + id + " not found");
    }
}
