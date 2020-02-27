package xyz.champrin.Listenters;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import xyz.champrin.ScientificGames;

public class FallIll implements Listener {

    private ScientificGames plugin= ScientificGames.getInstance();

    @EventHandler @SuppressWarnings("unused")
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
            if (entity instanceof Player) {
                EntityDamageEvent.DamageCause cause = event.getCause();
                if (event instanceof EntityDamageByEntityEvent) {
                    Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

                    if (damager instanceof Player) {

                        entity.getLevel().addParticle(new DestroyBlockParticle(new Vector3(entity.getX(), entity.getY(), entity.getZ()), Block.get(152)));//喷血效果

                        int itemID = ((Player) damager).getInventory().getItemInHand().getId();
                        if (itemID == 272 || itemID == 283 || itemID == 276 || itemID == 267 || itemID == 268) {
                            ((Player) entity).sendMessage("§b你被剑砍而感染开放性伤口 \n          §e有流血、虚弱、损伤效果");
                            entity.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                        }
                    }
                }
                if (cause == EntityDamageEvent.DamageCause.FALL)//摔
                {
                    entity.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    entity.getLevel().addParticle(new DestroyBlockParticle(new Vector3(entity.getX(), entity.getY(), entity.getZ()), Block.get(155)));
                    ((Player) entity).sendMessage("§a你腿摔断了 \n          §6需要接骨！");
                }
                if (cause == EntityDamageEvent.DamageCause.DROWNING)//溺水
                {
                    entity.addEffect(Effect.getEffect(Effect.FATIGUE).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    entity.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    entity.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    ((Player) entity).sendMessage("§e你疲劳过度溺水了 \n          §c有虚弱,眩晕,疲劳效果！");
                }
                if (cause == EntityDamageEvent.DamageCause.HUNGER) //饥饿
                {
                    entity.addEffect(Effect.getEffect(Effect.HUNGER).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    entity.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(1).setAmplifier(20 * 120).setVisible(true));
                    ((Player) entity).sendMessage("§6你的血液血糖浓度太低 \n          §a导致饥饿,并且出现眩晕!");
                }
                if (cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.FIRE)//熔岩、烧
                {
                    ((Player) entity).sendMessage("§c你浴火纵身 \n          §6直接死亡");
                    entity.setHealth(entity.getHealth() - 9);
                    entity.setHealth(entity.getHealth() - 8);
                    entity.setHealth(entity.getHealth() - 3);
                    entity.kill();
                }
                if (cause == EntityDamageEvent.DamageCause.SUFFOCATION)//窒息
                {
                    ((Player) entity).sendMessage("§c你窒息了 \n          §6导致无法呼吸,直接死亡！");
                    entity.setHealth(entity.getHealth() - 9);
                    entity.setHealth(entity.getHealth() - 8);
                    entity.setHealth(entity.getHealth() - 3);
                    entity.kill();
                }
                if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)//方块爆炸(tnt)
                {
                    ((Player) entity).sendMessage("§c你被tnt炸碎得粉身碎骨 \n          §6直接死亡！");
                    entity.setHealth(entity.getHealth() - 9);
                    entity.setHealth(entity.getHealth() - 8);
                    entity.setHealth(entity.getHealth() - 3);
                    entity.kill();
                }
            }
        }


    private int high_80 = 0, high_100 = 0, high_120 = 0;
    @EventHandler @SuppressWarnings("unused")
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
            double y = player.getY();
            if (y >= 80 && y <= 100) {
                this.high_80++;
                if (this.high_80 == 5) {
                    player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 120).setVisible(true));
                    player.sendTitle("§b高原反应", " §6开始使你变得虚弱,再高一点甚至会出现眩晕状况!");
                    player.sendTip("§a你已经出现高原反应了！ \n §6不要再往上走了！！");
                }
            }
            if (y >= 100) {
                this.high_100++;
                if (this.high_100 == 5) {
                    player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(3).setAmplifier(20 * 120).setVisible(true));
                    player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(3).setAmplifier(20 * 120).setVisible(true));
                    player.sendTip("§a你的高原反应很严重！ \n §6停止你的动作！！");
                }
            }
            if (y >= 120) {
                this.high_120++;
                if (this.high_120 == 5) {
                    player.sendMessage("§b高原反应\n §6高地极度缺氧让你大量扣血至死亡!");
                    player.kill();
                }
            }
            if (y <= 80) {
                this.high_80 = 0;
                this.high_100 = 0;
                this.high_120 = 0;
            }

    }
}
