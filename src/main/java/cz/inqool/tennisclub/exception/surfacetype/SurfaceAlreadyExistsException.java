package cz.inqool.tennisclub.exception.surfacetype;

public class SurfaceAlreadyExistsException extends RuntimeException {
    public SurfaceAlreadyExistsException(String name) {
        super("Surface with name " + name + " already exists.");
    }
}
