package com.danesisinni;

import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        String csvFile = "file.csv";
        String enumName = "NAME";
        String enumFile = "file.java";

        if (args.length == 3) {
            csvFile = args[0];
            enumName = args[1];
            enumFile = args[2];
        }

        CSVEnumGenerator  generator = new CSVEnumGenerator(csvFile, enumName);
        val enumData = generator.getEnum();
        writeToFile(enumData, enumFile);
    }

    public static void writeToFile(String data, String fileName) {
        try {
            val path = Paths.get(fileName);
            Files.createFile(path);
            Files.write(path, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
