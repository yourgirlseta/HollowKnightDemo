package com.yourgirlseta.hollowKnight.model.enemy.boss;

import com.yourgirlseta.hollowKnight.model.enums.FalseKnightState;

import java.util.ArrayList;
import java.util.Random;

public class FalseKnightAI {

    private final Random random = new Random();
    private FalseKnightState lastState = FalseKnightState.IDLE;

    public FalseKnightState chooseState(float distance, boolean phase2) {

        ArrayList<FalseKnightState> states = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();

        if (distance < 250f) {                 // برد واقعی مِیس
            add(states, weights, FalseKnightState.ATTACK_ANTIC, 65);
            add(states, weights, FalseKnightState.JUMP_ATTACK, 20);
            add(states, weights, FalseKnightState.RUN_ANTIC, 15);
        }
        else if (distance < 600f) {
            add(states, weights, FalseKnightState.RUN_ANTIC, 55);
            add(states, weights, FalseKnightState.JUMP_ATTACK, 35);
            add(states, weights, FalseKnightState.JUMP, 10);
        }
        else {
            add(states, weights, FalseKnightState.RUN_ANTIC, 70);
            add(states, weights, FalseKnightState.JUMP_ATTACK, 20);
            add(states, weights, FalseKnightState.JUMP, 10);
        }

        if (phase2) {

            for (int i = 0; i < states.size(); i++) {
                if (states.get(i) == FalseKnightState.JUMP_ATTACK) {

                    weights.set(i, weights.get(i) + 20);

                }

                if (states.get(i) == FalseKnightState.RUN_ANTIC) {

                    weights.set(i, weights.get(i) + 10);

                }

            }

        }

        FalseKnightState selected = weightedRandom(states, weights);

        if (states.size() > 1) {
            while (selected == lastState) {
                selected = weightedRandom(states, weights);
            }
        }

        lastState = selected;
        return selected;
    }

    private void add(
        ArrayList<FalseKnightState> states,
        ArrayList<Integer> weights,
        FalseKnightState state,
        int weight) {

        states.add(state);
        weights.add(weight);
    }

    private FalseKnightState weightedRandom(
        ArrayList<FalseKnightState> states,
        ArrayList<Integer> weights) {

        int total = 0;

        for (int w : weights) {
            total += w;
        }

        int value = random.nextInt(total);

        int current = 0;

        for (int i = 0; i < states.size(); i++) {

            current += weights.get(i);

            if (value < current) {
                return states.get(i);
            }
        }

        return FalseKnightState.IDLE;
    }
}
