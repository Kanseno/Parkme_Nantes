package i5.epsi.fr.parkmenantes.i5.epsi.fr.parjmenantes.model;

/**
 * Created by antoine on 20/01/16.
 */
public class Parking {

    String name;
    int id;
    int type;
    int zipCode;
    String website;
    String phone;
    int theme;
    String commune;
    String libCategorie;
    String address;

    String libType;
    int latitude;
    int longitude;
    int freePlaces;
    int maxPlaces;
    int busyPlaces;




    public Parking(){
        super();

    }

    public Parking(String name, int id, int type, int zipCode, String website, String phone, int theme, String commune, String libCategorie, String address, String libType, int latitude, int longitude, int freePlaces, int maxPlaces, int busyPlaces) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.zipCode = zipCode;
        this.website = website;
        this.phone = phone;
        this.theme = theme;
        this.commune = commune;
        this.libCategorie = libCategorie;
        this.address = address;
        this.libType = libType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.freePlaces = freePlaces;
        this.maxPlaces = maxPlaces;
        this.busyPlaces = busyPlaces;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getLibCategorie() {
        return libCategorie;
    }

    public void setLibCategorie(String libCategorie) {
        this.libCategorie = libCategorie;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLibType() {
        return libType;
    }

    public void setLibType(String libType) {
        this.libType = libType;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public int getBusyPlaces() {
        return busyPlaces;
    }

    public void setBusyPlaces(int busyPlaces) {
        this.busyPlaces = busyPlaces;
    }


}
