package xyz.champrin.scientificgames.Listenters;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;
import xyz.champrin.scientificgames.ScientificGames;
import xyz.champrin.scientificgames.libs.SGPlayer;

import java.util.Random;

public class Foods implements Listener {

    private ScientificGames plugin = ScientificGames.getInstance();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            PlayerInventory inventory = player.getInventory();
            Item item = inventory.getItemInHand();
            int itemID = item.getId();
            int DataID = item.getDamage();
            String itemData = itemID + "-" + DataID;
            if (this.plugin.Item_Check(itemData)) {
                System.out.println("true");
                this.plugin.ItemData_Check(itemData, SGplayer);
            }
            switch (itemID) {
                case Item.BUCKET:
                    switch (DataID) {
                        case 8://牛奶
                            inventory.removeItem(new Item(325, 8, 1));
                            inventory.addItem(new Item(325, 0, 1));
                            break;
                        case 10://水桶
                            inventory.removeItem(new Item(325, 10, 1));
                            inventory.addItem(new Item(325, 0, 1));
                            break;
                    }
                    break;
                case Item.DRAGON_BREATH://肾上腺素 Item:龙息
                    player.sendMessage("§a你打了肾上激素 \n  §6加强速度,挖掘速度,力量");
                    SGplayer.addEffect(Effect.SPEED, 3, 20 * 120 * 2, true);
                    SGplayer.addEffect(Effect.HASTE, 3, 20 * 120 * 2, true);
                    SGplayer.addEffect(Effect.STRENGTH, 3, 20 * 120 * 2, true);
                    player.getInventory().removeItem(new Item(Item.DRAGON_BREATH, 0, 1));
                    break;
                case Item.BONE://骨头
                    if (player.hasEffect(Effect.SLOWNESS)) {
                        player.removeEffect(Effect.SLOWNESS);
                        inventory.removeItem(new Item(Item.BONE, 0, 1));
                        player.sendMessage("§6 你已换骨");
                    }
                    break;
                case Item.PAPER://纸
                    if (player.hasEffect(Effect.POISON)) {
                        player.removeEffect(Effect.POISON);
                        inventory.removeItem(new Item(Item.PAPER, 0, 1));
                        player.sendMessage("§e 你已排毒");
                    }
                    break;
            }
        }
    }


    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            //TODO 手持提示该食物的属性 如ph water energy等
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);
            PlayerInventory inventory = player.getInventory();
            Item item = inventory.getItemInHand();
            int itemID = item.getId();
            int DataID = item.getDamage();
            if (itemID != Item.PAPER && itemID != Item.BONE) {
                plugin.PlayerStateCheckExcretion(SGplayer);
                plugin.PlayerStateCheckPH(SGplayer);
                plugin.PlayerStateCheckThirst(SGplayer);
            }
            switch (itemID) {
                case Item.POTION://水瓶
                    if (SGplayer.getWater() >= this.plugin.WaterMax) {
                        player.sendMessage(">  §a你现在不渴！不需要补充水份哟！!");
                    }
                    break;
                case Item.BUCKET://水桶
                    if (DataID == 8 || DataID == 10) {
                        if (SGplayer.getWater() >= this.plugin.WaterMax) {
                            player.sendMessage(">  §a你现在不渴！不需要补充水份哟！!");
                        }
                    }
                    break;
                case Item.APPLE://苹果
                    player.sendMessage("§a>§3  特效药 §b适合症状:眩晕 §d治疗方法：食用 §6物品：苹果");
                    break;
                case Item.PUMPKIN_PIE: //南瓜派
                    player.sendMessage("§a>§f  南瓜派 §b适合症状:虚弱 §d治疗方法：点地 §6物品：南瓜派");
                    break;
                case Item.BREAD: //面包
                    player.sendMessage("§a>§e  士力架 §b适合症状:饥饿 §d治疗方法：食用 §6物品：面包");
                    break;
                case Item.COOKIE://曲奇
                    player.sendMessage("§a>§9  曲奇 §b适合症状:疲劳 §d治疗方法：食用 §6物品：曲奇");
                    break;
                case Item.CARROTS://胡萝卜
                    player.sendMessage("§a>§b  维生素A §b适合症状:失明 §d治疗方法：食用 §6物品：胡萝卜");
                    break;
                case Item.PAPER://纸
                    player.sendMessage("§a>§c  云南白药§b适合症状:中毒 §d治疗方法：点地  §6物品：纸");
                    break;
                case Item.DRAGON_BREATH://龙息
                    player.sendMessage("§a>§6  肾上腺素 §b加强速度,挖掘速度,力量");
                    break;
                case Item.BONE://骨头
                    player.sendMessage("§a>§6  骨头 §b适合症状:骨折 §d治疗方法：点地 §6物品：骨头");
                    break;
                case Item.GOLDEN_APPLE://金苹果
                case Item.GOLDEN_APPLE_ENCHANTED:
                case Item.GOLDEN_CARROT://金萝卜
                case Item.GLISTERING_MELON://金西瓜
                    player.sendMessage(">  §a你想磕掉呀吗2333");
                    break;
                case Item.RAW_RABBIT://生兔
                case Item.RAW_PORKCHOP://生猪
                case Item.RAW_CHICKEN://生鸡
                case Item.RAW_MUTTON://生羊
                case Item.RAW_FISH://生鱼
                case Item.RAW_SALMON:
                case Item.RAW_BEEF://生牛
                    player.sendMessage(">  §a食用生肉可能会中毒哦~~");
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (this.plugin.OpenWorld.contains(event.getPlayer().getLevel().getFolderName())) {
            Player player = event.getPlayer();
            String name = player.getName();
            SGPlayer SGplayer = this.plugin.getSGPlayer(name);

            Item item = event.getItem();
            int itemID = item.getId();
            int damage = item.getDamage();
            String itemData = itemID + "-" + damage;
            if (this.plugin.Item_Check(itemData)) {
                System.out.println("true");
                this.plugin.ItemData_Check(itemData, SGplayer);
            }
            switch (itemID) {
                case Item.APPLE: //苹果---特效药
                    if (player.hasEffect(Effect.NAUSEA)) {
                        player.removeEffect(Effect.NAUSEA);
                        player.sendMessage("  §a已缓解头晕！");
                    }
                    break;
                case Item.PUMPKIN_PIE://面包---士力架
                    if (player.hasEffect(Effect.HUNGER)) {
                        player.removeEffect(Effect.HUNGER);
                        player.sendMessage("  §e横扫饥饿，做回自己！");
                    }
                    break;
                case Item.BREAD://曲奇
                    if (player.hasEffect(Effect.FATIGUE)) {
                        player.removeEffect(Effect.FATIGUE);
                        player.sendMessage("  §2已缓解疲劳！");
                    }
                    break;
                case Item.COOKIE://南瓜派
                    if (player.hasEffect(Effect.WEAKNESS)) {
                        player.removeEffect(Effect.WEAKNESS);
                        player.sendMessage("  §6强身健体");
                    }
                    break;
                case Item.CARROTS://胡萝卜---维生素A
                    if (player.hasEffect(Effect.BLINDNESS)) {
                        player.removeEffect(Effect.BLINDNESS);
                        player.sendMessage("  §3成功治疗失明！");
                    }
                    break;
                case Item.POTATO://马铃薯--中毒
                    SGplayer.addEffect(Effect.POISON, 3, 20 * 120 * 2, true);
                    player.sendMessage(">  §e你中毒了 \n  §a竟然敢生吃马铃薯???");
                    break;
                case Item.GOLDEN_APPLE://金苹果
                case Item.GOLDEN_APPLE_ENCHANTED:
                case Item.GOLDEN_CARROT://金萝卜
                case Item.GLISTERING_MELON://金西瓜
                    player.setHealth(player.getHealth() - 13);
                    player.sendMessage("  §e你的牙被磕掉,造成大量流血 \n  §a因为你食用了金制食物");
                    break;
                case Item.RAW_RABBIT://生兔
                case Item.RAW_PORKCHOP://生猪
                case Item.RAW_CHICKEN://生鸡
                case Item.RAW_MUTTON://生羊
                case Item.RAW_FISH://生鱼
                case Item.RAW_SALMON:
                case Item.RAW_BEEF://生牛
                    Random random = new Random();
                    int num = random.nextInt(101);
                    if (num <= 66) {
                        SGplayer.addEffect(Effect.POISON, 2, 20 * 120, true);
                        SGplayer.addEffect(Effect.WEAKNESS, 2, 20 * 120, true);
                        SGplayer.addEffect(Effect.NAUSEA, 2, 20 * 120, true);
                        player.sendMessage(">  §c你吃了带有细菌的生肉 \n  §a所以你生病了~");
                    }
                    break;
            }
        }
    }

}
