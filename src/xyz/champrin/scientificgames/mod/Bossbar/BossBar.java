package xyz.champrin.scientificgames.mod.Bossbar;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import xyz.champrin.scientificgames.ScientificGames;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BossBar implements Listener {

    public ScientificGames plugin = ScientificGames.getInstance();


    public final Map<Player, Long> bossbar = new HashMap<>();
    public final Set<Player> initialized = new HashSet<>();

    public BossBar() {
        this.plugin.getServer().getScheduler().scheduleRepeatingTask(new BossBarSchedule(this), 20);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        if (event.getPacket() instanceof SetLocalPlayerAsInitializedPacket) {
            Player player = event.getPlayer();
            this.bossbar.put(player, this.createBossBar(player));
            this.initialized.add(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.initialized.remove(event.getPlayer());
    }

    public long createBossBar(Player player) {
        return player.createBossBar("§6§l这里是科学游戏——特色生存世界！§r", 50);
    }

}
