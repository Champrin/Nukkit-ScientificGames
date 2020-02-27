package xyz.champrin;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import xyz.champrin.Listenters.Actions;
import xyz.champrin.Listenters.FallIll;
import xyz.champrin.Listenters.Foods;
import xyz.champrin.Object.SGPlayer;

import java.io.File;
import java.util.*;

public class ScientificGames extends PluginBase {

    //TODO 排泄系统完善 当水量到达一定时可以进行排泄 如水量超过一定限度 将会受到负面影响
    //TODO 疲劳度功能/*过度疲劳后的行为无效 如何恢复？---坐下，睡觉*/
    //TODO 心率、负重算法


    public Config config, WorldC, PlayerInC, ItemData;
    public List<String> OpenWorld = new ArrayList<>();
    public HashMap<String, SGPlayer> PlayerIn = new HashMap<>();
    public double WaterMax, TemperatureMax, FoodMax, EnergyMax, GluMax, pHMax, AgeMax, FatigueMax, Heart_rateMax, Step_numMax, BurdenMax;
    public int ExcretionBID;
    public double energyBufferLimit, waterBufferLimit;


    private static ScientificGames instance;

    public static ScientificGames getInstance() {
        return instance;
    }


    @Override
    public void onLoad() {
        instance = this;
        this.getLogger().info("§f“真实”游戏插件§d§r---§a§lScientificGames§e科学游戏 §f§r加载中");
        this.getLogger().info("§6§lChamprin§r§d开发 §2获取更多信息请加入交流群哟(๑•̀ω•́๑)");
    }

    @Override @SuppressWarnings("unchecked")
    public void onEnable() {

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
        this.GluMax = this.config.getDouble("glu");
        this.pHMax = this.config.getDouble("pH");
        this.AgeMax = this.config.getDouble("age");
        this.FatigueMax = this.config.getDouble("fatigue");
        this.Heart_rateMax = this.config.getDouble("heart_rate");
        this.Step_numMax = this.config.getInt("step_num");
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

        if (!this.config.get("底部显示格式").equals("tip") && !this.config.get("底部显示格式").equals("pop")) {
            this.getLogger().info("§c底部显示格式未设置或设置错误 §a先已默认为tip格式");
            this.config.set("底部显示格式", "tip");
            this.config.save();
        }

        this.OpenWorld.addAll((Collection<? extends String>) this.config.get("worlds"));
        reloadPlayerIn();
        double x = 516.45;
        double y = 5.88;
        double z = 56.4;
        String pos = (x + ":" + y + ":" + z).toString();
        String[] num = pos.split(":");
        double a = Double.parseDouble(num[0]);
        System.out.println(a);
    }

    @Override
    public void onDisable() {
        savePlayerIn();
    }

    public void newPlayerIn(String name) {
        LinkedHashMap<String, Object> Map = new LinkedHashMap<>();
        Map.put("water", this.WaterMax);
        Map.put("waterBuffer", this.waterBufferLimit);
        Map.put("energy", this.EnergyMax);
        Map.put("energyBuffer", this.energyBufferLimit);
        Map.put("food", this.FoodMax);
        Map.put("temperature", this.TemperatureMax);
        Map.put("glu", this.GluMax);
        Map.put("pH", this.pHMax);
        Map.put("age", this.AgeMax);
        Map.put("fatigue", this.FatigueMax);
        Map.put("heart_rate", this.Heart_rateMax);
        Map.put("step_num", this.Step_numMax);
        Map.put("burden", this.BurdenMax);
        this.PlayerInC.set(name, Map);
        this.PlayerInC.save();
        this.PlayerIn.put(name, new SGPlayer(name,Map));
    }
    @SuppressWarnings("unchecked")
    public void reloadPlayerIn() {
        for (Map.Entry<String, Object> entry : this.PlayerInC.getAll().entrySet()) {
            LinkedHashMap<String,Object> map = (LinkedHashMap<String, Object>) entry.getValue();
            LinkedHashMap<String, Object> Map = new LinkedHashMap<>();
            Map.put("water",map.get("water"));
            Map.put("waterBuffer", map.get("waterBuffer"));
            Map.put("energy", map.get("energy"));
            Map.put("energyBuffer", map.get("energyBuffer"));
            Map.put("food", map.get("food"));
            Map.put("temperature", map.get("temperature"));
            Map.put("glu", map.get("glu"));
            Map.put("pH", map.get("pH"));
            Map.put("age", map.get("age"));
            Map.put("fatigue", map.get("fatigue"));
            Map.put("heart_rate", map.get("heart_rate"));
            Map.put("step_num", map.get("step_num"));
            Map.put("burden", map.get("burden"));
            this.PlayerIn.put(entry.getKey(), new SGPlayer(entry.getKey(), Map));
        }
    }

