package com.ngccorp;

public enum DifficultyLevel {
  /**
   * Vanilla Hytale behaviour — no damage multipliers applied.
   */
  NORMAL(1.0f, 1.0f, 1.0f, 1.0f),

  /**
   * Slightly harder: mobs hit 50 % harder, environment 25 % harder, fall 25 %
   * harder.
   */
  MEDIUM(1.5f, 1.25f, 1.25f, 1.0f),

  /**
   * Challenging: mobs hit twice as hard, environment 50 % harder, fall 75 %
   * harder.
   */
  HARD(2.0f, 1.5f, 1.75f, 1.0f),

  /**
   * Nightmare: mobs hit three times as hard, environment twice as hard, fall 2×
   * harder.
   */
  NIGHTMARE(3.0f, 2.5f, 2.0f, 1.0f);

  /** Multiplier applied to damage dealt by non-player entities (mobs). */
  private final float mobDamageMultiplier;

  /**
   * Multiplier applied to non-fall environment damage (fire, void, drowning,
   * suffocation, …).
   */
  private final float environmentDamageMultiplier;

  /** Multiplier applied specifically to fall damage. */
  private final float fallDamageMultiplier;

  /** Multiplier applied to damage dealt by players to mobs (not PvP). */
  private final float playerDamageMultiplier;

  DifficultyLevel(float mobDamageMultiplier, float environmentDamageMultiplier, float fallDamageMultiplier, float playerDamageMultiplier) {
    this.mobDamageMultiplier = mobDamageMultiplier;
    this.environmentDamageMultiplier = environmentDamageMultiplier;
    this.fallDamageMultiplier = fallDamageMultiplier;
    this.playerDamageMultiplier = playerDamageMultiplier;
  }

  public float getMobDamageMultiplier() {
    return mobDamageMultiplier;
  }

  public float getEnvironmentDamageMultiplier() {
    return environmentDamageMultiplier;
  }

  public float getFallDamageMultiplier() {
    return fallDamageMultiplier;
  }

  public float getPlayerDamageMultiplier() {
    return playerDamageMultiplier;
  }
}
