package xyz.champrin.Object;

import cn.nukkit.Player;
import cn.nukkit.potion.Effect;
import xyz.champrin.ScientificGames;

import java.util.LinkedHashMap;

public class SGPlayer {

    private String name;//名字
    private Player player;
    private double water;//水量
    private double waterBuffer;//水量缓冲

    private double energy;//能量
    private double energyBuffer;//能量缓冲

    private double food;//食物度

    private double temperature;//体温

    private double glu;//血糖

    private double pH;//体内pH值

    private double age;//年龄

    private double fatigue;//疲劳度

    private double heart_rate;//心率

    private double step_num;//步数
    private String pos;//位置

    private double burden;//负重 TODO

    private double energyBufferLimit;

    private double waterBufferLimit;

    public SGPlayer(String name, LinkedHashMap<String, Object> config) {
        this.name = name;
        this.water = (double) config.get("water");
        this.waterBuffer = (double) config.get("waterBuffer");
        this.energy = (double) config.get("energy");
        this.energyBuffer = (double) config.get("energyBuffer");
        this.food = (double) config.get("food");
        this.temperature = (double) config.get("temperature");
        this.glu = (double) config.get("glu");
        this.pH = (double) config.get("pH");
        this.age = (double) config.get("age");
        this.fatigue = (double) config.get("fatigue");
        this.heart_rate = (double) config.get("heart_rate");
        this.step_num = (double) config.get("step_num");
        this.burden = (double) config.get("burden");

        this.waterBufferLimit = ScientificGames.getInstance().waterBufferLimit;
        this.energyBufferLimit = ScientificGames.getInstance().energyBufferLimit;
        this.player = ScientificGames.getInstance().getServer().getPlayer(name);
    }

    @Override
    public String toString() {
        return "SGPlayer{" +
                "name='" + name + '\'' +
                ", water=" + water +
                ", waterBuffer=" + waterBuffer +
                ", energy=" + energy +
                ", energyBuffer=" + energyBuffer +
                ", food=" + food +
                ", temperature=" + temperature +
                ", glu=" + glu +
                ", pH=" + pH +
                ", age=" + age +
                ", fatigue=" + fatigue +
                ", heart_rate=" + heart_rate +
                ", step_num=" + step_num +
                '}';
    }

    public Object getAllObject() {
        return new Object[]{
                name, water, waterBuffer, energy, energyBuffer, food, temperature, glu, pH, age, fatigue, heart_rate, step_num
        };
    }


    public void toEnergyBuffer() {
        double a = energyBuffer;
        this.energyBuffer = a + 3;
        if (a >= energyBufferLimit) {
            this.energy = energy - 1;
            this.energyBuffer = 0;
        }
    }

    public void toWaterBuffer() {
        double a = waterBuffer;
        this.waterBuffer = a + 1;
        if (a >= waterBufferLimit) {
            this.water = water - 1;
            this.waterBuffer = 0;
        }
    }

    public void onLeaveBed() {
        this.energy = energy - 120;
    }

    public boolean toActionEnergy() {
        if (energy <= 20) {
            return false;
        }
        return true;
    }

