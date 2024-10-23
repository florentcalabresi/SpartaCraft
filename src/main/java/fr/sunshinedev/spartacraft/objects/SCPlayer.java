package fr.sunshinedev.spartacraft.objects;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SCPlayer {

    public UUID uuid;
    public List<Long> listPoopTimestamps = new ArrayList<>();
    public BukkitTask handlerPoop;

    public SCPlayer(UUID uuid) {
        this.uuid = uuid;
    }

}
