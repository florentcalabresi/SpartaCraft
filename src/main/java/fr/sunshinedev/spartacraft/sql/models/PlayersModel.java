package fr.sunshinedev.spartacraft.sql.models;

import fr.sunshinedev.spartacraft.sql.SCModel;

import java.util.LinkedHashMap;

public class PlayersModel extends SCModel {

    public PlayersModel() {
        super("players", true);
    }

    public LinkedHashMap<String, Class<?>> getColumns() {
        LinkedHashMap<String, Class<?>> columns = new LinkedHashMap<>();
        columns.put("uuid", String.class);

        return columns;
    }
}
