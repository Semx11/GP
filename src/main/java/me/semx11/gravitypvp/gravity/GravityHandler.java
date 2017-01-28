package me.semx11.gravitypvp.gravity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class GravityHandler implements Runnable {

    private Map<UUID, Vector> velocities = new HashMap<>();
    private Map<UUID, Boolean> onGround = new HashMap<>();

    private void updateVelocities() {
        this.getEntities().forEach(e -> GravityConstant.getCurrent().entrySet().forEach(entry -> {
            // If gravity is normal, don't modify anything.
            if (!entry.getValue().equals(GravityConstant.NORMAL) && entry.getKey().isInstance(e)) {

                Vector newVec = e.getVelocity().clone();
                UUID uuid = e.getUniqueId();
                // Execute several checks on entities
                if (this.velocities.containsKey(uuid) && this.onGround.containsKey(uuid)
                        && !e.isOnGround() && !e.isInsideVehicle()) {
                    Vector oldVec = this.velocities.get(uuid);
                    if (!this.onGround.get(uuid)) {
                        double dy = oldVec.clone().subtract(newVec).getY();
                        if (dy > 0.0 && (newVec.getY() < -0.01 || newVec.getY() > 0.01)) {
                            // Gravity is set here.
                            newVec.setY(oldVec.getY() - dy * entry.getValue().get());
                            // Check if X should be applied
                            boolean newXChanged = newVec.getX() < -0.001 || newVec.getX() > 0.001;
                            boolean oldXChanged = oldVec.getX() < -0.001 || oldVec.getX() > 0.001;
                            if (newXChanged && oldXChanged) {
                                newVec.setX(oldVec.getX());
                            }
                            // Check if Z should be applied
                            boolean newZChanged = newVec.getZ() < -0.001 || newVec.getZ() > 0.001;
                            boolean oldZChanged = oldVec.getZ() < -0.001 || oldVec.getZ() > 0.001;
                            if (newZChanged && oldZChanged) {
                                newVec.setZ(oldVec.getZ());
                            }
                        }
                    }
                    e.setVelocity(newVec.clone());
                }
                // Store previous values in HashMap
                this.velocities.put(uuid, newVec.clone());
                this.onGround.put(uuid, e.isOnGround());
            }
        }));
    }

    private Stream<Entity> getEntities() {
        return Bukkit.getWorlds().stream().flatMap(w -> w.getEntities().stream());
    }

    @Override
    public void run() {
        this.updateVelocities();
    }

}
