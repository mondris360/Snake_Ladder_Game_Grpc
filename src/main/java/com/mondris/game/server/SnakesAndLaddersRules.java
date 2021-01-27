package com.mondris.game.server;

import java.util.HashMap;
import java.util.Map;

public class SnakesAndLaddersRules {
    private static Map<Integer, Integer> dieScoreRules;

    public static int getPosition(int playerPosition) {
        // rules for ladder base
        dieScoreRules = new HashMap<>();
        dieScoreRules.put(1, 38);
        dieScoreRules.put(4, 14);
        dieScoreRules.put(8, 10);
        dieScoreRules.put(21, 42);
        dieScoreRules.put(28, 76);
        dieScoreRules.put(50, 67);
        dieScoreRules.put(80, 99);
        dieScoreRules.put(71, 92);

        // rules for snake head
        dieScoreRules.put(32, 10);
        dieScoreRules.put(36, 6);
        dieScoreRules.put(48, 26);
        dieScoreRules.put(62, 18);
        dieScoreRules.put(88, 24);
        dieScoreRules.put(95, 56);
        dieScoreRules.put(97, 78);

        return dieScoreRules.getOrDefault(playerPosition, playerPosition);
    }
}
