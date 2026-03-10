package com.ngccorp;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.ngccorp.commands.DifficultyCommand;
import javax.annotation.Nonnull;

public class Difficulty extends JavaPlugin {

  private static Difficulty instance;

  // withConfig() must be called at field-init time, not inside a method.
  @Nonnull
  private final Config<DifficultyConfig> config = this.withConfig(DifficultyConfig.CODEC);

  public Difficulty(@Nonnull JavaPluginInit init) {
    super(init);
    instance = this;
  }

  public static Difficulty get() {
    return instance;
  }

  @Override
  protected void setup() {
    this.getEntityStoreRegistry().registerSystem(new DamageFilter());
    this.getEntityStoreRegistry().registerSystem(new PlayerDamageFilter());
    this.getCommandRegistry().registerCommand(new DifficultyCommand());
  }

  @Override
  protected void start() {
    // Config is pre-loaded before start(), so get() is safe here.
    DifficultySettings.setMultipliers(
        this.config.get().getMobMultiplier(),
        this.config.get().getEnvMultiplier(),
        this.config.get().getFallMultiplier(),
        this.config.get().getPlayerMultiplier());
  }

  /**
   * Updates all multipliers in memory and persists them to disk.
   * Called by {@link DifficultyCommand} and the admin UI.
   */
  public void setAndSave(float mob, float env, float fall, float player) {
    DifficultySettings.setMultipliers(mob, env, fall, player);
    this.config.get().setMultipliers(mob, env, fall, player);
    this.config.save(); // async, fire-and-forget
  }

  /** Convenience overload that snaps to a named preset. */
  public void setAndSave(@Nonnull DifficultyLevel level) {
    setAndSave(level.getMobDamageMultiplier(), level.getEnvironmentDamageMultiplier(), level.getFallDamageMultiplier(),
        level.getPlayerDamageMultiplier());
  }

  public static class DifficultyConfig {

    @Nonnull
    public static final BuilderCodec<DifficultyConfig> CODEC = BuilderCodec
        .builder(DifficultyConfig.class, DifficultyConfig::new)
        .append(
            new KeyedCodec<>("MobMultiplier", Codec.STRING),
            (cfg, s) -> cfg.mobMultiplier = Float.parseFloat(s),
            cfg -> String.valueOf(cfg.mobMultiplier))
        .add()
        .append(
            new KeyedCodec<>("EnvMultiplier", Codec.STRING),
            (cfg, s) -> cfg.envMultiplier = Float.parseFloat(s),
            cfg -> String.valueOf(cfg.envMultiplier))
        .add()
        .append(
            new KeyedCodec<>("FallMultiplier", Codec.STRING),
            (cfg, s) -> cfg.fallMultiplier = Float.parseFloat(s),
            cfg -> String.valueOf(cfg.fallMultiplier))
        .add()
        .append(
            new KeyedCodec<>("PlayerMultiplier", Codec.STRING),
            (cfg, s) -> cfg.playerMultiplier = Float.parseFloat(s),
            cfg -> String.valueOf(cfg.playerMultiplier))
        .add()
        .build();

    private float mobMultiplier = 1.0f;
    private float envMultiplier = 1.0f;
    private float fallMultiplier = 1.0f;
    private float playerMultiplier = 1.0f;

    public DifficultyConfig() {
    }

    public float getMobMultiplier() {
      return mobMultiplier;
    }

    public float getEnvMultiplier() {
      return envMultiplier;
    }

    public float getFallMultiplier() {
      return fallMultiplier;
    }

    public float getPlayerMultiplier() {
      return playerMultiplier;
    }

    public void setMultipliers(float mob, float env, float fall, float player) {
      this.mobMultiplier = mob;
      this.envMultiplier = env;
      this.fallMultiplier = fall;
      this.playerMultiplier = player;
    }
  }
}
