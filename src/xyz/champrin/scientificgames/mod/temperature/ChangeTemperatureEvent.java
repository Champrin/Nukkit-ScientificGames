package xyz.champrin.scientificgames.mod.temperature;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Level;

public class ChangeTemperatureEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    public AirTemperature main;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int newT;

    public ChangeTemperatureEvent(int newT, AirTemperature main) {
        this.newT = newT;
        this.main = main;

        main.plugin.dataManager.setAt(newT);
    }


}
