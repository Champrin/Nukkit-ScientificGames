package xyz.champrin.Listenters;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;
import xyz.champrin.Object.SGPlayer;
import xyz.champrin.ScientificGames;

import java.util.Random;

public class Foods implements Listener {

    private ScientificGames plugin = ScientificGames.getInstance();

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = ScientificGames.getInstance().getSGPlayer(name);

        Inventory inventory = player.getInventory();
        // Level level = player.getLevel(); TODO 音效

        int itemID = event.getItem().getId();
        if (this.plugin.Item_Check(itemID)) {
            this.plugin.ItemData_Check(itemID, SGplayer);
        }
        switch (itemID) {

            case 335://牛奶
                inventory.removeItem(new Item(325, 8, 1));
                inventory.addItem(new Item(325, 0, 1));
                break;
            case 326://水桶
                inventory.removeItem(new Item(325, 10, 1));
                inventory.addItem(new Item(325, 0, 1));
                break;
            case 437://肾上腺素 Item:龙息
                player.sendMessage("§a你打了肾上激素 \n  §6加强速度,挖掘速度,力量");
                player.addEffect(Effect.getEffect(Effect.SPEED).setDuration(3).setAmplifier(20 * 120 * 2).setVisible(true));
                player.addEffect(Effect.getEffect(Effect.HASTE).setDuration(3).setAmplifier(20 * 120 * 2).setVisible(true));
                player.addEffect(Effect.getEffect(Effect.STRENGTH).setDuration(3).setAmplifier(20 * 120 * 2).setVisible(true));
                player.getInventory().removeItem(new Item(437, 0, 1));
                break;
            case 352://骨头
                if (player.hasEffect(Effect.SLOWNESS)) {
                    player.removeEffect(Effect.SLOWNESS);
                    inventory.removeItem(new Item(352, 0, 1));
                    player.sendMessage("§6 你已换骨");
                }
                break;
            case 339://纸
                if (player.hasEffect(Effect.POISON)) {
                    player.removeEffect(Effect.POISON);
                    inventory.removeItem(new Item(339, 0, 1));
                    player.sendMessage("§e 你已排毒");
                }
                break;
        }

    }



    @EventHandler
    @SuppressWarnings("unused")
    public void onHeld(PlayerItemHeldEvent event) {
        //TODO 手持提示该食物的属性 如ph water energy等
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = ScientificGames.getInstance().getSGPlayer(name);

        int itemID = event.getItem().getId();
        if (itemID != 339 && itemID != 352) {
            SGplayer.CheckStateExcretion();
            SGplayer.CheckStateThirst();
            SGplayer.CheckStatepH();
        }
        switch (itemID) {
            case 373://水瓶
            case 335://牛奶
            case 329://水桶
                if (SGplayer.getWater() >= ScientificGames.getInstance().WaterMax) {
                    player.sendMessage(">  §a你现在不渴！不需要补充水份哟！!");
                }
                break;
            case 260://苹果
                player.sendMessage("§a>§3  特效药 §b适合症状:眩晕 §d治疗方法：食用 §6物品：苹果");
                break;
            case 400: //南瓜派
                player.sendMessage("§a>§f  南瓜派 §b适合症状:虚弱 §d治疗方法：点地 §6物品：南瓜派");
                break;
            case 297: //面包
                player.sendMessage("§a>§e  士力架 §b适合症状:饥饿 §d治疗方法：食用 §6物品：面包");
                break;
            case 357://曲奇
                player.sendMessage("§a>§9  曲奇 §b适合症状:疲劳 §d治疗方法：食用 §6物品：曲奇");
                break;
            case 391://胡萝卜
                player.sendMessage("§a>§b  维生素A §b适合症状:失明 §d治疗方法：食用 §6物品：胡萝卜");
                break;
            case 339://纸
                player.sendMessage("§a>§c  云南白药§b适合症状:中毒 §d治疗方法：点地  §6物品：纸");
                break;
            case 437://龙息
                player.sendMessage("§a>§6  肾上腺素 §b加强速度,挖掘速度,力量");
                break;
            case 352://骨头
                player.sendMessage("§a>§6  骨头 §b适合症状:骨折 §d治疗方法：点地 §6物品：骨头");
                break;
            case 322://金苹果
            case 396://金萝卜
            case 382://金西瓜
                player.sendMessage("§a你想磕掉呀吗2333");
                break;
            case 411://生兔
            case 319://生猪
            case 365://生鸡
            case 423://生羊
            case 349://生鱼
            case 363://生牛
                player.sendMessage("§a食用生肉可能会中毒哦~~");
                break;
        }

    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SGPlayer SGplayer = ScientificGames.getInstance().getSGPlayer(name);

        int itemID = event.getItem().getId();

        if (this.plugin.Item_Check(itemID)) {
            this.plugin.ItemData_Check(itemID, SGplayer);
        }
        switch (itemID) {
            case 260: //苹果---特效药
                if (player.hasEffect(Effect.NAUSEA)) {
                    player.removeEffect(Effect.NAUSEA);
                    player.sendMessage("  §a已缓解头晕！");
                }
                break;
            case 297://面包---士力架
                if (player.hasEffect(Effect.HUNGER)) {
                    player.removeEffect(Effect.HUNGER);
                    player.sendMessage("  §e横扫饥饿，做回自己！");
                }
                break;
            case 357://曲奇
                if (player.hasEffect(Effect.FATIGUE)) {
                    player.removeEffect(Effect.FATIGUE);
                    player.sendMessage("  §2已缓解疲劳！");
                }
                break;
            case 400://南瓜派
                if (player.hasEffect(Effect.WEAKNESS)) {
                    player.removeEffect(Effect.WEAKNESS);
                    player.sendMessage("  §6强身健体");
                }
                break;
            case 391://胡萝卜---维生素A
                if (player.hasEffect(Effect.BLINDNESS)) {
                    player.removeEffect(Effect.BLINDNESS);
                    player.sendMessage("  §3成功治疗失明！");
                }
                break;
            case 392://马铃薯--中毒
                player.addEffect(Effect.getEffect(Effect.POISON).setDuration(3).setAmplifier(20 * 120 * 2).setVisible(true));
                player.sendMessage("  §e你中毒了 \n  §a竟然敢生吃马铃薯???");
                break;
            case 322://金苹果
            case 396://金萝卜
            case 382://金西瓜
                player.setHealth(player.getHealth() - 13);
                player.sendMessage("  §e你的牙被磕掉,造成大量流血 \n  §a因为你食用了金制食物");
                break;
            case 319://生猪肉
            case 365://生鸡肉
            case 423://生羊肉
            case 363://生牛肉
            case 411://生兔肉
            case 349://生鱼肉
                Random random = new Random();
                int num = random.nextInt(101);
                if (num <= 30) {
                    player.addEffect(Effect.getEffect(Effect.POISON).setDuration(2).setAmplifier(20 * 120).setVisible(true));
                    player.addEffect(Effect.getEffect(Effect.WEAKNESS).setDuration(2).setAmplifier(20 * 120).setVisible(true));
                    player.addEffect(Effect.getEffect(Effect.NAUSEA).setDuration(2).setAmplifier(20 * 120).setVisible(true));
                    player.sendMessage("  §c你吃了带有细菌的生肉 \n  §a所以你生病了~");
                }
                break;
        }

    }

}
