public enum COUNTRY { 

CANADA("1867-07-01","Canada"),
UNITED_STATES("1776-07-04","United States Of America");

private final LocalDate foundedOn;
private final String countryName;


COUNTRY(LocalDate foundedOn,String countryName) {
this.foundedOn = foundedOn;
this.countryName = countryName;
}

public LocalDate getFoundedOn() {
return this.foundedOn;
}

public String getCountryName() {
return this.countryName;
}

}