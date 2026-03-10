package com.ngccorp;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Intercepts incoming damage on players and scales it according to the active
 * multipliers in {@link DifficultySettings}.
 *
 * When both multipliers are 1.0 the system exits immediately so vanilla
 * behaviour is preserved with zero overhead.
 */
public class DamageFilter extends DamageEventSystem {

  @Nonnull
  private static final Query<EntityStore> QUERY = Player.getComponentType();

  @Nonnull
  @Override
  public Query<EntityStore> getQuery() {
    return QUERY;
  }

  @Nullable
  @Override
  public SystemGroup<EntityStore> getGroup() {
    return DamageModule.get().getFilterDamageGroup();
  }

  @Override
  public void handle(
      int index,
      @Nonnull ArchetypeChunk<EntityStore> chunk,
      @Nonnull Store<EntityStore> store,
      @Nonnull CommandBuffer<EntityStore> buffer,
      @Nonnull Damage damage) {

    // Nothing to do on vanilla multipliers.
    if (DifficultySettings.isVanilla()) {
      return;
    }

    Damage.Source source = damage.getSource();

    if (source instanceof Damage.EntitySource entitySource) {
      // Only scale mob damage — leave PvP unmodified.
      System.out.println("Difficulty: MOB DAMAGE");

      boolean attackerIsPlayer = buffer.getComponent(entitySource.getRef(), Player.getComponentType()) != null;

      if (!attackerIsPlayer) {
        System.out.println("Difficulty: MOB DAMAGE 2");

        damage.setAmount(damage.getAmount() * DifficultySettings.getMobMultiplier());
      }

      return;
    }

    DamageCause cause = DamageCause.getAssetMap().getAsset(damage.getDamageCauseIndex());
    boolean isFall = cause != null && cause == DamageCause.FALL;

    if (isFall) {
      System.out.println("Difficulty: FALL DAMAGE");
      // Fall damage is scaled independently.
      damage.setAmount(damage.getAmount() * DifficultySettings.getFallMultiplier());

      return;
    }

    if (source instanceof Damage.EnvironmentSource || source == Damage.NULL_SOURCE) {
      System.out.println("Difficulty: ENV DAMAGE");
      // Fire, void, drowning, suffocation, …
      damage.setAmount(damage.getAmount() * DifficultySettings.getEnvMultiplier());
    }
  }
}
