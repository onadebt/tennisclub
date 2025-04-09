package cz.inqool.tennisclub.exception;

public class SurfaceTypeNotFoundException extends RuntimeException {
    public SurfaceTypeNotFoundException(Long id) {
        super("Surface type with id '" + id + "' not found.");
    }

    public SurfaceTypeNotFoundException(String name) {
        super("Surface type with name '" + name + "' not found.");
    }
}
