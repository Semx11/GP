package me.semx11.gravitypvp.gravity;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.semx11.gravitypvp.GravityPvp;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;

public enum GravityConstant {

    VERY_HIGH(4),
    HIGH(2),
    NORMAL(1),
    LOW(0.5),
    VERY_LOW(0.25),
    INVERTED(-1);

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<GravityConstant> VALUES = Collections
            .unmodifiableList(Arrays.asList(values()));

    private static final List<Class<? extends Entity>> ENTITIES = Arrays.asList(ThrownPotion.class,
            FishHook.class, Snowball.class, Egg.class, Arrow.class, ThrownExpBottle.class);
    private static final Map<Class<? extends Entity>, GravityConstant> CURRENT = new HashMap<>();

    private final double value;

    GravityConstant(double value) {
        this.value = value;
    }

    public static void randomize() {
        ENTITIES.forEach(e -> CURRENT.put(e, GravityConstant.randomConstant()));
        GravityPvp.getScoreboard().update();
    }

    public static Map<Class<? extends Entity>, GravityConstant> getCurrent() {
        return CURRENT;
    }

    private static GravityConstant randomConstant() {
        int i = RANDOM.nextInt(VALUES.size());
        return VALUES.get(i);
    }

    public double get() {
        return this.value;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name().toLowerCase().replaceAll("_", " "));
    }

}
