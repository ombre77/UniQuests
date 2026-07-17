package net.ody.uniQuests.modules;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    public Map<String, Integer> block_broken = new HashMap<>(); // e.g. "dirt" -> 100
    public Map<String, Integer> mob_killed = new HashMap<>();   // e.g. "pig" -> 8
    public Distance distance = new Distance();

    public static class Distance {
        public double walked; // in km
        public double runed;  // in km
        public double jumped;
    }
}
