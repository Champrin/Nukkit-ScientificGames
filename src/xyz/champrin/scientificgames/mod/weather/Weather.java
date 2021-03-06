package xyz.champrin.scientificgames.mod.weather;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.LevelEventPacket;
import xyz.champrin.scientificgames.ScientificGames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;

public class Weather implements Listener {

    public ScientificGames plugin = ScientificGames.getInstance();

    public LinkedHashMap<Level, LinkedHashMap<String, Object>> weatherLevel = new LinkedHashMap<>();

    private final ArrayList<String> weatherList = new ArrayList<>(Arrays.asList("小雨", "中雨", "大雨", "雷暴雨", "小雪", "中雪", "大雪"));

    public Weather() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onWeatherEnd(EndWeatherEvent event) {
        spawnWeather(event.getLevel());
    }

    public void spawnWeather(Level level) {
        plugin.getServer().getPluginManager().callEvent(new ChangeWeatherEvent(randomWeather(), level, this));
    }

    public String randomWeather() {
        return weatherList.get(new Random().nextInt(weatherList.size()));
    }

    public String WeathertoString(Player player) {
        return "§c" + player.getLevel().getName() + "   §f" + ((String) weatherLevel.get(player.getLevel()).get("weather"));
    }

    public boolean isSnow(Level level) {
        if (weatherLevel.containsKey(level)) {
            LinkedHashMap<String, Object> list = weatherLevel.get(level);
            return ((String) list.get("weather")).equals("小雪") ||
                    ((String) list.get("weather")).equals("中雪") ||
                    ((String) list.get("weather")).equals("大雪");
        }
        return false;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        Level level = e.getLevel();
        String chunk = e.getChunk().getX() + "-" + e.getChunk().getZ();
        weatherLevel.get(level).put("chunk", new LinkedHashMap<String, Object>() {{
            put("pos", chunk);
        }});
        if (isSnow(level)) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    ((LinkedHashMap<String, Object>) weatherLevel.get(level).get("chunk")).put(x + "-" + z, e.getChunk().getBiomeId(x, z));
                    e.getChunk().setBiomeId(x, z, 12);
                }
            }
            /*else if (mode == 1) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        e.getChunk().setBiomeId(x, z, 1);
                    }
                }
            }*/
        }
    }


    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (isSnow(e.getLevel())) {
            e.setCancelled();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (isSnow(e.getPlayer().getLevel())) {
            setRaining(e.getPlayer());
        }
    }

    @EventHandler
    public void onLevelChange(EntityLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (isSnow(e.getTarget())) {
                setRaining((Player) e.getEntity());
            }
        }
    }

    private void setRaining(Player p) {
        this.plugin.getServer().getScheduler().scheduleDelayedTask(this.plugin, () -> {
            try {
                LevelEventPacket pk = new LevelEventPacket();
                pk.evid = LevelEventPacket.EVENT_START_RAIN;
                pk.data = Integer.MAX_VALUE;
                p.dataPacket(pk);
            } catch (Exception ignore) {
            }
        }, 20);
    }
//晴：指天空无云或虽有零星的云，但云量占天空不到1/10称为晴，有时天空中出现很高很薄的云，但对透过阳光很少有影响的也称为晴。 [7-8]
//多云：当空中的中、低云的云量占天空面积的4/10～7/10或高空云量占天空面积的6/10或以上时称为能够为多云。
//阴天：凡中、低云的云量占天空面积的8/10及以上是称为阴。阴天是天色阴暗，阳光很少或不能透过云层。
//雾：指近地层空气中悬浮的大量水滴或冰晶微粒的集合体。当这种集合体使水平能见距离降到1000米以下时称为雾；当能见距离降到1000～10000米之间时称为轻雾。
//小雨：4小时降雨量小于10.0毫米或1小时降雨量小于2.5毫米的称为小雨。
//中雨：24小时内的降雨量为10.0～24.9毫米或1小时的降雨量在2.5～8.0毫米之间时成为中雨。
//大雨：24小时降雨量小于25.0～49.9毫米或1小时降雨量达到8.1～15.9毫米时称为大雨。
//暴雨：24小时内的降雨量达到或超过50.0毫米和1小时内的降雨量达到或超过16.0毫米时称为暴雨。降雨量达到100.0～199.9毫米的称为大暴雨；降雨量达到200.0毫米的称为特大暴雨；
//雷阵雨：指伴有雷电现象的阵性降雨，起特点是降雨时间短促，开始和终止都很突然，降水的强度变化大。忽下忽停并伴有电闪雷鸣的阵性降水。
//冰雹：是一种固体降水物。指云层中将下的直径大雨5.0毫米的圆球形或圆锥形冰块，起形状也有不规则的，单体称为雹块，由透明和不透明层相间组成，大的雹块直径可达十九厘米。
//冻雨：雨滴冻结在低于0℃的物体表面的地面上，又称雨淞（由雾滴冻结的，称雾凇），常坠断电线，使路面结冰，影响通信、供电、交通等。
//雨夹雪：指雨滴和湿雪同时降落到地面的降水现象。发生时，近地面的气温略高于0℃，当雪降落到这层空气中，部分雪融化成水滴。
//小雪：指下雪时水平能见度超过1000米的或24小时内的降雪量小于2.5毫米的降雪。
//中雪：指下雪时水平能见度超过500～1000米的或24小时内的降雪量小于2.5～5.0毫米的降雪。
//大-暴雪：指下雪时水平能见度小于500米的或24小时内的降雪量大于5.0毫米的降雪。
//霜冻：在春秋转换季节，白天气温高于0℃，夜晚气温短时间降至0℃以下的低温危害现象。出现时，百叶箱内的气温可不低于0℃；地面或物体表面常出现白霜。
//大风：用风矢表示，有风向杆和风羽组成。风向杆指风的来向，有8个方位。风羽由3，4个短划和三角表示大风的风力，垂直在风向杆末端的右侧（北半球）。
//六级风：指距地面10米高度处的风速为10.8～13.8米/秒的风。
//七级风：指距地面10米高度处的风速为13.9～17.1米/秒的风。
//八～十二级风：指距地面10米高度处的风速分别达到17.2～20.7米/秒、20.8～24.4米/秒、24.5～28.4米/秒、28.5～32.6米/秒、32.7～35.9米/秒时，分别称为八级风、九级风、十级风、十一级风和十二级风。
//台风：指发生在热带海洋上强烈的暖心气旋性涡旋。当中心附近的平均再大风力达到十二级及以上时称为台风。
//雷雨:是空气在极端不稳定状况下，所产生的剧烈天气现象，它常挟带强风、暴雨、闪电、雷击，甚至伴随有冰雹或龙卷风出现，因此往往可造成灾害。
}
