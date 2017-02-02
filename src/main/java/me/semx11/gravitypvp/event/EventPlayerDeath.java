package me.semx11.gravitypvp.event;

import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EventPlayerDeath implements Listener {

    private static final EventPlayerDeath INSTANCE = new EventPlayerDeath();

    private EventPlayerDeath() {
    }

    public static EventPlayerDeath getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(Wrapper.format("&7[&cGP&7]: &e" + e.getDeathMessage()));
        // Kill counting soon..
    }

}
