package com.ngccorp;

import javax.annotation.Nonnull;

/**
 * Global difficulty settings for the server.
 *
 * <p>
 * Multipliers are stored as raw floats so they can be fine-tuned via the admin
 * UI sliders. Use {@link #applyPreset(DifficultyLevel)} to snap to a named
 * preset, or {@link #setMultipliers(float, float)} for arbitrary values.
 */
public final class DifficultySettings {

  private static volatile float mobMultiplier = 1.0f;
  private static volatile float envMultiplier = 1.0f;
  private static volatile float fallMultiplier = 1.0f;
  private static volatile float playerMultiplier = 1.0f;

  private DifficultySettings() {
  }

  public static float getMobMultiplier() {
    return mobMultiplier;
  }

  public static float getEnvMultiplier() {
    return envMultiplier;
  }

  public static float getFallMultiplier() {
    return fallMultiplier;
  }

  public static float getPlayerMultiplier() {
    return playerMultiplier;
  }

  public static void setMultipliers(float mob, float env, float fall, float player) {
    mobMultiplier = mob;
    envMultiplier = env;
    fallMultiplier = fall;
    playerMultiplier = player;
  }

  /** Snaps all multipliers to the values defined by the given preset. */
  public static void applyPreset(@Nonnull DifficultyLevel level) {
    setMultipliers(level.getMobDamageMultiplier(), level.getEnvironmentDamageMultiplier(), level.getFallDamageMultiplier(), level.getPlayerDamageMultiplier());
  }

  /**
   * True when all multipliers are 1.0 — vanilla behaviour, no scaling needed.
   */
  public static boolean isVanilla() {
    return mobMultiplier == 1.0f && envMultiplier == 1.0f && fallMultiplier == 1.0f && playerMultiplier == 1.0f;
  }

  public static boolean isMedium() {
    return mobMultiplier == DifficultyLevel.MEDIUM.getMobDamageMultiplier()
        && envMultiplier == DifficultyLevel.MEDIUM.getEnvironmentDamageMultiplier()
        && fallMultiplier == DifficultyLevel.MEDIUM.getFallDamageMultiplier()
        && playerMultiplier == DifficultyLevel.MEDIUM.getPlayerDamageMultiplier();
  }

  public static boolean isHard() {
    return mobMultiplier == DifficultyLevel.HARD.getMobDamageMultiplier()
        && envMultiplier == DifficultyLevel.HARD.getEnvironmentDamageMultiplier()
        && fallMultiplier == DifficultyLevel.HARD.getFallDamageMultiplier()
        && playerMultiplier == DifficultyLevel.HARD.getPlayerDamageMultiplier();
  }

  public static boolean isNightmare() {
    return mobMultiplier == DifficultyLevel.NIGHTMARE.getMobDamageMultiplier()
        && envMultiplier == DifficultyLevel.NIGHTMARE.getEnvironmentDamageMultiplier()
        && fallMultiplier == DifficultyLevel.NIGHTMARE.getFallDamageMultiplier()
        && playerMultiplier == DifficultyLevel.NIGHTMARE.getPlayerDamageMultiplier();
  }
}
