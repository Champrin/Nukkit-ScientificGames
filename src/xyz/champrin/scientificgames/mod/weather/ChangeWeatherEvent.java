package xyz.champrin.scientificgames.mod.weather;

import cn.nukkit.command.Command;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class ChangeWeatherEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();


    public static HandlerList getHandlers() {
        return handlers;
    }

    private String weather;
    private Level level;
    public Weather main;
    public int time;

    public ChangeWeatherEvent(String weather, Level level, Weather main) {
        this.weather = weather;
        this.level = level;
        this.main = main;
        onChange();
    }

    public String getWeather() {
        return weather;
    }

    public Level getLevel() {
        return level;
    }

    private void onChange() {
        LinkedHashMap<String, Object> list = new LinkedHashMap<>();
        switch (weather) {
            case "小雨":
                time = (new Random().nextInt(100) + 100) * 20;
                level.setRaining(true);
                level.setRainTime(time);
                break;
            case "中雨":
                time = (new Random().nextInt(100) + 300) * 20;
                level.setRaining(true);
                level.setRainTime(time);
                break;
            case "大雨":
                time = (new Random().nextInt(100) + 500) * 20;
                level.setRaining(true);
                level.setRainTime(time);
                break;
            case "雷暴雨":
                time = (new Random().nextInt(500) + 120) * 20;
                level.setThundering(true);
                level.setRainTime(time);
                level.setThunderTime(time);
                break;
            case "小雪":
                time = (new Random().nextInt(100) + 100) * 20;
                break;
            case "中雪":
                time = (new Random().nextInt(100) + 300) * 20;
                break;
            case "大雪":
                time = (new Random().nextInt(100) + 500) * 20;
                break;
            default:
                time = 0;
                break;
        }
        list.put("weather", weather);
        list.put("time", time);
        main.weatherLevel.put(level, list);
        this.main.plugin.getServer().getScheduler().scheduleRepeatingTask(new WeatherSchedule(this), 20);

    }


}
