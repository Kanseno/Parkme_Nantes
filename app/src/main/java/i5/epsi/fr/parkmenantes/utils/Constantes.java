package i5.epsi.fr.parkmenantes.utils;

/**
 * Created by klarhant on 16/02/2016.
 */
public class Constantes {

    //URL
    public static final String URL_DISPO_PARKING = "http://data.nantes.fr/api/getDisponibiliteParkingsPublics/1.0/0TZIF9VWW3BTCBC/?output=json";
    public static final String URL_LOCATION_EQUIPEMENT= "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content";

    public static final String FILTER1 = "?filter={%22_IDOBJ%22:{%22$eq%22:";
    public static final String FILTER2 = "}}/?format=json";

    //JSON data
    public static final String OPENDATA = "opendata";
    public static final String ANSWER = "answer";
    public static final String DATA = "data";
    public static final String GROUPES_PARKING = "Groupes_Parking";
    public static final String GROUPE_PARKING = "Groupe_Parking";
    public static final String ID_OBJET = "IdObj";
    public static final String PARKING_NAME = "Grp_nom";
    public static final String PARKING_PLACES_DISPO = "Grp_disponible";
    public static final String PARKING_MAX_PLACES = "Grp_exploitation";

    //Snippet
    public static final String SNIPPET_PLACES_MAX = "Places Maximales";

    //Other
    public static final String EMPTY = "";
    public static final String COLON = " : ";
}
