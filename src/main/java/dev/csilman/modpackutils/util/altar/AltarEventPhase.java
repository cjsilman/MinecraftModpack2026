package dev.csilman.modpackutils.util.altar;

public enum AltarEventPhase {
    DORMANT, // Players collect beacons
    AWAKENING, // Once all beacons are collected and placed on pedestals
    SIEGE, // Final event - still determining how this will look
    BOSS,
    DEFEATED // Post event
}
