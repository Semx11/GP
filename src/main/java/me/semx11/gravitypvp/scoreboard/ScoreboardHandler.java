package me.semx11.gravitypvp.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.semx11.gravitypvp.GravityPvp;
import me.semx11.gravitypvp.gravity.GravityConstant;
import me.semx11.gravitypvp.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private List<String> oldLabels = new ArrayList<>();
    private int emptyLines = 0;

    private AtomicInteger index = new AtomicInteger(9);

    public ScoreboardHandler(Scoreboard sb) {
        this.scoreboard = sb;

        this.objective = this.scoreboard.registerNewObjective("main", "dummy");
        this.objective.setDisplayName(Wrapper.format("&e&lGRAVITY PVP"));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.update();
    }

    // Quick implementation with scoreboards.
    public void update() {
        this.clear();

        this.index = new AtomicInteger(15);
        this.addLine();

        switch (GravityPvp.getGameState()) {
            case WAITING:
                this.addScore(Wrapper.format("Players: &a%d/%d", Wrapper.getOnlineCount(),
                        GravityPvp.getMaxPlayers()));
                this.addLine();
                this.addScore(Wrapper.format("&fWaiting..."));
                break;
            case STARTING:
                this.addScore(Wrapper.format("&fPlayers: &a%d/%d", Wrapper.getOnlineCount(),
                        GravityPvp.getMaxPlayers()));
                this.addLine();
                this.addScore(Wrapper.format("&fStarting in &a%ds",
                        GravityPvp.getInstance().getCountdown()));
                break;
            case RUNNING:
                this.addScore(Wrapper.format("&eChanges each minute!"));
                this.addLine();
                this.addScore(Wrapper.format("&eGravity:"));
                GravityConstant.getCurrent().entrySet().forEach(entry -> this.addScore(
                        Wrapper.format("&f%s: &a%s",
                                entry.getKey().getSimpleName(),
                                entry.getValue().toString())));
                break;
            case FINISHED:
                this.addScore(Wrapper.format("&aThanks for playing!"));
                break;
        }
        this.addLine();
        this.addScore(Wrapper.format("&ewww.example.com"));

        Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(scoreboard));
    }

    private void addScore(String s) {
        this.objective.getScore(s).setScore(index.getAndDecrement());
        this.oldLabels.add(s);
    }

    private void addLine() {
        String line = "";
        for (int i = 0; i < this.emptyLines; i++) {
            line += " ";
        }
        this.addScore(line);
        this.emptyLines++;
    }

    private void clear() {
        this.emptyLines = 0;
        this.oldLabels.forEach(this.scoreboard::resetScores);
        this.oldLabels.clear();
    }

}
