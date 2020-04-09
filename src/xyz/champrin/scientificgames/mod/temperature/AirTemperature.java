package xyz.champrin.scientificgames.mod.temperature;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import xyz.champrin.scientificgames.ScientificGames;
import xyz.champrin.scientificgames.mod.season.ChangeSeasonEvent;
import xyz.champrin.scientificgames.mod.time.onDayEvent;
import xyz.champrin.scientificgames.mod.time.onNightEvent;
import xyz.champrin.scientificgames.mod.time.onNoonEvent;
import xyz.champrin.scientificgames.mod.weather.ChangeWeatherEvent;
import xyz.champrin.scientificgames.mod.weather.EndWeatherEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class AirTemperature implements Listener {

    public ScientificGames plugin = ScientificGames.getInstance();

    public LinkedHashMap<String, ArrayList<Integer>> weatherMinMax = new LinkedHashMap<>();
    public LinkedHashMap<String, ArrayList<Integer>> seasonMinMax = new LinkedHashMap<>();

    public AirTemperature() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        seasonMinMax.put("春", initMinMax(0, 15));
        seasonMinMax.put("夏", initMinMax(25, 40));
        seasonMinMax.put("秋", initMinMax(15, 20));
        seasonMinMax.put("冬", initMinMax(-10, 10));
    }

    private ArrayList<Integer> initMinMax(int min, int max) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(min);
        list.add(max);
        return list;
    }

    public boolean controlValue(String type, String key, int value) {
        int min, max;
        if (type.equals("weather")) {
            min = weatherMinMax.get(key).get(0);
            max = weatherMinMax.get(key).get(1);
        } else {
            min = seasonMinMax.get(key).get(0);
            max = seasonMinMax.get(key).get(1);
        }
        return value >= min && value <= max;
    }

    public String getSeason() {
        return plugin.dataManager.getSeason();
    }

    public int getAirTemperature() {
        return plugin.dataManager.getAirTemperature();
    }

    //夜晚气温降低
    @EventHandler
    public void onNight(onNightEvent event) {
        int down = new Random().nextInt(7) + 1;
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() - down, this));
    }

    @EventHandler
    public void onDay(onDayEvent event) {
        String newSeason = getSeason();
        int up = new Random().nextInt(seasonMinMax.get(newSeason).get(1) - seasonMinMax.get(newSeason).get(0));
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() + up, this));
    }

    //中午气温升高
    @EventHandler
    public void onNoon(onNoonEvent event) {
        int up = new Random().nextInt(7) + 1;
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() + up, this));
    }

    @EventHandler
    public void onSeasonChange(ChangeSeasonEvent event) {
        String newSeason = event.newSeason;
        int min = weatherMinMax.get(newSeason).get(0);
        int max = weatherMinMax.get(newSeason).get(1);
        int a = new Random().nextInt(max) + min;
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() + a, this));
    }

    @EventHandler
    public void onWeatherChange(ChangeWeatherEvent event) {
        int down = new Random().nextInt(5) + 1;
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() - down, this));
    }

    @EventHandler
    public void onWeatherEnd(EndWeatherEvent event) {
        int up = new Random().nextInt(5) + 1;
        plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(getAirTemperature() + up, this));
    }
}
