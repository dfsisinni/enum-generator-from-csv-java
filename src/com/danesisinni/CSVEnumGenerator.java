package com.danesisinni;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVEnumGenerator {

    private static final String ENUM_INITIALIZATION_VALUE = "VALUE";
    private static final ImmutableSet<String> NON_QUOTE_DATA_TYPES = new ImmutableSet.Builder<String>()
            .add("int")
            .add("Integer")
            .add("long")
            .add("Long")
            .add("double")
            .add("Double")
            .add("Float")
            .add("float")
            .build();

    private final String CSV_FILE;
    private final String ENUM_NAME;

    public CSVEnumGenerator(String CSV_FILE, String ENUM_NAME) {
        this.CSV_FILE = CSV_FILE;
        this.ENUM_NAME = ENUM_NAME;
    }

    public String getEnum() {
        val enumWrapper = getDataFromCSVFile();

        val enumBuilder = new StringBuilder();
        enumBuilder.append("public enum ");
        enumBuilder.append(ENUM_NAME);
        enumBuilder.append(" { \n\n");

        val arguments = enumWrapper.getDataTypes();
        val argumentOrder = arguments.keySet().asList();


        val initializationList = new ArrayList<String>();
        for (EnumData entry : enumWrapper.getEnumData()) {
            val instanceBuilder = new StringBuilder();
            instanceBuilder.append(entry.getName());
            instanceBuilder.append("(");

            val paramsList = new ArrayList<String>();
            for (String argument : argumentOrder) {
                val arg = entry.getInitializationValues().get(argument);
                if (needsQuotes(arguments.get(argument), arg)) {
                    paramsList.add("\"" + arg + "\"");
                } else {
                    paramsList.add(arg);
                }
            }
            instanceBuilder.append(Joiner.on(",").join(paramsList));
            instanceBuilder.append(")");
            initializationList.add(instanceBuilder.toString());
        }

        enumBuilder.append(Joiner.on(",\n").join(initializationList));
        enumBuilder.append(";\n\n");

        for (String argument : argumentOrder) {
            enumBuilder.append("private final ");
            enumBuilder.append(arguments.get(argument));
            enumBuilder.append(" ");
            enumBuilder.append(argument);
            enumBuilder.append(";\n");
        }

        enumBuilder.append("\n\n");
        enumBuilder.append(ENUM_NAME);
        enumBuilder.append("(");

        val parameters = new ArrayList<String>();
        for (String argument : argumentOrder) {
            parameters.add(arguments.get(argument) + " " + argument);
        }
        enumBuilder.append(Joiner.on(",").join(parameters));
        enumBuilder.append(") {\n");

        val initialize = new ArrayList<String>();
        for (String argument : argumentOrder) {
            initialize.add("this." + argument + " = " + argument);
        }
        enumBuilder.append(Joiner.on(";\n").join(initialize));
        enumBuilder.append("\n}\n\n}");

        return enumBuilder.toString();
    }

    private boolean needsQuotes(String dataType, String value) {
        return !(value.equals("null") || NON_QUOTE_DATA_TYPES.contains(dataType));
    }

    private EnumWrapper getDataFromCSVFile() {
        final EnumWrapper data;
        try {
            data = getDataFromCSVFileThrowsException();
        } catch (IOException ex) {
            throw new IllegalStateException(String.format("Unable to parse file: %s", CSV_FILE), ex);
        }
        return data;
    }

    private EnumWrapper getDataFromCSVFileThrowsException() throws IOException {
        val data = new ArrayList<EnumData>();
        val fileReader = new FileReader(CSV_FILE);
        val records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fileReader);

        val headerDataTypes = new HashMap<String, String>();
        val headerMap = records.getHeaderMap();

        for (String header : headerMap.keySet()) {
            if (!header.trim().equals(ENUM_INITIALIZATION_VALUE)) {
                val split = header.trim().split(" ");
                if (split.length != 2) {
                    throw new IllegalStateException(String.format("Invalid header: %s", header));
                }

                headerDataTypes.put(split[1], split[0]);
            }
        }

        for (CSVRecord record : records) {
            val entry = new HashMap<String, String>();
            for (String header : headerMap.keySet()) {
                if (!header.trim().equals(ENUM_INITIALIZATION_VALUE)) {
                    entry.put(header.trim().split(" ")[1], record.get(headerMap.get(header)));
                }
            }
            data.add(new EnumData(record.get(headerMap.get(ENUM_INITIALIZATION_VALUE)), ImmutableMap.copyOf(entry)));
        }

        return new EnumWrapper(ImmutableList.copyOf(data), ImmutableMap.copyOf(headerDataTypes));
    }

    @Data
    @AllArgsConstructor
    static class EnumWrapper {
        private ImmutableList<EnumData> enumData;
        private ImmutableMap<String, String> dataTypes;
    }

    @Data
    @AllArgsConstructor
    static class EnumData {

        private String name;
        private ImmutableMap<String, String> initializationValues;

    }

}