    public void savePlayerIn() {
        for (SGPlayer player : this.PlayerIn.values()) {
            LinkedHashMap<String, Object> Map = new LinkedHashMap<>();
            Map.put("water", player.getWater());
            Map.put("waterBuffer", player.getWaterBuffer());
            Map.put("energy", player.getEnergy());
            Map.put("energyBuffer", player.getEnergyBuffer());
            Map.put("food", player.getFood());
            Map.put("temperature", player.getTemperature());
            Map.put("glu", player.getGlu());
            Map.put("pH", player.getpH());
            Map.put("age", player.getAge());
            Map.put("fatigue", player.getFatigue());
            Map.put("heart_rate", player.getHeart_rate());
            Map.put("step_num", player.getStep_num());
            Map.put("burden", player.getBurden());
            this.PlayerInC.set(player.getName(), Map);
            this.PlayerInC.save();
        }
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


    @SuppressWarnings("unchecked")
    public void ItemData_Check(int itemID,SGPlayer player)
     {
         Map<String, Object> map = this.ItemData.getAll();
         LinkedHashMap<String,Object> item = (LinkedHashMap<String, Object>) map.get(Integer.toString(itemID));
         if (item.get("PH") != null) {
             player.setpH(player.getpH() + Double.parseDouble(item.get("PH").toString()));
         }
         if (item.get("Water") != null) {
             player.setWater(player.getWater() + Double.parseDouble(item.get("Water").toString()));
         }
         if (item.get("Excretion") != null) {
             player.setFood(player.getFood() + Double.parseDouble(item.get("Excretion").toString()));
         }
         if (item.get("Energy") != null) {
             player.setEnergy(player.getEnergy() + Double.parseDouble(item.get("Energy").toString()));
         }
         if (item.get("Temperature") != null) {
             player.setTemperature(player.getTemperature() + Double.parseDouble(item.get("Temperature").toString()));
         }
         if (item.get("Glu") != null) {
             player.setGlu(player.getGlu() + Double.parseDouble(item.get("Glu").toString()));
         }
         if (item.get("Fatigue") != null) {
             player.setFatigue(player.getFatigue() + Double.parseDouble(item.get("Fatigue").toString()));
         }
         if (item.get("Burden") != null) {
             player.setBurden(player.getBurden() + Double.parseDouble(item.get("Burden").toString()));
         }

     }

    public boolean Item_Check(int itemId){
        return this.ItemData.getAll().get(Integer.toString(itemId)) != null;
    }
/*TODO 计时器
 public function onRun(int CK)
	{
	    this.times++;
        if(in_array(this.player.getLevel().getFolderName(),this.world.get("worlds")))
        {
            name = this.player.getName();
            this.PlayerStateCheckThirst(name);
            this.PlayerStateCheckEnergy(name);
            this.PlayerStateCheckExcretion(name);
            this.PlayerStateCheckGlu(name);
            this.PlayerStateCheckpH(name);
            if(this.times >= 72){
                this.setEnergy(name,this.getEnergy(name)-69);
                this.setWater(name,this.getWater(name)-1);
                this.setExcretion(name,this.getExcretion(name)+0.5);
                this.times = 0;
            }
        }
    }
*/

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
