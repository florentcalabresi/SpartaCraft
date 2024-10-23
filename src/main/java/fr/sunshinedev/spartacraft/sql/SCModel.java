package fr.sunshinedev.spartacraft.sql;

import fr.sunshinedev.spartacraft.SpartaCraft;
import fr.sunshinedev.spartacraft.objects.SCQuest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class SCModel {

    private final String name;
    private final boolean hasIdPrimary;

    public SCModel(String name, boolean hasIdPrimary) {
        this.name = name;
        this.hasIdPrimary = hasIdPrimary;
    }

    public String getName() {
        return name;
    }

    public boolean isHasIdPrimary() {
        return hasIdPrimary;
    }

    public abstract LinkedHashMap<String, Class<?>> getColumns();


    public PreparedStatement prepareInsert() {
        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder columnsPlaceholder = new StringBuilder();
            getColumns().forEach((column, clazz) -> {
                columns.append(column).append(", ");
            });
            for(int i = 0; i<getColumns().size();i++) {
                columnsPlaceholder.append("?");
                if(!(i == getColumns().size() - 1)) {
                    columnsPlaceholder.append(", ");
                }
            }
            columns.setLength(columns.length() - 2);
            columnsPlaceholder.setLength(columns.length() - 2);
            String insertSQL = "INSERT INTO " + this.name + " ("+columns+") VALUES ("+columnsPlaceholder+")";
            return SpartaCraft.getInstance().getSqlManager().getConnection().prepareStatement(insertSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(HashMap<Integer, Object> values) throws SQLException {
        PreparedStatement preparedStatement = prepareInsert();
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            Integer k = entry.getKey();
            Object v = entry.getValue();
            preparedStatement.setObject(k, v);
        }
        preparedStatement.executeUpdate();
    }

    public List<SCQuest> listRecords() {
        List<SCQuest> records = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + name; // Utilise un SELECT pour récupérer tous les enregistrements
        try {
             PreparedStatement preparedStatement = SpartaCraft.getInstance().getSqlManager().getConnection().prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String entitled = resultSet.getString("entitled");
                String difficulty = resultSet.getString("difficulty");
                String type = resultSet.getString("type");
                Integer questAmount = resultSet.getInt("questAmount");
                String questValue = resultSet.getString("questValue");
                records.add(new SCQuest(uuid, entitled, difficulty, type, questAmount, questValue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
