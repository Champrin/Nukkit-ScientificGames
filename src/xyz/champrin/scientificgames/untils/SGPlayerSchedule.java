package xyz.champrin.scientificgames.untils;

import cn.nukkit.Player;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import xyz.champrin.scientificgames.libs.SGPlayer;

public class SGPlayerSchedule extends Task {

    private SGPlayer sgPlayer;
    private Player player;
    private int timer;
    private String t ;
    public SGPlayerSchedule(SGPlayer sgPlayer) {
        this.sgPlayer = sgPlayer;
        this.player= sgPlayer.getPlayer();
        this.t = (String) sgPlayer.plugin.config.get("底部显示格式");
    }

    @Override
    public void onRun(int i) {
        this.timer = timer + 1;
        if (sgPlayer.plugin.OpenWorld.contains(player.getLevel().getFolderName())) {
            CheckStateEnergy();
            CheckStateExcretion();
            CheckStateGlu();
            CheckStatepH();
            CheckStateWater();
        }
        if (timer >= 30) {
            sgPlayer.addEnergy(-69);
            sgPlayer.addWater(-1);
            sgPlayer.addFood(0.5);
            this.timer = 0;
        }
        if (!t.equals("no")) {
            String send = (String) sgPlayer.plugin.config.get("Tip");
            double shl = sgPlayer.getWater();
            double pxz = sgPlayer.getFood();
            double nl = sgPlayer.getEnergy();
            //double tw = sgPlayer.getTemperature();
            double xt = sgPlayer.getGlu();
            double ph = sgPlayer.getpH();
            //double ss = sgPlayer.getAge();
            double pld = sgPlayer.getFatigue();
            double fz = sgPlayer.getBurden();
            String tp = send.replaceAll("@水含量", String.format("%.2f",shl));
            tp = tp.replaceAll("@排泄值", String.format("%.2f", pxz));
            tp = tp.replaceAll("@能量值", String.format("%.2f",nl));
            // tp = tp.replaceAll("@体温值", String.format("%.2f",tw));
            tp = tp.replaceAll("@血糖", String.format("%.2f",xt));
            tp = tp.replaceAll("@pH值", String.format("%.2f",ph));
            //tp = tp.replaceAll("@年龄", String.format("%.2f",ss));
            tp = tp.replaceAll("@疲劳度", String.format("%.2f",pld));
            tp = tp.replaceAll("@负重", String.format("%.2f",fz));
            if (t.equals("tip")) {
                player.sendTip(tp+"\n\n");
            } else if (t.equals("pop")) {
                player.sendPopup(tp+"\n\n");
            }
        }
        double y = player.getY();
        if (y >= 80 && y < 100) {
            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(20 * 120).setAmplifier(3).setVisible(true));
            player.sendTitle("§b高原反应", " §6开始使你变得虚弱,再高一点甚至会出现眩晕状况!");
            player.sendTip("§a你已经出现高原反应了！ \n §6不要再往上走了！！");
        }
        if (y >= 100 && y < 120) {
            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(20 * 120).setAmplifier(3).setVisible(true));
            player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(20 * 120).setAmplifier(3).setVisible(true));
            player.sendTip("§a你的高原反应很严重！ \n §6停止你的动作！！");
        }
        if (y >= 120) {
            player.sendMessage("§b高原反应\n §6高地极度缺氧让你大量扣血至死亡!");
            player.kill();
        }
    }

    public void CheckStateEnergy() {
        if (sgPlayer.getEnergy() <= 100 && sgPlayer.getEnergy() > 45) {
            sgPlayer.addEffect( Effect.SLOWNESS, 1, 20 * 120, true);
            player.sendTitle("§c需要补充能量", "§a你的能量只有10%了，行动缓慢");
        } else if (sgPlayer.getEnergy() <= 45 && sgPlayer.getEnergy() > 25) {
            sgPlayer.addEffect( Effect.SLOWNESS, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.MINING_FATIGUE, 2, 20 * 120, true);
            player.sendTitle("§c需要马上能量", "§a你的能量非常少了，行动缓慢且疲劳");
        } else if (sgPlayer.getEnergy() <= 25 && sgPlayer.getEnergy() > 5) {
            sgPlayer.addEffect( Effect.SLOWNESS, 3, 20 * 120, true);
            sgPlayer.addEffect( Effect.MINING_FATIGUE, 3, 20 * 120, true);
            sgPlayer.addEffect( Effect.WEAKNESS, 3, 20 * 120, true);
            player.sendTitle("§c需要立刻补充能量!!", "§a你的能量只有2.5%了，行动缓慢，头晕，失明");
        } else if (sgPlayer.getEnergy() <= 5) {
            sgPlayer.addEffect( Effect.BLINDNESS, 3, 20 * 120, true);
            player.sendTitle("§c你已经没有能量维持你的机体了..", "§6生机正在流失......");
            sgPlayer.killPlayer();
        }
    }

    public void CheckStateWater() {
        if (sgPlayer.getWater() <= 25 && sgPlayer.getWater() > 15) {
            sgPlayer.addEffect( Effect.SLOWNESS, 1, 20 * 120, true);
            sgPlayer.addEffect( Effect.WEAKNESS, 1, 20 * 120, true);
            player.sendTitle("§c急需补充水份!!", "§a你的水含量低于25%,身体很虚弱");
        } else if (sgPlayer.getWater() <= 15 && sgPlayer.getWater() > 7) {
            sgPlayer.addEffect( Effect.SLOWNESS, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.FATIGUE, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.WEAKNESS, 2, 20 * 120, true);
            player.sendTitle("§c急需补充水份!!", "§a你现在非常渴");
        } else if (sgPlayer.getWater() <= 7 && sgPlayer.getWater() > 0) {
            player.sendTitle("§c必须要补充水份!!", "§a你的水含量严重不足");
        } else if (sgPlayer.getWater() <= 0) {
            player.sendTitle("§c你的身体已经脱水完全..", "§6生机正在流失......");
            sgPlayer.killPlayer();
        }
    }

    public void CheckStateExcretion() {
        if (sgPlayer.getFood() >= 17 && sgPlayer.getFood() < 25) {
            sgPlayer.addEffect( Effect.NAUSEA, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.WEAKNESS, 2, 20 * 120, true);
            player.sendTitle("§c你需要马上排泄!!", "§a你现在膀胱要爆了,行走变慢,头晕");
        }
        if (sgPlayer.getFood() >= 25) {
            player.sendTitle(">  §c你膀胱因装不下而爆炸了!", "§6生机正在流失......");
            sgPlayer.killPlayer();
        }
    }

    public void CheckStateGlu() {
        if (sgPlayer.getEnergy() < 180 && sgPlayer.getGlu() > 1) {
            sgPlayer.addEnergy(250);
            sgPlayer.addGlu(-1);
            player.sendTitle("§c你的能量不足", "§a已自动将血糖转换为能量");
        }
        if (sgPlayer.getGlu() <= 0) {
            player.sendTitle(">  §c你的血糖稀罕的到达了零!", "§6生机正在流失......");
            sgPlayer.killPlayer();
        }
    }

    public void CheckStatepH() {
        if (sgPlayer.getpH() <= 6.9) {
            sgPlayer.addEffect( Effect.MINING_FATIGUE, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.NAUSEA, 2, 20 * 120, true);
            sgPlayer.addEffect( Effect.WEAKNESS, 2, 20 * 120, true);
            player.sendTitle("§c你需要补碱性食物!", "§a你的体质显酸性,患上了一些疾病");
        }
    }
}