    //TODO 体温算法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
        this.CheckStateThirst();
    }

    public double getWaterBuffer() {
        return (double) waterBuffer;
    }

    public void setWaterBuffer(double waterBuffer) {
        this.waterBuffer = waterBuffer;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
        this.CheckStateEnergy();
    }

    public double getEnergyBuffer() {
        return (double) energyBuffer;
    }

    public void setEnergyBuffer(double energyBuffer) {
        this.energyBuffer = energyBuffer;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
        this.CheckStateExcretion();
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getGlu() {
        return glu;
    }

    public void setGlu(double glu) {
        this.glu = glu;
        this.CheckStateGlu();
    }

    public double getpH() {
        return pH;
    }

    public void setpH(double pH) {
        this.pH = pH;
        this.CheckStatepH();
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getFatigue() {
        return fatigue;
    }

    public void setFatigue(double fatigue) {
        this.fatigue = fatigue;
    }

    public double getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(double heart_rate) {
        this.heart_rate = heart_rate;
    }

    public double getStep_num() {
        return step_num;
    }

    public void setStep_num(double step_num) {
        this.step_num = step_num;
    }

    public void intiPos() {
        double x = Math.floor(player.x);
        double y = Math.floor(player.y);
        double z = Math.floor(player.z);

        this.pos = (x + ":" + y + ":" + z);
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public double getBurden() {
        return burden;
    }

    public void setBurden(double burden) {
        this.burden = burden;
    }

    public void CheckStateEnergy() {
        double en = energy;
        if (en <= 100 && en > 45) {
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c需要补充能量", "§a你的能量只有10%了，行动缓慢");
        }
        if (en <= 45 && en > 25) {
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.MINING_FATIGUE).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c需要马上能量", "§a你的能量非常少了，行动缓慢且疲劳");
        }
        if (en <= 25 && en > 5) {
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(3).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.MINING_FATIGUE).setDuration(3).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(3).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c需要立刻补充能量！！", "§a你的能量只有2.5%了，行动缓慢，头晕，失明");
        }
        if (en <= 5) {
            this.player.addEffect(Effect.getEffect(Effect.BLINDNESS).setDuration(3).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c能量告急", "§a你现在必须补充能量,否则将会死亡！！！！");
            this.player.setHealth(this.player.getHealth() - 18);
        }
    }

    public void CheckStateThirst() {
        double w = water;
        if (w <= 25 && w > 15) {
            this.player.sendTitle("§c急需补充水份！！", "§a你的水含量低于25%,身体很虚弱");
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
        }
        if (w <= 15 && w > 7) {
            this.player.sendTitle("§c急需补充水份！！", "§a你现在非常渴");
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.FATIGUE).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
        }
        if (w <= 7 && w > 0) {
            this.player.sendTitle("§c必须要补充水份！！", "§a你的水含量严重不足");
            this.player.addEffect(Effect.getEffect(Effect.BLINDNESS).setDuration(3).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(3).setAmplifier(20 * 120).setVisible(true));
        }
        if (w <= 0) {
            this.player.sendTitle("§c再不补充水份即将死亡！！！", "§a你的水含量已达0");
            this.player.setHealth(this.player.getHealth() - 18);
        }
    }

    public void CheckStateExcretion() {
        double e = food;
        if (e >= 17 && e < 25) {
            this.player.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c你需要马上排泄！！", "§a你现在膀胱要爆了,行走变慢,头晕");
        }
        if (e >= 25) {
            this.player.sendMessage(">  §c你膀胱已经爆了,死亡！！");
            this.player.setHealth(this.player.getHealth() - 6);
            this.player.setHealth(this.player.getHealth() - 8);
            this.player.setHealth(this.player.getHealth() - 9);
            this.player.setHealth(this.player.getHealth() - 6);
            this.player.setHealth(this.player.getHealth() - 8);
            this.player.setHealth(this.player.getHealth() - 9);
        }
    }

    public void CheckStateGlu() {
        double xt = glu;
        double nl = energy;
        if (nl < 180 && xt > 1) {
            this.energy = nl + 250;
            this.glu = xt - 1;
            this.player.sendTitle("§c你的能量不足", "§a已自动将血糖转换为能量");
        }
        if (xt <= 0) {
            this.player.sendMessage(">  §c你的血糖为零,已升天~~");
            this.player.setHealth(this.player.getHealth() - 6);
            this.player.setHealth(this.player.getHealth() - 8);
            this.player.setHealth(this.player.getHealth() - 9);
            this.player.setHealth(this.player.getHealth() - 6);
            this.player.setHealth(this.player.getHealth() - 8);
            this.player.setHealth(this.player.getHealth() - 9);
        }
    }

    public void CheckStatepH() {
        double ph = pH;
        if (ph <= 6.9) {
            this.player.addEffect(Effect.getEffect(Effect.MINING_FATIGUE).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
            this.player.sendTitle("§c你需要补碱性食物！", "§a你的体质显酸性,患上了各类疾病");
        }
    }


}
