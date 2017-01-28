package me.semx11.gravitypvp.event;

import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.util.GameState;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventPlayerJoin implements Listener {

    private static final EventPlayerJoin INSTANCE = new EventPlayerJoin();

    private EventPlayerJoin() {
    }

    public static EventPlayerJoin getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        int playerCount = Wrapper.getOnlineCount();

        Player p = e.getPlayer();
        e.setJoinMessage(Wrapper.format("&7[&cGP&7]: &7%s &ehas joined (&b%d&e/&b%d&e)!",
                p.getDisplayName(), playerCount, GravityPvp.getMaxPlayers()));
        GravityPvp.getScoreboard().update();

        if (playerCount >= GravityPvp.getMaxPlayers()) {
            GravityPvp.setGameState(GameState.STARTING);
        }
    }

}
