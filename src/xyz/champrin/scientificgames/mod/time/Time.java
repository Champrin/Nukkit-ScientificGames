package xyz.champrin.scientificgames.mod.time;

import cn.nukkit.scheduler.Task;
import xyz.champrin.scientificgames.ScientificGames;
import xyz.champrin.scientificgames.mod.season.ChangeSeasonEvent;
import xyz.champrin.scientificgames.mod.temperature.ChangeTemperatureEvent;
import xyz.champrin.scientificgames.mod.weather.RandomWeather;

public class Time extends Task {
    //我的世界里面的一天等于现实中的20分钟。
    //
    //相关介绍：
    //
    //现实时间是Minecraft中时间流逝的速度的精准的72倍。这是由于现实中1天有1440分钟，而1个完整的Minecraft天持续20分钟。
    //
    //白天是一天周期中最长的一节，历时10分钟。单人游戏中玩家在一个新世界首次出生时，时间总会处于白天的开始(0:00)。大多数多人游戏服务器的时间也是从这个时间开始，但昼夜更替会持续进行，不会受玩家加入影响。
    //
    //
    //
    //扩展资料
    //
    //日落是介于白天和夜晚之间的时间段，持续3分钟。日落时，太阳从西边降落，月亮从东边升起。下落的太阳附近的天空会有红晕。日落时的太阳看上去还会渐渐变大。所有直接暴露在天空之下的、未被其他光源照亮的方块，每10秒钟亮度降低1级。
    //
    //夜晚比白天略短，持续7分钟。夜晚时，月亮会逐渐升到天空的中央，黑蓝色的天空布满了微小而白亮的星星。星星会随着月亮移动，日落即将结束的时候会先于月亮出现。
    public int year;
    public int month;
    public int day;
    public int minute;
    public int second;

    public ScientificGames plugin = ScientificGames.getInstance();

    public Time(int year, int month, int day, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public void onRun(int i) {
        this.second = second + 1;
        if (second >= 60) {
            this.second = 0;
            this.minute = minute + 1;
        } else if (minute >= 8 && minute < 11) {
            plugin.getServer().getPluginManager().callEvent(new onSunsetEvent());
        } else if (minute >= 11 && minute < 20) {
            plugin.getServer().getPluginManager().callEvent(new onNightEvent());
        } else if (minute >= 20) {
            plugin.getServer().getPluginManager().callEvent(new onDayEvent());
            plugin.getServer().getPluginManager().callEvent(new RandomWeather());
            plugin.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent());
            this.minute = 0;
            this.day = day + 1;
        } else if (day >= 30) {
            this.day = 0;
            this.month = month + 1;
            if (this.month % 4 == 0){
                plugin.getServer().getPluginManager().callEvent(new ChangeSeasonEvent());
            }
        } else if (month >= 12) {
            this.month = 0;
            this.year = year + 1;
        }
    }

    public void saveTime() {
        ScientificGames.getInstance().config.set("time", year + "-" + month + "-" + day + "-" + minute + "-" + second);
    }
}
