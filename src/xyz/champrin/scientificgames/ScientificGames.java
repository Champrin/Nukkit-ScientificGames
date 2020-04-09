package xyz.champrin.scientificgames;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import xyz.champrin.scientificgames.Listenters.Actions;
import xyz.champrin.scientificgames.Listenters.FallIll;
import xyz.champrin.scientificgames.Listenters.Foods;
import xyz.champrin.scientificgames.libs.DataManager;
import xyz.champrin.scientificgames.libs.SGPlayer;
import xyz.champrin.scientificgames.mod.Bossbar.BossBar;
import xyz.champrin.scientificgames.mod.season.Season;
import xyz.champrin.scientificgames.mod.temperature.AirTemperature;
import xyz.champrin.scientificgames.mod.temperature.ChangeTemperatureEvent;
import xyz.champrin.scientificgames.mod.time.Time;
import xyz.champrin.scientificgames.mod.weather.ChangeWeatherEvent;
import xyz.champrin.scientificgames.mod.weather.Weather;
import xyz.champrin.scientificgames.untils.MetricsLite;

import java.io.File;
import java.util.*;

public class ScientificGames extends PluginBase implements Listener {

    public Config config, data, PlayerInC, ItemData;
    public List<String> OpenWorld = new ArrayList<>();
    public HashMap<String, SGPlayer> PlayerIn = new HashMap<>();
    public double WaterMax, TemperatureMax, FoodMax, EnergyMax, GluMax, pHMax, AgeMax, FatigueMax, BurdenMax;
    public int ExcretionBID;
    public double energyBufferLimit, waterBufferLimit, fatigueBufferLimit;

    private static ScientificGames instance;

    public static ScientificGames getInstance() {
        return instance;
    }

    public DataManager dataManager;

    public Time timeTask;

    public Weather weather;

    public Season season;

    public AirTemperature airTemperature;

    public BossBar bossBar;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("§f“真实”游戏插件§d§r---§a§lScientificGames§e科学游戏 §f§r加载中");
        this.getLogger().info("§6§lChamprin,Spiderman§r§d开发 §2获取更多信息请加入交流群:499711864哟(๑•̀ω•́๑)");
        new MetricsLite(this);
        if (!new File(this.getDataFolder() + "/config.yml").exists()) {
            this.saveResource("config.yml", false);
        }
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        ConfigUpdate();
        this.WaterMax = this.config.getDouble("water");
        this.waterBufferLimit = this.config.getInt("waterBuffer");
        this.TemperatureMax = this.config.getDouble("temperature");
        this.FoodMax = this.config.getDouble("food");
        this.EnergyMax = this.config.getDouble("energy");
        this.energyBufferLimit = this.config.getInt("energyBuffer");
        this.fatigueBufferLimit = this.config.getInt("fatigueBuffer");
        this.GluMax = this.config.getDouble("glu");
        this.pHMax = this.config.getDouble("pH");
        this.AgeMax = this.config.getDouble("age");
        this.FatigueMax = this.config.getDouble("fatigue");
        this.BurdenMax = this.config.getDouble("burden");


        if (!new File(this.getDataFolder() + "/ItemData.yml").exists()) {
            this.saveResource("ItemData.yml", false);
        }
        this.ItemData = new Config(this.getDataFolder() + "/ItemData.yml", Config.YAML);

        this.PlayerInC = new Config(this.getDataFolder() + "/PlayerIn.yml", Config.YAML);
        this.PlayerInC.save();

        if (!new File(this.getDataFolder() + "/time.yml").exists()) {
            this.saveResource("time.yml", false);
        }
        if (!new File(this.getDataFolder() + "/data.yml").exists()) {
            this.saveResource("data.yml", false);
        }
        this.data = new Config(this.getDataFolder() + "/data.yml", Config.YAML);
        this.dataManager = new DataManager();
        this.timeTask = new Time(data.getInt("year"), data.getInt("month"), data.getInt("day"), data.getInt("minute"), data.getInt("second"));
        this.getServer().getScheduler().scheduleRepeatingTask(this.timeTask, 20);

        if (!new File(this.getDataFolder() + "/data.yml").exists()) {
            this.saveResource("data.yml", false);
        }


