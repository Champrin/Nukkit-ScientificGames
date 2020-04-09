package xyz.champrin.scientificgames.libs;

import xyz.champrin.scientificgames.ScientificGames;

import java.util.*;

public class DataManager {

    public ScientificGames plugin = ScientificGames.getInstance();


    public DataManager() {
        this.season = plugin.data.getString("season");
        this.texts.add("Time");
        this.texts.add("Season");
        this.texts.add("Weather");
        this.texts.add("airTemperature");
        this.texts.set(2, "§e地区天气: §r");
    }

    private String season;

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }


    private int airTemperature;

    public int getAirTemperature() {
        return airTemperature;
    }

    public void setAt(int airTemperature) {
        this.airTemperature = airTemperature;
        this.texts.set(3, "§e地区气温: §f" + airTemperature + "度");
    }

    public ArrayList<String> texts = new ArrayList<>();
    /*- Time:Year:Month:Day:Min:Sec
     * - Season:
     * - Weather: LevelName
     * */
}
