package me.semx11.gravitypvp.event;

import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.util.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EventFallDamage implements Listener {

    private static final EventFallDamage INSTANCE = new EventFallDamage();

    private EventFallDamage() {
    }

    public static EventFallDamage getInstance() {
        return INSTANCE;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (!GravityPvp.getGameState().equals(GameState.RUNNING)) {
            return;
        }
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

}
