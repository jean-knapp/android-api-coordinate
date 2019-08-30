package app.jeankn.api.coordinate;

import java.util.Locale;

class LatLon2MGRS extends LatLon2UTM
{
    private final char[] digraphArrayE;


    public class MGRS {
        public String longZone;
        public char latZone;
        public String digraph;
        public String easting;
        public String northing;

        public String ToString() {
            return longZone + latZone + " " + digraph + " " + easting + " " + northing;
        }
    }

    LatLon2MGRS( )
    {
        digraphArrayE = new char[]{
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };
    }

    MGRS convertLatLonToMGRS( double latitude, double longitude )
    {
        verifyLatLon( latitude, longitude );
        convert( latitude, longitude );

        String digraph = calcDigraph( );
        String eastingStr = formatIngValue( eastingValue );
        String northingStr = formatIngValue( northingValue );

        MGRS mgrs = new MGRS();
        mgrs.longZone = String.format("%02d", longitudeZoneValue);
        mgrs.latZone = digraphArrayN[ latitudeZoneValue ];
        mgrs.digraph = digraph;
        mgrs.easting = eastingStr;
        mgrs.northing = northingStr;

        return mgrs;
    }

    private String calcDigraph( )
    {
        int letter = (int) Math.floor( ( longitudeZoneValue - 1 ) * 8 + ( eastingValue ) / 100000.0 );
        int letterIdx = (( letter % 24 ) + 23 ) % 24;

        char digraph = digraphArrayE[ letterIdx ];

        letter = (int) Math.floor( northingValue / 100000.0 );
        if ( longitudeZoneValue / 2.0 == Math.floor( longitudeZoneValue / 2.0 ) )
        {
            letter = letter + 5;
        }

        letterIdx = letter - (int) ( 20 * Math.floor( letter / 20.0 ) );

        return String.format( "%c%c", digraph, digraphArrayN[ letterIdx ] );
    }

    private String formatIngValue( double value )
    {
        String str = String.format( Locale.getDefault( ), "%d", (int) Math.round( value - 100000 * Math.floor( value / 100000 ) ) );

        if ( str.length( ) < 5 )
        {

            str = String.format( "00000%s", str );
        }

        return str.substring( str.length( ) - 5 );
    }
}
