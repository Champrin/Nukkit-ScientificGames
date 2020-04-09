package xyz.champrin.scientificgames.Listenters;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockStone;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityTeleportEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.potion.Effect;
import xyz.champrin.scientificgames.ScientificGames;
import xyz.champrin.scientificgames.libs.SGPlayer;
import xyz.champrin.scientificgames.untils.Burden;

import java.util.Random;

public class Actions implements Listener {

    private ScientificGames plugin = ScientificGames.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnChat(PlayerChatEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            SGPlayer SGplayer = this.plugin.getSGPlayer(event.getPlayer().getName());
            SGplayer.onActions();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnDeath(PlayerDeathEvent event) {
        if (this.plugin.OpenWorld.contains(event.getEntity().getLevel().getFolderName())) {
            this.plugin.getSGPlayer(event.getEntity().getName()).respawn();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (this.plugin.PlayerInC.get(name) == null) {
            this.plugin.newPlayerIn(player);
        } else {
            plugin.reloadPlayerIn(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTp(EntityTeleportEvent event) {
        Entity player = event.getEntity();
        if (player instanceof Player) {
            if (this.plugin.OpenWorld.contains(event.getTo().getLevel().getFolderName()) && this.plugin.OpenWorld.contains(event.getFrom().getLevel().getFolderName())) {

                SGPlayer SGplayer = this.plugin.getSGPlayer(player.getName());
                if (SGplayer.toActFatigue()) {
                    event.setCancelled(true);
                    ((Player) player).sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                }
                SGplayer.addEnergy(-20.0D);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity player = event.getEntity();
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (player instanceof Player && damager instanceof Player) {
                if (this.plugin.OpenWorld.contains(player.getLevel().getFolderName()) && this.plugin.OpenWorld.contains(damager.getLevel().getFolderName())) {
                    SGPlayer SGplayer = this.plugin.getSGPlayer(damager.getName());
                    if (SGplayer.toActFatigue()) {
                        event.setCancelled(true);
                        ((Player) player).sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                    }
                    SGplayer.onActions();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            SGplayer.onActions();
            if (SGplayer.toActFatigue()) {
                event.setCancelled(true);
                player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
            }
            if (!SGplayer.toActEnergy()) {
                player.sendPopup(">  §a你的能量不足,已消耗你生命为代价！你需要补充能量");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(PlayerMoveEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {

            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);

            SGplayer.onActions();
            if (SGplayer.toActFatigue()) {
                event.setCancelled(true);
                player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
            }
            if (!SGplayer.toActEnergy()) {
                event.setCancelled(true);
                player.sendPopup(">  §a你的能量不足,已消耗你生命为代价！你需要补充能量");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);

            Level world = player.getLevel();
            int blockID = world.getBlock(player.floor().subtract(0, 1)).getId();
            double ez = SGplayer.getFood();
            double nl = SGplayer.getEnergy();
            int id = this.plugin.ExcretionBID;
            if (blockID == id) {
                if (nl >= 20.0D) {
                    if (ez >= 18.0D)//判断排泄值是否大于或等于18
                    {
                        SGplayer.setFood(0);//排泄值归零
                        SGplayer.setEnergy(nl - 20.0D);
                        player.sendMessage(">  §b你已排泄,身体一身轻！！");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeld(PlayerItemHeldEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            SGplayer.onActions();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            SGPlayer SGplayer = this.plugin.getSGPlayer(event.getPlayer().getName());
            SGplayer.onActions();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            SGPlayer SGplayer = this.plugin.getSGPlayer(event.getPlayer().getName());
            SGplayer.addBurden(-(new Burden().getItemBurden(event.getItem())));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPickupItem(InventoryPickupItemEvent event) {
        EntityItem item = event.getItem();
        Inventory inventory = event.getInventory();
        if (inventory instanceof PlayerInventory) {
            String name = ((PlayerInventory) inventory).getHolder().getName();
            Player player = plugin.getServer().getPlayer(name);
            if (this.plugin.OpenWorld.contains(player.getLevel().getFolderName())) {
                SGPlayer SGplayer = this.plugin.getSGPlayer(name);
                SGplayer.setBurden(new Burden().addBurden(item.getItem(), SGplayer.getBurden()));
                player.setMovementSpeed(SGplayer.getSpeed());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPMChange(PlayerGameModeChangeEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            SGPlayer SGplayer = this.plugin.getSGPlayer(event.getPlayer().getName());
            if (event.getNewGamemode() == 0) {
                SGplayer.setBurden(-1.0D);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnBreak(BlockBreakEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();

            int item = player.getInventory().getItemInHand().getId();
            Block block = event.getBlock();
            int blockID = block.getId();
            int blockMETA = block.getDamage();
            float health = player.getHealth();
            switch (blockID) {
                case Block.WOOD:
                case Block.WOOD2:
                    if (item == 0) {
                        player.setHealth(health - 1);
                        player.sendMessage(">  §7手撸木头,会伤害手哦~~");
                    }
                    break;
                case Block.CACTUS:
                    if (item == 0) {
                        player.setHealth(health - 1);
                        player.sendMessage(">  §7手撸仙人掌,会伤害手哦~~");
                    }
                    Random random = new Random();
                    int num = random.nextInt(11);
                    block.level.dropItem(block, new Item(Item.POTION, 0, num));
                    player.sendMessage(">  §a你在仙人掌里找到了§e" + num + "§a瓶水~~");
                    break;
                case Block.MELON_BLOCK:
                case Block.TALL_GRASS:
                case Block.DEAD_BUSH:
                case Block.GLASS:
                case Block.GLASS_PANE:
                case Block.STAINED_GLASS:
                case Block.STAINED_GLASS_PANE:
                case Block.VINES:
                case Block.ICE:
                case Block.ICE_FROSTED:
                case Block.PACKED_ICE:
                case Block.LEAVES:
                case Block.LEAVES2:
                    block.level.dropItem(block, new Item(blockID, blockMETA, 1));
                    break;
                case Block.BROWN_MUSHROOM_BLOCK:
                    block.level.dropItem(block, new Item(39, 0, 1));
                    break;
                case Block.RED_MUSHROOM_BLOCK:
                    block.level.dropItem(block, new Item(40, 0, 1));
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            PlayerInventory inventory = player.getInventory();

            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            SGplayer.onActions();

            if (inventory.getItemInHand().getId() == Item.STICK) {
                if (event.getBlock() instanceof BlockStone) {
                    int num = new Random().nextInt(101);
                    if (num <= 30) {
                        inventory.removeItem(new Item(Item.STICK, 0, 1));
                        inventory.addItem(new Item(Item.TORCH, 0, 1));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (packet instanceof PlayerActionPacket) {
            if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
                Player player = event.getPlayer();
                String name = player.getName();
                SGPlayer SGplayer = this.plugin.getSGPlayer(name);
                switch (((PlayerActionPacket) packet).action) {
                    case PlayerActionPacket.ACTION_START_BREAK:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,打破方块将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  §a你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                /*case PlayerActionPacket.ACTION_ABORT_BREAK:
                    break;
                  case PlayerActionPacket.ACTION_CONTINUE_BREAK:
                    break;*/
                    case PlayerActionPacket.ACTION_STOP_BREAK:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,打破方块以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_DROP_ITEM:
                        SGplayer.onActions();
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  §a你太疲劳了,做不了任何事了,你需要休息");
                        }
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,丢弃物品以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_SLEEPING:
                        if (SGplayer.getEnergy() - 120 <= 0) {
                            player.sendMessage(">  §e你的能量过低,现在睡觉可能会在睡梦中死去,请补充能量后再睡觉！");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_SLEEPING:
                        SGplayer.onActions();
                        SGplayer.setFatigue(SGplayer.getFatigue() * 0.2D);
                        double a = SGplayer.getEnergy();
                        double xt = SGplayer.getGlu();
                        if (a - 120 <= 0) {
                            player.sendMessage(">  §e你由于能量过低,已在睡梦中死去,一睡不醒！");
                            SGplayer.killPlayer();
                        } else if (a - 120 <= 60) {
                            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 30).setVisible(true));
                            player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 30).setVisible(true));
                            player.sendTitle("§a你刚起床时能量过低 ", "§e会有短时间眩晕和虚弱效果");
                        }
                        if (xt < 3) {
                            player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 30).setVisible(true));
                            player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 30).setVisible(true));
                            player.sendTitle("§a你刚起床时血糖过低 ", "§e会有短时间眩晕和虚弱效果");
                        }
                        SGplayer.addEnergy(-120);
                        break;
                    case PlayerActionPacket.ACTION_JUMP:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,跳跃以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_SPRINT:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,你的行为将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_SPRINT:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,奔跑以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_SNEAK:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,你的行为将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_SNEAK:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,下蹲以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_GLIDE:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,你的行为将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_GLIDE:
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,滑翔以消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_SWIMMING:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,你的行为将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_SWIMMING:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,游泳消耗你生命为代价！你需要补充能量");
                        }
                        break;
                    case PlayerActionPacket.ACTION_START_SPIN_ATTACK:
                        if (!SGplayer.toActEnergy()) {
                            player.sendPopup(">  §a你的能量太低,你的行为将会导致消耗你的生命,你需要补充能量");
                        }
                        if (SGplayer.toActFatigue()) {
                            event.setCancelled(true);
                            player.sendPopup(">  你太疲劳了,做不了任何事了,你需要休息");
                        }
                        break;
                    case PlayerActionPacket.ACTION_STOP_SPIN_ATTACK:
                        SGplayer.onActions();
                        if (!SGplayer.toActEnergy()) {
                            player.setHealth(player.getHealth() - 1);
                            player.sendPopup(">  §a你的能量不足,已消耗你生命为代价！你需要补充能量");
                        }
                        break;

                }
            }
        }
    }
}
