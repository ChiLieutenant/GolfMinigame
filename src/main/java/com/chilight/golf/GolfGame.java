package com.chilight.golf;

import lombok.Getter;
import lombok.Setter;

public class GolfGame {
    @Getter @Setter private static PartyHandler party;

    public void GolfGame(PartyHandler party) {
        setParty(party);
    }

    public void start() {
    }

    public void end() {

    }
}
