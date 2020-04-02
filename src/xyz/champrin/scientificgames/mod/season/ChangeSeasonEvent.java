package xyz.champrin.scientificgames.mod.season;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import xyz.champrin.scientificgames.ScientificGames;

public class ChangeSeasonEvent extends Event implements Cancellable {

    String oldSeason = ScientificGames.getInstance().getSeason();

    public ChangeSeasonEvent() {
        changeSeason();
    }

    public void changeSeason() {
        switch (oldSeason) {
            case "春季":
                ScientificGames.getInstance().setSeason("夏季");
                break;
            case "夏季":
                ScientificGames.getInstance().setSeason("秋季");
                break;
            case "秋季":
                ScientificGames.getInstance().setSeason("冬季");
                break;
            case "冬季":
                ScientificGames.getInstance().setSeason("春季");
                break;
        }
    }
}
