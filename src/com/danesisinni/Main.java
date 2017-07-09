package com.danesisinni;

public class Main {

    public static void main(String[] args) {
        CSVEnumGenerator  generator = new CSVEnumGenerator("file.csv", "NAME");
        System.out.println(generator.getEnum());
    }
}
