package xyz.champrin.scientificgames;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import xyz.champrin.scientificgames.Listenters.Actions;
import xyz.champrin.scientificgames.Listenters.FallIll;
import xyz.champrin.scientificgames.Listenters.Foods;
import xyz.champrin.scientificgames.libs.SGPlayer;
import xyz.champrin.scientificgames.untils.Burden;
import xyz.champrin.scientificgames.untils.MetricsLite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ScientificGames extends PluginBase implements Listener {

    public Config config, WorldC, PlayerInC, ItemData;
    public List<String> OpenWorld = new ArrayList<>();
    public HashMap<String, SGPlayer> PlayerIn = new HashMap<>();
    public double WaterMax, TemperatureMax, FoodMax, EnergyMax, GluMax, pHMax, AgeMax, FatigueMax, BurdenMax;
    public int ExcretionBID;
    public double energyBufferLimit, waterBufferLimit, fatigueBufferLimit;

    public String season;

    private static ScientificGames instance;

    public static ScientificGames getInstance() {
        return instance;
    }


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
        //reloadAllPlayerIn();
        this.OpenWorld.addAll(this.config.getStringList("worlds"));
        this.getLogger().info("§f“真实”游戏插件§d§r---§f§r加载完毕");
    }


    @Override
    public void onDisable() {
        savePlayerIn();
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
        SGplayer.setBurden(new Burden().getBurden(player.getInventory()));
        SGplayer.RunSchedule();
    }

    public void reloadPlayerIn(Player player) {
        String name = player.getName();
        SGPlayer SGplayer = new SGPlayer(name);
        this.PlayerIn.put(name, SGplayer);
        SGplayer.setBurden(new Burden().getBurden(player.getInventory()));
        SGplayer.RunSchedule();
    }

    public void savePlayerIn() {
        for (SGPlayer player : this.PlayerIn.values()) {
            player.saveConfig();
        }
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    @SuppressWarnings("unused")
    public void PlayerStateCheckThirst(SGPlayer player) {
        if (player.getWater() <= 15) {
            player.getPlayer().sendMessage("你现在非常渴,不能再吃这些干燥的东西！！");
        }
    }

    @SuppressWarnings("unused")
    public void PlayerStateCheckExcretion(SGPlayer player) {
        if (player.getFood() >= 17) {
            player.getPlayer().sendMessage("你现在膀胱要爆了,不能再吃东西！！你需要先排泄！！");
        }
    }

    @SuppressWarnings("unused")
    public void PlayerStateCheckPH(SGPlayer player) {
        if (player.getpH() <= 7.05) {
            player.getPlayer().sendMessage("你的ph值已经快小于7了,不能再吃酸性食物！！");
        }
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
            }
        }
        if (item.get("Water") != null) {
            a = (double) item.get("Water");
            if (player.getWater() + a < WaterMax) {
                player.addWater(a);
            }
        }
        if (item.get("Excretion") != null) {
            a = (double) item.get("Food");
            if (player.getFood() + a < FoodMax) {
                player.addFood(a);
            }
        }
        if (item.get("Energy") != null) {
            a = (double) item.get("Energy");
            if (player.getEnergy() + a < EnergyMax) {
                player.addEnergy(a);
            }
        }
        if (item.get("Temperature") != null) {
            a = (double) item.get("Temperature");
            if (player.getTemperature() + a < TemperatureMax) {
                player.addTemperature(a);
            }
        }
        if (item.get("Glu") != null) {
            a = (double) item.get("Glu");
            if (player.getGlu() + a < GluMax) {
                player.addGlu(a);
            }
        }
        if (item.get("Fatigue") != null) {
            a = (double) item.get("Fatigue");
            if (player.getFatigue() + a < FatigueMax) {
                player.addFatigue(a);
            }
        }

    }

    public boolean Item_Check(String item) {
        return this.ItemData.getAll().get(item) != null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String Title = "§l§d科学游戏§7>§r";
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
                            if (!this.getServer().isLevelGenerated(level)) {
                                sender.sendMessage(Title + "  §a地图§6" + level + "§a不存在！");
                                return false;
                            }
                            if (this.OpenWorld.contains(level)) {
                                sender.sendMessage(Title + "  §a地图§6" + level + "§a已经开启");
                                return false;
                            }
                            this.OpenWorld.add(level);

                            this.WorldC.setAll((ConfigSection) new LinkedHashMap<String, Object>().put("world", this.OpenWorld.toArray()));
                            this.WorldC.save();
                            sender.sendMessage(Title + "  §6真实生存开启在世界§alevel");
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
                            this.WorldC.setAll((ConfigSection) new LinkedHashMap<String, Object>().put("world", this.OpenWorld.toArray()));
                            this.WorldC.save();
                            sender.sendMessage(Title + "  §6真实生存关闭在世界§alevel");
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
