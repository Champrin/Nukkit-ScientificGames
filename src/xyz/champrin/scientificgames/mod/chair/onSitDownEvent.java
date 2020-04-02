package xyz.champrin.scientificgames.mod.chair;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import xyz.champrin.scientificgames.libs.SGPlayer;

public class onSitDownEvent extends Event implements Cancellable {

    SGPlayer player;

    public onSitDownEvent(SGPlayer player) {
        this.player = player;
        relieveFatigue();
    }

    public void relieveFatigue(){
        player.addEnergy(- 5);
        player.setFatigue(player.getFatigue()*0.2D);
    }
}
