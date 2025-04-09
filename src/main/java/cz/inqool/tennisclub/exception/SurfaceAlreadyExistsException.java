package cz.inqool.tennisclub.exception;

public class SurfaceAlreadyExistsException extends RuntimeException {
    public SurfaceAlreadyExistsException(String name) {
        super("Surface with name " + name + " already exists.");
    }
}
