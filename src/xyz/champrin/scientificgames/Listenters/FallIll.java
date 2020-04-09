package xyz.champrin.scientificgames.Listenters;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import xyz.champrin.scientificgames.ScientificGames;

public class FallIll implements Listener {

    private ScientificGames plugin = ScientificGames.getInstance();

    @EventHandler
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
                        ((Player) entity).sendTitle("§b你被剑砍而感染开放性伤口 \n          §e有流血、虚弱、损伤效果");
                        entity.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                    }
                }
            }
            if (cause.equals(EntityDamageEvent.DamageCause.FALL))//摔
            {
                entity.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                entity.getLevel().addParticle(new DestroyBlockParticle(new Vector3(entity.getX(), entity.getY(), entity.getZ()), Block.get(155)));
                ((Player) entity).sendMessage("§a你腿摔断了 \n          §6需要接骨！");
            }
            if (cause.equals(EntityDamageEvent.DamageCause.DROWNING))//溺水
            {
                entity.addEffect(Effect.getEffect(Effect.FATIGUE).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                entity.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                entity.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                ((Player) entity).sendMessage("§e你溺水了 \n          §c有虚弱,眩晕,疲劳效果！");
            }
            if (cause.equals(EntityDamageEvent.DamageCause.HUNGER)) //饥饿
            {
                entity.addEffect(Effect.getEffect(Effect.HUNGER).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                entity.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(20 * 120).setAmplifier(1).setVisible(true));
                ((Player) entity).sendMessage("§6你的体内的物质不够维持了 \n          §a导致饥饿,并且出现眩晕!");
            }
            if (cause.equals(EntityDamageEvent.DamageCause.LAVA) || cause.equals(EntityDamageEvent.DamageCause.FIRE))//熔岩、烧
            {
                ((Player) entity).sendMessage("§c你浴火纵身 \n          §6直接死亡");
                entity.setHealth(entity.getHealth() - 9);
                entity.setHealth(entity.getHealth() - 8);
                entity.setHealth(entity.getHealth() - 3);
                entity.kill();
            }
            if (cause.equals(EntityDamageEvent.DamageCause.SUFFOCATION))//窒息
            {
                ((Player) entity).sendMessage("§c你窒息了 \n          §6导致无法呼吸,直接死亡！");
                entity.setHealth(entity.getHealth() - 9);
                entity.setHealth(entity.getHealth() - 8);
                entity.setHealth(entity.getHealth() - 3);
                entity.kill();
            }
            if (cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))//方块爆炸(tnt)
            {
                ((Player) entity).sendMessage("§c你被tnt炸碎得粉身碎骨 \n          §6直接死亡！");
                entity.setHealth(entity.getHealth() - 9);
                entity.setHealth(entity.getHealth() - 8);
                entity.setHealth(entity.getHealth() - 3);
                entity.kill();
            }
        }
    }

}
