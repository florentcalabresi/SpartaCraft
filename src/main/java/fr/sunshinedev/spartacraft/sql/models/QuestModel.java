package fr.sunshinedev.spartacraft.sql.models;

import fr.sunshinedev.spartacraft.sql.SCModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class QuestModel extends SCModel {

    public QuestModel() {
        super("quests", true);
    }

    public LinkedHashMap<String, Class<?>> getColumns() {
        LinkedHashMap<String, Class<?>> columns = new LinkedHashMap<>();
        columns.put("uuid", String.class);
        columns.put("entitled", String.class);
        columns.put("difficulty", String.class);
        columns.put("type", String.class);
        columns.put("questAmount", int.class);
        columns.put("questValue", String.class);

        return columns;
    }

}
