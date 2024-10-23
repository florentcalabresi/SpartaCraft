package fr.sunshinedev.spartacraft.objects;

public class SCQuest {

    private String uuid;
    private String name;
    private String difficulty;
    private String type;
    private Integer questAmount;
    private String questValue;

    public SCQuest(String uuid, String name, String difficulty, String type, Integer questAmount, String questValue) {
        this.uuid = uuid;
        this.name = name;
        this.difficulty = difficulty;
        this.type = type;
        this.questAmount = questAmount;
        this.questValue = questValue;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getType() {
        return type;
    }

    public Integer getQuestAmount() {
        return questAmount;
    }

    public String getQuestValue() {
        return questValue;
    }
}
