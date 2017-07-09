# Java Enum Generator - from CSV files
Generates java Enums from CSV files

## Goals
* enables seamless generation of Java enums for large datasets

## Formating the CSV file
* the CSV file must be properly formatted in order for the generator to run properly
* the first line of the CSV consists of all the variable types/names
* one of the header cells must contain the word `VALUE` which indicates the column is reserved for the enum constant names
* example header: `VALUE,String country,String code`

## Running from the Command Line
* The jar file is located in `out/artificats/CsvEnumGenerator_jar`
* Argument Ordering:
  1. CSV file to read from
  2. Name of the enum
  3. output file
* Running the generator from the command line: `java -jar CsvEnumGenerator.jar csvFile.csv ENUMNAME enumname.java`
* if using Intellij use `Ctrl+Alt+L` to reformat the enum source code

## Sample parseable CSV
```csv
VALUE,String country,String alpha2,String alpha3
CANADA,Canada,CA,CAN
UNITED_STATES,United States,US,USA
```
* aside from the reserved `VALUE` cell header, all other headers must contain a space to separate the variable type and variable name

## Generating with custom data types
* whenever a custom data type is used simply create an additional constructor after the enum is generated that parses the String and converts it to the specified object type

#### Custom data type example
```csv
VALUE,String country,LocalDate foundedOn
CANADA,Canada,1867-07-01
UNITED_STATES,United States of America,1776-07-04
```
##### Generated Enum:
```java
public enum COUNTRY { 

  CANADA("Canada","1867-07-01"),
  UNITED_STATES("United States Of America","1776-07-04");

  private final String country;
  private final LocalDate foundedOn;


  COUNTRY(String country,LocalDate foundedOn) {
    this.country = country;
    this.foundedOn = foundedOn
  }
  
  public String getCountry() {
    return this.country;
  }

  public LocalDate getFoundedOn() {
    return this.foundedOn;
  }
}
```

##### Code to add:
```java
 private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
 COUNTRY(String country, String foundedOn) {
  LocalDate date = LocalDate.parse(foundedOn, formatter);
  this(country, date);
 }
```
* As you can see for large data sets the csv-enum generator will signficantly reduce the manual work/number of lines a developer has to write

## Technologies Used
* Java 8
* Maven
* Guava
* Lombok
* Apache Commons Lang
* Apache Commons CSV

Please feel free to reach out if there are any improvements that can be made or if you have any questions :)
