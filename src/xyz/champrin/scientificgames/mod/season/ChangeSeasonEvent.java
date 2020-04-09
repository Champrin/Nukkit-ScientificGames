package xyz.champrin.scientificgames.mod.season;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import xyz.champrin.scientificgames.ScientificGames;

public class ChangeSeasonEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ScientificGames plugin = ScientificGames.getInstance();

    public String oldSeason = plugin.dataManager.getSeason();
    public String newSeason;

    public ChangeSeasonEvent() {
        changeSeason();
    }

    public void changeSeason() {
        switch (oldSeason) {
            case "春":
                newSeason = "夏";
                break;
            case "夏":
                newSeason = "秋";
                break;
            case "秋":
                newSeason = "冬";
                break;
            case "冬":
                newSeason = "春";
                break;
        }
        plugin.dataManager.setSeason(newSeason);
        plugin.dataManager.texts.set(1, "§e当前节: §f" + newSeason);
    }

}
