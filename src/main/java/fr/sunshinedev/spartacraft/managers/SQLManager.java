package fr.sunshinedev.spartacraft.managers;


import com.google.gson.JsonObject;
import fr.sunshinedev.spartacraft.SpartaCraft;
import fr.sunshinedev.spartacraft.sql.SCModel;
import org.h2.tools.Server;
import org.reflections.Reflections;

import java.sql.*;
import java.util.*;

public class SQLManager {

    private static Server webServer;
    private Connection connection;
    private List<SCModel> listModels = new ArrayList<>();

    public void connect() {
        try {
            // Chemin vers le fichier de la base de données (ou "jdbc:h2:mem:" pour in-memory)
            String url = "jdbc:h2:./plugins/SpartaCraft/database";
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to H2 database.");
            initializeModels();
            startConsole();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeModels() {
        Reflections reflections = new Reflections("fr.sunshinedev.spartacraft.sql.models");
        Set<Class<? extends SCModel>> classes = reflections.getSubTypesOf(SCModel.class);
        for (Class<? extends SCModel> clazz : classes) {
            try {
                SCModel instance = clazz.getDeclaredConstructor().newInstance();
                SpartaCraft.getInstance().getSqlManager().createTable(instance.getName(), instance.getColumns(), instance.isHasIdPrimary());
                SpartaCraft.getInstance().getLogger().info("Model initilize: " + clazz.getName());
                listModels.add(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void startConsole() {
        try {
            // Démarre la console H2 sur le port 8082
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String mapJavaTypeToSQLType(Class<?> clazz) {
        if (clazz == String.class) {
            return "VARCHAR(255)";
        } else if (clazz == Integer.class || clazz == int.class) {
            return "INT";
        } else if (clazz == Double.class || clazz == double.class) {
            return "DOUBLE";
        } else if (clazz == Float.class || clazz == float.class) {
            return "FLOAT";
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return "BOOLEAN";
        } else if (clazz == java.util.Date.class || clazz == java.sql.Timestamp.class) {
            return "TIMESTAMP";
        } else if (clazz == JsonObject.class) {
            return "JSON";
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + clazz.getName());
        }
    }

    public void createTable(String tableName, LinkedHashMap<String, Class<?>> columns, boolean hasIdPrimary) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        if (hasIdPrimary) {
            sql.append("id integer PRIMARY KEY AUTO_INCREMENT, ");
        }
        for (Map.Entry<String, Class<?>> entry : columns.entrySet()) {
            String columnName = entry.getKey();
            Class<?> columnType = entry.getValue();
            String sqlType = mapJavaTypeToSQLType(columnType);

            sql.append(columnName).append(" ").append(sqlType).append(", ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
            SpartaCraft.getInstance().getLogger().info("Table " + tableName + " created with columns: " + columns.keySet());
        } catch (SQLException e) {
            SpartaCraft.getInstance().getLogger().info("Failed to create table.");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                SpartaCraft.getInstance().getLogger().info("Connection with database closed.");
            }
            if (webServer != null) {
                webServer.stop();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public SCModel getTable(String name) {
        Optional<SCModel> model = listModels.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
        return model.orElse(null);
    }
}
