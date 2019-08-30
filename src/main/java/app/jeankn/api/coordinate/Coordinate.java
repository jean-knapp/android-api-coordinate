package app.jeankn.api.coordinate;

import android.util.Log;

public class Coordinate {
    private double lat = 0;
    private double lng = 0;

    public class DD {
        public double lat;
        public double lng;

        public String ToString() {
            return "Lat: " + lat + ", " + "Lng: " + lng;
        }
    }

    public class DMS {
        public char latHemisphere;
        public int latDegrees;
        public int latMinutes;
        public double latSeconds;

        public char lngHemisphere;
        public int lngDegrees;
        public int lngMinutes;
        public double lngSeconds;

        public String ToString() {
            return "Lat: " + String.format("%02d", latDegrees) + 'º' + String.format("%02d", latMinutes) + '\'' + String.format("%02d", (int)latSeconds) + '\"' + latHemisphere + ", " +
                    "Lng: " + String.format("%03d", lngDegrees) + 'º' + String.format("%02d", lngMinutes) + '\'' + String.format("%02d", (int)lngSeconds) + '\"' + lngHemisphere;
        }

        public String GetLat() {
            return latHemisphere + String.format("%02d", latDegrees) + 'º' + String.format("%02d", latMinutes) + '\'' + String.format("%02d", (int)latSeconds) + '\"';
        }

        public String GetLng() {
            return lngHemisphere + String.format("%03d", lngDegrees) + 'º' + String.format("%02d", lngMinutes) + '\'' + String.format("%02d", (int)lngSeconds) + '\"';
        }
    }

    public class DMM {
        public char latHemisphere;
        public int latDegrees;
        public double latMinutes;

        public char lngHemisphere;
        public int lngDegrees;
        public double lngMinutes;

        public String ToString() {
            return "Lat: " + String.format("%02d", latDegrees) + 'º' + String.format("%05.2f", latMinutes) + '\'' + latHemisphere + ", " +
                    "Lng: " + String.format("%03d", lngDegrees) + 'º' + String.format("%05.2f", lngMinutes) + '\'' + lngHemisphere;
        }

        public String GetLat() {
            return latHemisphere + String.format("%02d", latDegrees) + 'º' + String.format("%05.2f", latMinutes) + '\'';
        }

        public String GetLng() {
            return lngHemisphere + String.format("%03d", lngDegrees) + 'º' + String.format("%05.2f", lngMinutes) + '\'';
        }
    }

    private Coordinate() {


    }

    public static Coordinate FromDD(double lat, double lng) {
        Coordinate coordinate = new Coordinate();
        coordinate.lat = lat;
        coordinate.lng = lng;
        return coordinate;
    }

    public static Coordinate FromDMS(int latHemisphere, int latDegrees, int latMinutes, double latSeconds, int lngHemisphere, int lngDegrees, int lngMinutes, double lngSeconds) {
        Coordinate coordinate = new Coordinate();
        coordinate.lat = latHemisphere * (latDegrees + (double)latMinutes / 60 + (double)latSeconds / 3600);
        coordinate.lng = lngHemisphere * (lngDegrees + (double)lngMinutes / 60 + (double)lngSeconds / 3600);
        return coordinate;
    }

    public static Coordinate FromDMS(char latHemisphere, int latDegrees, int latMinutes, double latSeconds, char lngHemisphere, int lngDegrees, int lngMinutes, double lngSeconds) {
        return Coordinate.FromDMS((latHemisphere == 'N' ? 1 : -1), latDegrees, latMinutes, latSeconds, (lngHemisphere == 'W' ? -1 : 1), lngDegrees, lngMinutes, lngSeconds);
    }

