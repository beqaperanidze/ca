package prjnightsky.service.generator;

import prjnightsky.entity.StarMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.geom.Point2D;

import static prjnightsky.service.generator.StarCatalog.STAR_CATALOG;


public class StarMapGenerator {

    private static final Map<String, ColorScheme> COLOR_SCHEMES = new HashMap<>() {{
        put("classic", new ColorScheme("#000033", "#FFFFFF", "#333366", "#4444AA"));
        put("midnight", new ColorScheme("#000000", "#00FFFF", "#003366", "#0066CC"));
        put("vintage", new ColorScheme("#2B1B17", "#DEB887", "#8B4513", "#A0522D"));
        put("modern", new ColorScheme("#1A1A1A", "#00FF00", "#333333", "#4D4D4D"));
    }};

    private record ColorScheme(String background, String stars, String grid, String text) {
    }

    public static String generateCustomStarMap(StarMap starMap) {
        double[] coordinates = parseLocation(starMap.getLocation());
        double latitude = coordinates[0];
        double longitude = coordinates[1];

        LocalDateTime dateTime = LocalDateTime.of(starMap.getDate(), starMap.getTime());

        ColorScheme colors = COLOR_SCHEMES.getOrDefault(starMap.getColorScheme(), COLOR_SCHEMES.get("classic"));

        int[] dimensions = parseSize(starMap.getSize());
        int width = dimensions[0];
        int height = dimensions[1];

        StringBuilder svg = new StringBuilder();
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.min(width, height) * 0.45;

        svg.append(String.format("""
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 %d %d">
                <rect width="%d" height="%d" fill="%s"/>
                """, width, height, width, height, colors.background));

        if (starMap.isShowGrid()) {
            addGrid(svg, centerX, centerY, radius, colors.grid);
        }

        double lst = calculateLST(dateTime, longitude);

        if (starMap.isShowConstellations()) {
            drawConstellations(svg, lst, latitude, centerX, centerY, radius, colors.grid);
        }

        for (Star star : STAR_CATALOG) {
            Point2D.Double point = convertToXY(star, lst, latitude, centerX, centerY, radius);
            if (point != null) {
                double starSize = Math.max(1, (4 - star.magnitude()));

                svg.append(String.format("""
                        <circle cx="%f" cy="%f" r="%f" fill="%s"/>
                        """, point.x, point.y, starSize, colors.stars));

                if (starMap.isShowLabels()) {
                    svg.append(String.format("""
                            <text x="%f" y="%f" fill="%s" font-size="12" text-anchor="middle">%s</text>
                            """, point.x, point.y + 15, colors.text, star.name()));
                }
            }
        }

        if (starMap.getCustomMessage() != null && !starMap.getCustomMessage().isEmpty()) {
            svg.append(String.format("""
                    <text x="%f" y="%d" fill="%s" font-size="20" text-anchor="middle">%s</text>
                    """, centerX, height - 30, colors.text, starMap.getCustomMessage()));
        }

        addMapDetails(svg, starMap, colors.text);

        svg.append("</svg>");

        return svg.toString();
    }

    private static void addGrid(StringBuilder svg, double centerX, double centerY, double radius, String gridColor) {

        for (int alt = 15; alt <= 90; alt += 15) {
            double r = radius * (90 - alt) / 90.0;
            svg.append(String.format("""
                    <circle cx="%f" cy="%f" r="%f" fill="none"
                            stroke="%s" stroke-width="0.5" stroke-dasharray="5,5"/>
                    """, centerX, centerY, r, gridColor));
        }

        for (int az = 0; az < 360; az += 30) {
            double x2 = centerX + radius * Math.sin(Math.toRadians(az));
            double y2 = centerY - radius * Math.cos(Math.toRadians(az));
            svg.append(String.format("""
                    <line x1="%f" y1="%f" x2="%f" y2="%f"
                          stroke="%s" stroke-width="0.5" stroke-dasharray="5,5"/>
                    """, centerX, centerY, x2, y2, gridColor));
        }
    }

    private static void addMapDetails(StringBuilder svg, StarMap starMap, String textColor) {
        svg.append(String.format("""
                <text x="20" y="30" fill="%s" font-size="14">Location: %s</text>
                <text x="20" y="50" fill="%s" font-size="14">Date: %s</text>
                <text x="20" y="70" fill="%s" font-size="14">Time: %s</text>
                """, textColor, starMap.getLocation(), textColor, starMap.getDate(), textColor, starMap.getTime()));
    }

    private static double[] parseLocation(String location) {
        String[] parts = location.split(",");
        double lat = parseCoordinate(parts[0].trim(), "N", "S");
        double lon = parseCoordinate(parts[1].trim(), "E", "W");
        return new double[]{lat, lon};
    }