        this.getServer().getPluginManager().registerEvents(new FallIll(), this);
        this.getServer().getPluginManager().registerEvents(new Foods(), this);
        this.getServer().getPluginManager().registerEvents(new Actions(), this);
        //this.getServer().getPluginManager().registerEvents(new SitDown(), this);

        //Entity.registerEntity("ChairEntity", ChairEntity.class);

        if (!this.config.get("底部显示格式").equals("tip") && !this.config.get("底部显示格式").equals("pop")) {
            this.getLogger().info("§c底部显示格式未设置或设置错误 §a先已默认为tip格式");
            this.config.set("底部显示格式", "tip");
            this.config.save();
        }
        this.OpenWorld.addAll(this.config.getStringList("worlds"));

        this.bossBar = new BossBar();
        this.weather = new Weather();
        for (Map.Entry<Integer, Level> map : this.getServer().getLevels().entrySet()) {
            weather.spawnWeather(map.getValue());
        }
        this.season = new Season();
        this.airTemperature = new AirTemperature();
        if ((int) this.data.get("airTemperature") == 0) {
            this.getServer().getPluginManager().callEvent(new ChangeTemperatureEvent(new Random().nextInt(6), this.airTemperature));
        }

        this.getLogger().info("§f“真实”游戏插件§d§r---§f§r加载完毕");
    }

    public void ConfigUpdate() {

    }

    @Override
    public void onDisable() {
        for (SGPlayer player : this.PlayerIn.values()) {
            player.saveConfig();
        }
        data.set("year", timeTask.year);
        data.set("month", timeTask.month);
        data.set("day", timeTask.day);
        data.set("minute", timeTask.minute);
        data.set("second", timeTask.second);
        data.set("season", dataManager.getSeason());
        data.set("airTemperature", dataManager.getAirTemperature());
        data.save();
        config.set("worlds", OpenWorld);
        config.save();
    }

    public void newPlayerIn(Player player) {
        LinkedHashMap<String, Object> Map = new LinkedHashMap<>();
        Map.put("water", this.WaterMax);
        Map.put("waterBuffer", 0.00);
        Map.put("energy", this.EnergyMax);
        Map.put("energyBuffer", 0.00);
        Map.put("food", this.FoodMax);
        Map.put("temperature", this.TemperatureMax);
        Map.put("glu", this.GluMax);
        Map.put("pH", this.pHMax);
        Map.put("age", this.AgeMax);
        Map.put("fatigue", this.FatigueMax);
        Map.put("fatigueBuffer", 0.00);
        Map.put("burden", this.BurdenMax);
        String name = player.getName();
        this.PlayerInC.set(name, Map);
        this.PlayerInC.save();
        SGPlayer SGplayer = new SGPlayer(name);
        this.PlayerIn.put(name, SGplayer);
        SGplayer.setBurden(-1.0D);
        SGplayer.RunSchedule();
    }

    public void reloadPlayerIn(Player player) {
        String name = player.getName();
        SGPlayer SGplayer = new SGPlayer(name);
        this.PlayerIn.put(name, SGplayer);
        SGplayer.setBurden(-1.0D);
        SGplayer.RunSchedule();
    }

    public SGPlayer getSGPlayer(String name) {
        return this.PlayerIn.get(name);
    }


    public void ItemData_Check(String itemID, SGPlayer player) {
        LinkedHashMap<String, Object> item = (LinkedHashMap<String, Object>) this.ItemData.getAll().get(itemID);
        double a;
        if (item.get("PH") != null) {
            a = (double) item.get("PH");
            if (player.getpH() + a < pHMax) {
                player.addpH(a);
            } else if (player.getpH() + a <= 0) {
                player.setpH(0);
            } else {
                player.setpH(pHMax);
            }
        }
        if (item.get("Water") != null) {
            a = (double) item.get("Water");
            if (player.getWater() + a < WaterMax) {
                player.addWater(a);
            } else if (player.getpH() + a <= 0) {
                player.setWater(0);
            } else {
                player.setWater(WaterMax);
            }
        }
        if (item.get("Excretion") != null) {
            a = (double) item.get("Food");
            if (player.getFood() + a < FoodMax) {
                player.addFood(a);
            } else if (player.getpH() + a <= 0) {
                player.setFood(0);
            } else {
                player.setFood(FoodMax);
            }
        }
        if (item.get("Energy") != null) {
            a = (double) item.get("Energy");
            if (player.getEnergy() + a < EnergyMax) {
                player.addEnergy(a);
            } else if (player.getpH() + a <= 0) {
                player.setEnergy(0);
            } else {
                player.setEnergy(EnergyMax);
            }
        }
        if (item.get("Temperature") != null) {
            a = (double) item.get("Temperature");
            if (player.getTemperature() + a < TemperatureMax) {
                player.addTemperature(a);
            } else if (player.getpH() + a <= 0) {
                player.setTemperature(0);
            } else {
                player.setTemperature(TemperatureMax);
            }
        }
        if (item.get("Glu") != null) {
            a = (double) item.get("Glu");
            if (player.getGlu() + a < GluMax) {
                player.addGlu(a);
            } else if (player.getpH() + a <= 0) {
                player.setGlu(0);
            } else {
                player.setGlu(GluMax);
            }
        }
        if (item.get("Fatigue") != null) {
            a = (double) item.get("Fatigue");
            if (player.getFatigue() + a < FatigueMax) {
                player.addFatigue(a);
            } else if (player.getpH() + a <= 0) {
                player.setFatigue(0);
            } else {
                player.setFatigue(FatigueMax);
            }
        }

    }

    public boolean Item_Check(String item) {
        return this.ItemData.getAll().get(item) != null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Title = "§l§d科学游戏§f>§r";
        switch (command.getName()) {
            case "ill":
                sender.sendMessage("§f================" + Title + "§f================");
                sender.sendMessage("> 特效药   §b适合症状:§a眩晕  §d治疗方法:§e食用 §6物品:§f苹果 ");
                sender.sendMessage("> 骨头     §b适合症状:§a骨折  §d治疗方法:§e点地 §6物品:§f骨头");
                sender.sendMessage("> 南瓜派   §b适合症状:§a虚弱  §d治疗方法:§e食用 §6物品:§f南瓜派");
                sender.sendMessage("> 士力架   §b适合症状:§a饥饿  §d治疗方法:§e食用 §6物品:§f面包");
                sender.sendMessage("> 曲奇     §b适合症状:§a疲劳  §d治疗方法:§e食用 §6物品:§f曲奇");
                sender.sendMessage("> 维生素A  §b适合症状:§a失明  §d治疗方法:§e食用 §6物品:§f胡萝卜");
                sender.sendMessage("> 云南白药 §b适合症状:§a中毒  §d治疗方法:§e点地 §6物品:§f纸");
                return true;
            case "rlset":
                switch (args[0]) {
                    case "reload":
                        this.ItemData.reload();
                        this.config.reload();
                        sender.sendMessage(Title + "  §f配置重载完成");
                        break;
                    case "setworld":
                        if (args.length > 1) {
                            String level = args[1];
                            if (this.OpenWorld.contains(level)) {
                                sender.sendMessage(Title + "  §a地图§6" + level + "§a已经开启");
                                return false;
                            }
                            this.OpenWorld.add(level);
                            sender.sendMessage(Title + "  §6真实生存开启在世界§a" + level);
                            return true;
                        } else {
                            sender.sendMessage(Title + "  §c未输入要添加的地图名");
                            sender.sendMessage(Title + "  §a用法: /rlset setworld [地图名]");
                            return false;
                        }
                    case "delworld":
                        if (args.length > 1) {
                            String level = args[1];
                            if (this.OpenWorld.contains(level)) {
                                sender.sendMessage(Title + "  §a地图§6" + level + "§a未开启");
                                return false;
                            }
                            this.OpenWorld.remove(level);
                            sender.sendMessage(Title + "  §6真实生存关闭在世界§a" + level);
                            return true;
                        } else {
                            sender.sendMessage(Title + "  §c未输入要删除的地图名");
                            sender.sendMessage(Title + "  §a用法: /rlset delworld [地图名]");
                            return false;
                        }
                    default:
                        sender.sendMessage("============== -=§l§dScientificGames§r§7=- ================");
                        sender.sendMessage("/rlset help                         §8查看帮助");
                        sender.sendMessage("/rlset reload                       §8重载配置文件");
                        sender.sendMessage("/rlset setworld [世界名称]          §3添加§8真实生存世界");
                        sender.sendMessage("/rlset delworld [世界名称]          §3移除§8真实生存世界");
                        break;
                }
                return true;
        }
        return true;
    }

}
