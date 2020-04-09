package xyz.champrin.scientificgames.mod.weather;

import cn.nukkit.scheduler.Task;
import xyz.champrin.scientificgames.ScientificGames;


public class WeatherSchedule extends Task {

    private int time;
    public ChangeWeatherEvent main;

    public ScientificGames plugin = ScientificGames.getInstance();

    public WeatherSchedule(ChangeWeatherEvent main) {
        this.main = main;
        this.time = main.time;
    }

    @Override
    public void onRun(int i) {
        this.time = time - 1;
        if (this.time <= 0) {
            plugin.getServer().getPluginManager().callEvent(new EndWeatherEvent(main.getWeather(), main.getLevel(), main));
            cancel();
        }
    }

}
