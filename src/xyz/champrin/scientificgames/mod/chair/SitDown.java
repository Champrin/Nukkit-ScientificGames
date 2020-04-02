package xyz.champrin.scientificgames.mod.chair;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import xyz.champrin.scientificgames.ScientificGames;

import java.util.ArrayList;
import java.util.Arrays;

public class SitDown implements Listener {

    public ScientificGames plugin = ScientificGames.getInstance();

    public void removeSitEntity(Player player) {
        if (player.namedTag.getLong("Chair") != 0) {
            Level level = player.getLevel();

            Entity chairEntity = level.getEntity(player.namedTag.getLong("Chair"));

            if (chairEntity != null && chairEntity.getName().equals("Chair")) {
                chairEntity.kill();
                chairEntity.close();
            }
            player.namedTag.remove("Chair");
        }
        if (player.namedTag.getBoolean("Click")) {
            player.namedTag.remove("Click");
        }
    }

    public void sitDown(Player player, Block block) {

        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();

        CompoundTag nbt = Entity.getDefaultNBT(new Vector3(x + 0.5, y, z + 0.4), new Vector3(0, 0, 0), 0, 0);

        ChairEntity entity = new ChairEntity(player.chunk, nbt);
        entity.spawnToAll();

        SetEntityLinkPacket pk = new SetEntityLinkPacket();
        pk.vehicleUniqueId = entity.getId();
        pk.riderUniqueId = player.getId();
        pk.type = 2;

        SetEntityLinkPacket finalPk = pk;
        Server.getInstance().getOnlinePlayers().forEach((uuid, p) -> {
            p.dataPacket(finalPk);
        });

        pk = new SetEntityLinkPacket();
        pk.vehicleUniqueId = entity.getId();
        pk.riderUniqueId = 0;
        pk.type = 2;
        SetEntityLinkPacket finalPk1 = pk;
        Server.getInstance().getOnlinePlayers().forEach((uuid, p) -> {
            p.dataPacket(finalPk1);
        });

        removeSitEntity(player);
        player.namedTag.putLong("Chair", entity.getId());

        player.sendTip(">  跳跃/潜行起身  <");
    }

    public ArrayList<Integer> chairId = new ArrayList<>(Arrays.asList(53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180, 203, 26));

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (chairId.contains(block.getId())) {
            if (player.namedTag.getBoolean("Click")) {
                sitDown(player, block);
                plugin.getServer().getPluginManager().callEvent(new onSitDownEvent(plugin.PlayerIn.get(player.getName())));
            } else {
                player.namedTag.putBoolean("Click", true);
                player.sendTip(">  再次点击坐下  <");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeSitEntity(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        removeSitEntity(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        removeSitEntity(player);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        removeSitEntity(player);
    }

    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (packet instanceof PlayerActionPacket) {
            if (((PlayerActionPacket) packet).action == 8) {
                Player player = event.getPlayer();
                removeSitEntity(player);
            }
        }
    }

}