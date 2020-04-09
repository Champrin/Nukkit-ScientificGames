package xyz.champrin.scientificgames.mod.season;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import xyz.champrin.scientificgames.ScientificGames;

public class Season implements Listener {
    public String season;
    public ScientificGames plugin = ScientificGames.getInstance();

    public Season() {
        this.season = ScientificGames.getInstance().dataManager.getSeason();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void Spring() {
    }

    public void Summer() {
    }

    public void Autumn() {
    }

    public void Winter() {
    }

    @EventHandler
    public void onSeasonChange(ChangeSeasonEvent event) {
        this.season = event.newSeason;
    }
}
