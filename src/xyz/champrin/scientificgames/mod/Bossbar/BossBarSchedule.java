package xyz.champrin.scientificgames.mod.Bossbar;

import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.DummyBossBar;
import xyz.champrin.scientificgames.ScientificGames;

import java.util.List;
import java.util.Map;

public class BossBarSchedule extends Task {

    private BossBar main;

    public ScientificGames plugin = ScientificGames.getInstance();

    public BossBarSchedule(BossBar main) {
        this.main = main;
    }

    private int timer = 0;
    private int index = 0;

    @Override
    public void onRun(int i) {
        List<String> texts = plugin.dataManager.texts;
        if (++timer >= 10) {
            timer = 0;
            if (++index >= texts.size()) {
                index = 0;
            }
        }
        int percentage = 100 - Math.round((float) timer / 10 * 100);
        this.plugin.getServer().getOnlinePlayers().values().forEach((player) -> {
            Map<Long, DummyBossBar> bb = player.getDummyBossBars();
            Long id = main.bossbar.get(player);
            if (bb.containsKey(id)) {
                switch (index) {
                    case 2:
                        player.updateBossBar(texts.get(index) + plugin.weather.WeathertoString(player), percentage, id);
                        break;
                    default:
                        player.updateBossBar(texts.get(index), percentage, id);
                        break;
                }
            } else if (main.initialized.contains(player)) {
                main.bossbar.put(player, main.createBossBar(player));
            }
        });
    }

}
