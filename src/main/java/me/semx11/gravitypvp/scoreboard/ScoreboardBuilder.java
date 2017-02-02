package me.semx11.gravitypvp.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

class ScoreboardBuilder {

    private static Map<Objective, List<String>> oldLines = new HashMap<>();

    private final Scoreboard scoreboard;
    private final Objective objective;

    private List<String> lines = new ArrayList<>();
    private int emptyLines = 0;

    ScoreboardBuilder(Objective objective) {
        this.scoreboard = objective.getScoreboard();
        this.objective = objective;
    }

    ScoreboardBuilder add(String s, Object... args) {
        this.lines.add(Wrapper.format(s, args));
        return this;
    }

    ScoreboardBuilder line() {
        String line = "";
        for (int i = 0; i < this.emptyLines; i++) {
            line += " ";
        }
        this.add(line);
        this.emptyLines++;
        return this;
    }

    Scoreboard build() {
        if (oldLines.containsKey(this.objective)) {
            oldLines.get(this.objective).forEach(this.scoreboard::resetScores);
            oldLines.get(this.objective).clear();
        }

        AtomicInteger index = new AtomicInteger(this.lines.size());
        this.lines.forEach(s -> this.objective.getScore(s).setScore(index.getAndDecrement()));

        oldLines.put(this.objective, this.lines);

        return this.objective.getScoreboard();
    }

    void buildAndSet() {
        Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(this.build()));
    }

}
