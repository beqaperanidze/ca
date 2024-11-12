package prjnightsky.service.generator;


/**
 * @param rightAscension in hours
 * @param declination    in degrees
 */
public record Star(String name, double rightAscension, double declination, double magnitude) {

}

