package fr.sunshinedev.spartacraft.objects;

public class SCPlayerQuest {

    private int amount;
    private String qid;

    public SCPlayerQuest(int amount, String qid) {
        this.amount = 0;
        this.qid = qid;
    }

    public int getAmount() {
        return amount;
    }

    public String getQid() {
        return qid;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
