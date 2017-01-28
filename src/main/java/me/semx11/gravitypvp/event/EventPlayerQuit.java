package me.semx11.gravitypvp.event;

import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventPlayerQuit implements Listener {

    private static final EventPlayerQuit INSTANCE = new EventPlayerQuit();

    private EventPlayerQuit() {
    }

    public static EventPlayerQuit getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(Wrapper.format("&7[&cGP&7]: &7%s &ehas quit!",
                e.getPlayer().getDisplayName()));
        GravityPvp.getScoreboard().update();
    }

}
