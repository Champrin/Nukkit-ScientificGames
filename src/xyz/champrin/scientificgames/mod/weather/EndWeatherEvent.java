package xyz.champrin.scientificgames.mod.weather;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;

import java.util.LinkedHashMap;

public class EndWeatherEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    public ChangeWeatherEvent main;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public String oldWeather;
    public Level level;

    public EndWeatherEvent(String oldWeather, Level level, ChangeWeatherEvent main) {
        this.main = main;
        this.oldWeather = oldWeather;
        this.level = level;
        finish();
    }

    public String getWeather() {
        return oldWeather;
    }

    public Level getLevel() {
        return level;
    }

    private void finish() {
        level.setRaining(false);
        level.setThundering(false);
        level.setRainTime(12000 * 20);
        level.setThunderTime(12000 * 20);

        if (main.main.isSnow(level)) {
            String[] chunk = ((String)
                    ((LinkedHashMap<String, Object>) main.main.weatherLevel.get(level).get("chunk")).get("pos")).split("-");
            BaseFullChunk chunk1 = level.getChunk(Integer.parseInt(chunk[0]), Integer.parseInt(chunk[1]));
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int id = (int) ((LinkedHashMap<String, Object>) main.main.weatherLevel.get(level).get("chunk")).get(x + "-" + z);
                    chunk1.setBiomeId(x, z, id);
                }
            }
        }
        main.main.weatherLevel.remove(level);

    }
}