    private static double parseCoordinate(String coord, String posDir, String negDir) {
        double value = Double.parseDouble(coord.replaceAll("[^0-9.]", ""));
        if (coord.contains(negDir)) value = -value;
        return value;
    }

    private static int[] parseSize(String size) {
        return switch (size.toLowerCase()) {
            case "small" -> new int[]{600, 600};
            case "large" -> new int[]{1200, 1200};
            default -> new int[]{800, 800};
        };
    }

    public static double calculateLST(LocalDateTime dateTime, double longitude) {

        LocalDateTime utc = dateTime.atOffset(ZoneOffset.systemDefault().getRules().getOffset(dateTime)).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();

        int year = utc.getYear();
        int month = utc.getMonthValue();
        int day = utc.getDayOfMonth();
        double hour = utc.getHour() + utc.getMinute() / 60.0 + utc.getSecond() / 3600.0;

        if (month <= 2) {
            year -= 1;
            month += 12;
        }

        int a = year / 100;
        int b = 2 - a + a / 4;

        double jd = Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5 + hour / 24.0;

        double t = (jd - 2451545.0) / 36525.0;
        double gmst = 280.46061837 + 360.98564736629 * (jd - 2451545.0) + t * t * (0.000387933 - t / 38710000.0);

        gmst = gmst % 360.0;
        if (gmst < 0) {
            gmst += 360.0;
        }

        double lst = gmst + longitude;
        lst = lst % 360.0;
        if (lst < 0) {
            lst += 360.0;
        }

        return lst;
    }

    public static Point2D.Double convertToXY(Star star, double lst, double latitude, double centerX, double centerY, double radius) {

        double ha = lst - star.rightAscension() * 15.0;
        if (ha < 0) {
            ha += 360.0;
        }


        double latRad = Math.toRadians(latitude);
        double decRad = Math.toRadians(star.declination());
        double haRad = Math.toRadians(ha);


        double sinAlt = Math.sin(decRad) * Math.sin(latRad) + Math.cos(decRad) * Math.cos(latRad) * Math.cos(haRad);
        double alt = Math.asin(sinAlt);


        if (Math.toDegrees(alt) < 0) {
            return null;
        }

        double cosA = (Math.sin(decRad) - Math.sin(latRad) * sinAlt) / (Math.cos(latRad) * Math.cos(alt));
        cosA = Math.max(-1, Math.min(1, cosA));
        double az = Math.acos(cosA);

        if (Math.sin(haRad) > 0) {
            az = 2 * Math.PI - az;
        }

        double r = radius * (Math.PI / 2 - alt) / (Math.PI / 2);
        double x = centerX + r * Math.sin(az);
        double y = centerY - r * Math.cos(az);

        return new Point2D.Double(x, y);
    }

    public static double hoursToDegrees(double hours) {
        return hours * 15.0;
    }

    public static double normalizeAngle(double angle) {
        angle = angle % 360.0;
        if (angle < 0) {
            angle += 360.0;
        }
        return angle;
    }

    private static void drawConstellations(StringBuilder svg, double lst, double latitude, double centerX, double centerY, double radius, String color) {

        Map<String, List<double[]>> constellationLines = getConstellationData();

        for (Map.Entry<String, List<double[]>> constellation : constellationLines.entrySet()) {
            StringBuilder path = new StringBuilder();
            boolean first = true;

            for (double[] star : constellation.getValue()) {
                Point2D.Double point = convertToXY(new Star("", star[0], star[1], 0), lst, latitude, centerX, centerY, radius);

                if (point != null) {
                    path.append(first ? "M " : "L ").append(point.x).append(" ").append(point.y).append(" ");
                    first = false;
                }
            }

            if (!path.isEmpty()) {
                svg.append(String.format("""
                        <path d="%s" fill="none" stroke="%s" 
                              stroke-width="0.5" stroke-opacity="0.5"/>
                        """, path.toString(), color));
            }
        }
    }

    private static Map<String, List<double[]>> getConstellationData() {
        Map<String, List<double[]>> constellations = new HashMap<>();

        List<double[]> ursaMajor = new ArrayList<>();
        ursaMajor.add(new double[]{11.0622, 61.7508});
        ursaMajor.add(new double[]{11.0307, 56.3824});
        ursaMajor.add(new double[]{11.8977, 53.6948});
        ursaMajor.add(new double[]{12.2570, 57.0326});
        ursaMajor.add(new double[]{12.9004, 55.9598});
        ursaMajor.add(new double[]{13.7923, 49.3133});
        ursaMajor.add(new double[]{13.7923, 49.3133});
        constellations.put("Ursa Major", ursaMajor);

        return constellations;
    }

}