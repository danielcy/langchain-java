package com.topopixel.library.langchain.java.sql_database;

import com.topopixel.library.langchain.java.exception.ValueErrorException;
import com.topopixel.library.langchain.java.utils.SnakeJsonUtils;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.var;

public class SQLDatabase {

    private Connection engine;
    private String schema;
    private List<String> allTables;
    private List<String> includeTables;
    private List<String> ignoreTables;
    private List<String> usableTables;
    private Integer sampleRowsInTableInfo;
    private Boolean indexesInTableInfo;
    private Map<String, Object> customTableInfo;
    private Integer maxStringLength;

    @Builder
    public SQLDatabase(Connection engine, String schema, List<String> ignoreTables,
        List<String> includeTables, Integer sampleRowsInTableInfo,
        Boolean indexesInTableInfo, Map<String, Object> customTableInfo,
        Boolean viewSupport, Integer maxStringLength) {

        this.engine = engine;
        this.schema = schema;
        if (includeTables != null && ignoreTables != null) {
            throw new ValueErrorException("Cannot specify both include_tables and ignore_tables");
        }
        allTables = getTableNames(schema);
        // TODO: view support

        this.includeTables = includeTables != null ? includeTables: new ArrayList<>();
        if (!this.includeTables.isEmpty()) {
            List<String> missingTables = this.includeTables.stream()
                .filter(t -> !allTables.contains(t)).collect(Collectors.toList());
            if (!missingTables.isEmpty()) {
                throw new ValueErrorException(String.format("includeTables %s not found in database", missingTables.toString()));
            }
        }
        this.ignoreTables = ignoreTables != null ? ignoreTables : new ArrayList<>();
        if (!this.ignoreTables.isEmpty()) {
            List<String> missingTables = this.ignoreTables.stream()
                .filter(t -> !allTables.contains(t)).collect(Collectors.toList());
            if (!missingTables.isEmpty()) {
                throw new ValueErrorException(String.format("ignoreTables %s not found in database", missingTables.toString()));
            }
        }

        List<String> usableTables = getUsableTableNames();
        this.usableTables = usableTables.isEmpty() ? allTables: usableTables;

        this.sampleRowsInTableInfo = sampleRowsInTableInfo != null ? sampleRowsInTableInfo : 3;
        this.indexesInTableInfo = indexesInTableInfo != null ? indexesInTableInfo : false;
        this.customTableInfo = customTableInfo;
        if (customTableInfo != null &&!customTableInfo.isEmpty()) {
            Set<String> keySet = this.customTableInfo.keySet();
            this.customTableInfo = keySet.stream()
                .filter(key -> allTables.contains(key))
                .collect(Collectors.toMap(Function.identity(), this.customTableInfo::get));
        }
        this.maxStringLength = maxStringLength != null ? maxStringLength: 300;

        // TODO: metadata
    }

    public List<String> getUsableTableNames() {
        if (!includeTables.isEmpty()) {
            return includeTables;
        }
        return allTables.stream().filter(t -> !ignoreTables.contains(t))
            .collect(Collectors.toList());
    }

    public List<String> getTableNames(String schema) {
        try {
            Statement stmt = engine.createStatement();
            String sql = String.format("select table_name from information_schema.tables where table_schema='%s'", schema);
            ResultSet result = stmt.executeQuery(sql);
            List<String> list = new ArrayList<>();
            while(result.next())
            {
                list.add(result.getString("TABLE_NAME"));
            }
            result.close();
            stmt.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static SQLDatabaseBuilder uri(String uri) {
        return uri(uri, null, null);
    }

    public static SQLDatabaseBuilder uri(String uri, String username, String password) {
        try {
            if (username == null && password == null) {
                Connection engine = DriverManager.getConnection(uri);
                return SQLDatabase.builder().engine(engine);
            }
            Connection engine = DriverManager.getConnection(uri, username, password);
            return SQLDatabase.builder().engine(engine);
        } catch (SQLException e) {
            throw new ValueErrorException("Cannot conntect to mysql with uri");
        }
    }

    public String run(String command) {
        return run(command, null);
    }

    public String run(String command, String fetch) {
        if (fetch == null) {
            fetch = "all";
        }
        try {
            Statement stmt = engine.createStatement();
            if (schema != null) {
                // TODO: deal with different dialect
                // stmt.execute("SET search_path TO " + schema);
            }

            ResultSet rs = stmt.executeQuery(command);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<List<Object>> result = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.add(value);
                }
                result.add(row);
                if ("one".equals(fetch)) {
                    break;
                }
            }
            return SnakeJsonUtils.toJson(result);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public String getTableInfo(List<String> tableNames) {
//        List<String> allNames = getUsableTableNames();
//        List<String> allTableNames = allNames;
//        if (tableNames != null) {
//            List<String> missingTables = tableNames.stream()
//                .filter(t -> !allNames.contains(t)).collect(Collectors.toList());
//            if (!missingTables.isEmpty()) {
//                throw new ValueErrorException(String.format("tableNames %s not found in database", missingTables.toString()));
//            }
//            allTableNames = tableNames;
//        }
//
//
//    }

    public static String truncateWord(Object content, int length) {
        return truncateWord(content, length, null);
    }

    public static String truncateWord(Object content, int length, String suffix) {
        if (suffix == null) {
            suffix = "...";
        }
        if (content instanceof String && length > 0) {

        } else {
            return content.toString();
        }

        String contentStr = (String) content;
        if (contentStr.length() <= length) {
            return contentStr;
        }
        return contentStr.substring(length - suffix.length()).split(" ")[0] + suffix;
    }
}
