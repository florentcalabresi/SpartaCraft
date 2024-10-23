package fr.sunshinedev.spartacraft;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerPooper {

    public UUID uuid;
    public List<Long> listTimestamps = new ArrayList<>();
    public BukkitTask handler;

    public PlayerPooper(UUID uuid) {
        this.uuid = uuid;
    }
}
