package org.scrum.psd.battleship.controller.dto;

import java.util.Locale;

public enum Letter {
    A, B, C, D, E, F, G, H;

    public static Letter fromInput(String letter) {
        if (letter == null)  {
            throw new NullPointerException("Letter is null");
        }

        for (Letter value : values()) {
            if (value.name().toLowerCase(Locale.ROOT).equals(letter)) {
                return value;
            }
        }

        return null;
    }
}