    public static Coordinate FromDMS(String lat, String lng) {
        char latHemisphere;
        int latDegrees;
        int latMinutes;
        double latSeconds;

        char lngHemisphere;
        int lngDegrees;
        int lngMinutes;
        double lngSeconds;

        Log.d("FromDMS", "Lat: " + lat + ", lng: " + lng);

        lat = lat.toUpperCase();
        lng = lng.toUpperCase();

        if (lat.contains("N")) {
            lat = lat.replace("N", "");
            latHemisphere = 'N';
        } else if (lat.contains("S")) {
            lat = lat.replace("S", "");
            latHemisphere = 'S';
        } else {
            return null;
        }
        Log.d("FromDMS", "LatH:" + latHemisphere);

        if (lng.contains("W")) {
            lng = lng.replace("W", "");
            lngHemisphere = 'W';
        } else if (lng.contains("E")) {
            lng = lng.replace("E", "");
            lngHemisphere = 'E';
        } else {
            return null;
        }
        Log.d("FromDMS", "LngH:" + lngHemisphere);

        lat = lat
                .replace("\"", "")
                .replace("''", "")
                .replace(" ", "")
                .replace(",", ".")
        ;
        lng = lng
                .replace("\"", "")
                .replace("''", "")
                .replace(" ", "")
                .replace(",", ".")
        ;

        if (lat.contains("º") && lat.contains("'")) {
            String[] parts = lat.split("º|'");

            latDegrees = Integer.valueOf(parts[0]);
            latMinutes = Integer.valueOf(parts[1]);
            latSeconds = Double.valueOf(parts[2]);
        } else {
            Log.d("FromDMM", "wrong lat format: " + lat);
            return null;
        }
        Log.d("FromDMM", "LatD:" + latDegrees);
        Log.d("FromDMM", "LatM:" + latMinutes);
        Log.d("FromDMM", "LatS:" + latSeconds);

        if (lng.contains("º") && lng.contains("'")) {
            String[] parts = lng.split("º|'");

            lngDegrees = Integer.valueOf(parts[0]);
            lngMinutes = Integer.valueOf(parts[1]);
            lngSeconds = Double.valueOf(parts[2]);
        } else {
            Log.d("FromDMM", "wrong lng format: " + lng);
            return null;
        }
        Log.d("FromDMM", "LngD:" + lngDegrees);
        Log.d("FromDMM", "LngM:" + lngMinutes);
        Log.d("FromDMM", "LngS:" + lngSeconds);

        try {

            return Coordinate.FromDMS(latHemisphere, latDegrees, latMinutes, latSeconds, lngHemisphere, lngDegrees, lngMinutes, lngSeconds);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static Coordinate FromDMM(int latHemisphere, int latDegrees, double latMinutes, int lngHemisphere, int lngDegrees, double lngMinutes) {
        Coordinate coordinate = new Coordinate();
        coordinate.lat = latHemisphere * (latDegrees + latMinutes / 60);
        coordinate.lng = lngHemisphere * (lngDegrees + lngMinutes / 60);
        return coordinate;
    }

    public static Coordinate FromDMM(String lat, String lng) {
        char latHemisphere;
        int latDegrees;
        double latMinutes;

        char lngHemisphere;
        int lngDegrees;
        double lngMinutes;

        Log.d("FromDMM", "Lat: " + lat + ", lng: " + lng);

        lat = lat.toUpperCase();
        lng = lng.toUpperCase();

        if (lat.contains("N")) {
            lat = lat.replace("N", "");
            latHemisphere = 'N';
        } else if (lat.contains("S")) {
            lat = lat.replace("S", "");
            latHemisphere = 'S';
        } else {
            return null;
        }
        Log.d("FromDMM", "LatH:" + latHemisphere);

        if (lng.contains("W")) {
            lng = lng.replace("W", "");
            lngHemisphere = 'W';
        } else if (lng.contains("E")) {
            lng = lng.replace("E", "");
            lngHemisphere = 'E';
        } else {
            return null;
        }
        Log.d("FromDMM", "LngH:" + lngHemisphere);

        lat = lat
                .replace("'", "")
                .replace(" ", "")
                .replace(",", ".")
        ;
        lng = lng
                .replace("'", "")
                .replace(" ", "")
                .replace(",", ".")
        ;

        if (lat.contains("º")) {
            String[] parts = lat.split("º");

            latDegrees = Integer.valueOf(parts[0]);
            latMinutes = Double.valueOf(parts[1]);
        } else {
            Log.d("FromDMM", "wrong lat format: " + lat);
            return null;
        }
        Log.d("FromDMM", "LatD:" + latDegrees);
        Log.d("FromDMM", "LatM:" + latMinutes);

        if (lng.contains("º")) {
            String[] parts = lng.split("º");

            lngDegrees = Integer.valueOf(parts[0]);
            lngMinutes = Double.valueOf(parts[1]);
        } else {
            Log.d("FromDMM", "wrong lng format: " + lng);
            return null;
        }
        Log.d("FromDMM", "LngD:" + lngDegrees);
        Log.d("FromDMM", "LngM:" + lngMinutes);

        try {

            return Coordinate.FromDMM(latHemisphere, latDegrees, latMinutes, lngHemisphere, lngDegrees, lngMinutes);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static Coordinate FromDMM(char latHemisphere, int latDegrees, double latMinutes, char lngHemisphere, int lngDegrees, double lngMinutes) {
        return Coordinate.FromDMM((latHemisphere == 'N' ? 1 : -1), latDegrees, latMinutes, (lngHemisphere == 'W' ? -1 : 1), lngDegrees, lngMinutes);
    }

    public static Coordinate FromMGRS(String longZone, String latZone, String digraph, String easting, String northing) {
        //Coordinate coordinate = new Coordinate();
        String MGRS = longZone + latZone + digraph + easting + northing;
        return FromMGRS(MGRS);
    }

    public static Coordinate FromMGRS(String MGRS) {
        Coordinate c = new MGRS2LatLon().convertMGRSToLatLong(MGRS);
        return c;
    }

    public static Coordinate FromUTM(String longZone, String latZone, String easting, String northing) {
        String UTM = longZone + " " + latZone + " " + easting + " " + northing;
        return FromUTM(UTM);
    }

    public static Coordinate FromUTM(String longZone, String latZone, int easting, int northing) {
        String UTM = longZone + " " + latZone + " " + easting + " " + northing;
        return FromUTM(UTM);
    }

    public static Coordinate FromUTM(String UTM) {
        Coordinate c = new UTM2LatLon().convertUTMToLatLong(UTM);
        return c;
    }

    public DD ToDD() {
        DD coordinate = new DD();
        coordinate.lat = lat;
        coordinate.lng = lng;
        return coordinate;
    }

    public DMS ToDMS() {
        DMS c = new DMS();

        c.latSeconds = (int)Math.round(lat * 3600);
        c.latDegrees = (int)Math.abs(c.latSeconds / 3600);
        c.latSeconds = Math.abs(c.latSeconds % 3600);
        c.latMinutes = (int)(c.latSeconds / 60);
        c.latSeconds %= 60;

        c.lngSeconds = (int)Math.round(lng * 3600);
        c.lngDegrees = (int)Math.abs(c.lngSeconds / 3600);
        c.lngSeconds = Math.abs(c.lngSeconds % 3600);
        c.lngMinutes = (int)(c.lngSeconds / 60);
        c.lngSeconds %= 60;

        c.latHemisphere = (lat > 0 ? 'N' : 'S');
        c.lngHemisphere = (lng > 0 ? 'E' : 'W');

        return c;
    }

    public DMM ToDMM() {
        DMM c = new DMM();

        c.latDegrees = (int)Math.abs(lat);
        c.latMinutes = (Math.abs(lat) - c.latDegrees) * 60;

        c.lngDegrees = (int)Math.abs(lng);
        c.lngMinutes = (Math.abs(lng) - c.lngDegrees) * 60;

        c.latHemisphere = (lat > 0 ? 'N' : 'S');
        c.lngHemisphere = (lng > 0 ? 'E' : 'W');

        return c;
    }

    public LatLon2MGRS.MGRS ToMGRS() {
        LatLon2MGRS.MGRS c = new LatLon2MGRS().convertLatLonToMGRS(lat, lng);
        return c;
    }

    public LatLon2UTM.UTM ToUTM() {
        LatLon2UTM.UTM c = new LatLon2UTM().convertLatLonToUTM(lat, lng);
        return c;
    }
}
