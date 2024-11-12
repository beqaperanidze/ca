package prjnightsky.service.generator;

public class CoordinateParser {
    public static double[] parseLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return new double[]{0.0, 0.0};
        }

        try {
            String[] parts = location.split(",");
            if (parts.length != 2) {
                return new double[]{0.0, 0.0};
            }

            double lat = parseCoordinate(parts[0].trim(), "N", "S");
            double lon = parseCoordinate(parts[1].trim(), "E", "W");
            return new double[]{lat, lon};
        } catch (Exception e) {
            return new double[]{0.0, 0.0};
        }
    }

    public static double parseCoordinate(String coord, String posDir, String negDir) {
        try {
            String numericPart = coord.replaceAll("[^0-9.-]", "");
            double value = Double.parseDouble(numericPart);

            if (coord.toUpperCase().contains(negDir)) {
                value = -Math.abs(value);
            } else if (coord.toUpperCase().contains(posDir)) {
                value = Math.abs(value);
            }

            return value;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static int[] parseSize(String size) {
        if (size == null) {
            return new int[]{800, 800};
        }

        return switch (size.toLowerCase()) {
            case "small" -> new int[]{600, 600};
            case "large" -> new int[]{1200, 1200};
            default -> new int[]{800, 800};
        };
    }
}