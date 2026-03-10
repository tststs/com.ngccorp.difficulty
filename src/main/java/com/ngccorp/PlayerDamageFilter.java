package com.ngccorp;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Intercepts outgoing damage from players to mobs and scales it according to
 * {@link DifficultySettings#getPlayerMultiplier()}.
 *
 * The query targets NPCEntity only, so PvP damage is never affected.
 */
public class PlayerDamageFilter extends DamageEventSystem {

  @SuppressWarnings("null")
  @Nonnull
  private static final Query<EntityStore> QUERY = NPCEntity.getComponentType();

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

    if (DifficultySettings.getPlayerMultiplier() == 1.0f) {
      return;
    }

    if (!(damage.getSource() instanceof Damage.EntitySource entitySource)) {
      return;
    }

    boolean attackerIsPlayer = buffer.getComponent(entitySource.getRef(), Player.getComponentType()) != null;

    if (attackerIsPlayer) {
      damage.setAmount(damage.getAmount() * DifficultySettings.getPlayerMultiplier());
    }
  }
}
