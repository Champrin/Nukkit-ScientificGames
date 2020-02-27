package xyz.champrin.Listenters;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityTeleportEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.potion.Effect;
import xyz.champrin.Object.SGPlayer;
import xyz.champrin.ScientificGames;

import java.util.Random;

public class Actions implements Listener {

    private ScientificGames plugin= ScientificGames.getInstance();

    @EventHandler @SuppressWarnings("unused")
    public void OnChat(PlayerChatEvent event) {
        if ( this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName()))
        {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            SGplayer.toEnergyBuffer();
            SGplayer.toWaterBuffer();
            player.sendMessage(SGplayer.toString());
        }

    }

    @EventHandler @SuppressWarnings("unused")
    public void onLeaveBed(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.onLeaveBed();

        double a = SGplayer.getEnergy();
        SGplayer.setEnergy(a-120);
        double xt = SGplayer.getGlu();
        if(a-120 <= 0)
        {
            player.sendMessage(">  §e你由于能量过低,已在睡梦中死去,一睡不醒！");
            player.setHealth(player.getHealth() - 6);
            player.setHealth(player.getHealth() - 8);
            player.setHealth(player.getHealth() - 9);
            player.setHealth(player.getHealth() - 6);
            player.setHealth(player.getHealth() - 8);
            player.setHealth(player.getHealth() - 9);
        }
        if(a-120 <= 60)
        {
            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 30).setVisible(true));
            player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 30).setVisible(true));
            player.sendTitle("§a你刚起床时能量过低 ","§e会有短时间眩晕和虚弱效果");
        }
        if(xt < 3)
        {
            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 30).setVisible(true));
            player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 30).setVisible(true));
            player.sendTitle("§a你刚起床时能量过低 ","§e会有短时间眩晕和虚弱效果");
        }
    }

    @EventHandler @SuppressWarnings("unused")
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();

        int block = player.getLevel().getBlock(player.floor().subtract(0, 1)).getId();

        if (block == 88 || block == 12 || block == 24 || block == 128 ||
                block == 181 || block == 179 || block == 182 || block == 180) {
            SGplayer.setEnergyBuffer(SGplayer.getEnergyBuffer() + 12);
            SGplayer.setWaterBuffer(SGplayer.getWaterBuffer() + 8);
        }
        double x = Math.floor(player.x);
        double y = Math.floor(player.y);
        double z = Math.floor(player.z);
        String pos = SGplayer.getPos();
        double step = Math.floor(SGplayer.getStep_num());
        String[] num = pos.split(":");
        if (x !=  Double.parseDouble(num[0])) {
            double a = Math.abs(x - Double.parseDouble(num[0]));
            step += a;
            SGplayer.setStep_num(Math.floor(step));
        } else if (y != Double.parseDouble(num[1]) && x !=  Double.parseDouble(num[0]) && z != Double.parseDouble(num[2])) {
            double a = Math.abs(y - Double.parseDouble(num[1]));
            step += a;
            SGplayer.setStep_num(Math.floor(step));
        } else if (z != Double.parseDouble(num[2])) {
            double a = Math.abs(z - Double.parseDouble(num[2]));
            step += a;
            SGplayer.setStep_num(Math.floor(step));
        }
        SGplayer.intiPos();
    }

    @EventHandler @SuppressWarnings("unused")
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (!this.plugin.PlayerIn.containsKey(name)){
            this.plugin.newPlayerIn(name);
        }
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.intiPos();
    }
    @EventHandler
    @SuppressWarnings("unused")
    public void onTip(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if ( this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())){
            if (player.getGamemode() != 0) {
                player.setGamemode(0);
                player.sendMessage("§7-=§l§dScientificGames§r§7=-  检测到你不是生存模式,已将你切换为生存模式");
            }
            String t = (String) this.plugin.config.get("底部显示格式");
            if (!t.equals("no")) {
                String send = (String) this.plugin.config.get("Tip");
                String name = player.getName();
                SGPlayer p = this.plugin.PlayerIn.get(name);
                double shl = p.getWater();
                double pxz = p.getFood();
                double nl = p.getEnergy();
                double tw = p.getTemperature();
                double xt = p.getGlu();
                double ph = p.getpH();
                double ss = p.getAge();
                double bs = p.getStep_num();
                double xl = p.getHeart_rate();
                double pld = p.getFatigue();
                double fz = p.getBurden();
                String tp = send.replaceAll("@水含量", String.valueOf(shl));
                tp = tp.replaceAll("@排泄值", String.valueOf(pxz));
                tp = tp.replaceAll("@能量值", String.valueOf(nl));
                tp = tp.replaceAll("@体温值", String.valueOf(tw));
                tp = tp.replaceAll("@血糖", String.valueOf(xt));
                tp = tp.replaceAll("@pH值", String.valueOf(ph));
                tp = tp.replaceAll("@年龄", String.valueOf(ss));
                tp = tp.replaceAll("@步数", String.valueOf(bs));
                tp = tp.replaceAll("@心率", String.valueOf(xl));
                tp = tp.replaceAll("@疲劳度", String.valueOf(pld));
                tp = tp.replaceAll("@负重", String.valueOf(fz));
                if (t.equals("tip")) {
                    player.sendTip(tp);
                } else if (t.equals("pop")) {
                    player.sendPopup(tp);
                }
            }
        }
    }
    @EventHandler @SuppressWarnings("unused")
    public void onTeleport(EntityTeleportEvent event) {
        Entity player = event.getEntity();
        if (player instanceof Player) {
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            SGplayer.intiPos();
        }
    }

    @EventHandler @SuppressWarnings("unused")
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();

        if (!SGplayer.toActionEnergy()) {
            event.setCancelled(true);
            player.sendPopup(">  §a你的能量太低,导致无法丢弃物品,你需要补充能量");
        }
    }

    @EventHandler @SuppressWarnings("unused")
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();

        if (!SGplayer.toActionEnergy()) {
            event.setCancelled(true);
            player.sendPopup(">  §a你的能量太低,导致无法无法放置方块,你需要补充能量");
        }
    }

    @EventHandler @SuppressWarnings("unused")
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();

        if (!SGplayer.toActionEnergy()) {
            event.setCancelled(true);
            player.sendPopup(">  §a你的能量太低,导致无法排泄,你需要补充能量");
        }
        Level world = player.getLevel();
        int blockID = world.getBlock(player.floor().subtract(0, 1)).getId();
        double ez = SGplayer.getFood();
        double nl = SGplayer.getEnergy();
        int id = this.plugin.ExcretionBID;
        if(blockID == id)
        {
            if(nl >= 20)
            {
                if(ez >= 14)//判断排泄值是否大于或等于18
                {
                    SGplayer.setFood(0);//排泄值归零
                    SGplayer.setEnergy(nl-20);
                    player.sendMessage(">  §b你已排泄,身体一身轻！！");
                }
            }
        }
    }

    @EventHandler @SuppressWarnings("unused")
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();
    }

    @EventHandler @SuppressWarnings("unused")
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();
    }


    @EventHandler @SuppressWarnings("unused")
    public void OnBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();


        if (!SGplayer.toActionEnergy()) {
            event.setCancelled(true);
            player.sendPopup("你的能量太低,导致无法打破方块,你需要补充能量");
        }

        int item = player.getInventory().getItemInHand().getId();
        Block block = event.getBlock();
        int blockID = block.getId();
        int blockMETA = block.getDamage();
        float health = player.getHealth();

        if ((event.getBlock() instanceof BlockWood) || (event.getBlock() instanceof BlockWood2)) {
            if (item == 0) {
                player.setHealth(health - 1);
                player.sendMessage("  §7手撸木头,会伤害手哦~~");
            }
        } else if (block instanceof BlockTallGrass) {
            event.setDrops(new Item[]{Item.get(295, 0, 1)});
        } else if (block instanceof BlockDeadBush) {
            event.setDrops(new Item[]{Item.get(280, 0, 1)});
        } else if (block instanceof BlockCactus) {
            if (item == 0) {
                player.setHealth(health - 1);
                player.sendMessage("  §7手撸仙人掌,会伤害手哦~~");
            }
            Random random = new Random();
            int num = random.nextInt(11);
            event.setDrops(new Item[]{Item.get(106, 0, num)});
            player.sendMessage("  §a你在仙人掌里找到了§e{num}§a瓶水~~");
        } else if (block instanceof BlockMelon || block instanceof BlockGlass || block instanceof BlockGlassPane || block instanceof BlockVine ||
                event.getBlock() instanceof BlockIce || block instanceof BlockIcePacked || block instanceof BlockLeaves || event.getBlock() instanceof BlockLeaves2) {
            if (item == 267 || item == 272 || item == 283 || item == 276 || item == 268 || item == 359) {
                event.setDrops(new Item[]{Item.get(blockID, blockMETA, 1)});
            }
        } else if (block instanceof BlockHugeMushroomBrown) {
            event.setDrops(new Item[]{Item.get(39, 0, 1)});
        } else if (block instanceof BlockHugeMushroomRed) {
            event.setDrops(new Item[]{Item.get(40, 0, 1)});
        }

    }

    @EventHandler @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Inventory inventory = player.getInventory();

        SGPlayer SGplayer = this.plugin.getSGPlayer(name);
        SGplayer.toEnergyBuffer();
        SGplayer.toWaterBuffer();

        if (event.getItem().getId() == 280) {
            if ((event.getBlock() instanceof BlockWood) || (event.getBlock() instanceof BlockWood2)) {
                Random random = new Random();
                int num = random.nextInt(101);
                if (num <= 30) {
                    inventory.removeItem(new Item(280, 0, 1));
                    inventory.addItem(new Item(50, 0, 1));
                }
            }
        }

    }

}
