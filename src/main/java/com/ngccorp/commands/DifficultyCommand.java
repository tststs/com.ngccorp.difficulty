package com.ngccorp.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ngccorp.Difficulty;
import com.ngccorp.DifficultyLevel;
import com.ngccorp.DifficultySettings;
import com.ngccorp.ui.DifficultyUI;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DifficultyCommand extends AbstractCommand {
  public DifficultyCommand() {
    super("difficulty", "Get or set server difficulty.");

    for (DifficultyLevel level : DifficultyLevel.values()) {
      this.addSubCommand(new SetDifficultyCommand(level));
    }

    this.addSubCommand(new ShowUICommand());
  }

  private static class ShowUICommand extends AbstractPlayerCommand {
    public ShowUICommand() {
      super("ui", "Opens the difficulty ui.");
      this.setPermissionGroup(null);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
      CompletableFuture.runAsync(() -> {
        DifficultyUI.openFor(playerRef, store);
      }, world);
    }
  }

  private static class SetDifficultyCommand extends AbstractPlayerCommand {
    @Nonnull
    private final DifficultyLevel level;

    public SetDifficultyCommand(@Nonnull DifficultyLevel level) {
      super(level.name().toLowerCase(), "Set difficulty to " + level.name().toLowerCase() + ".");
      this.setPermissionGroup(null);
      this.level = level;
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
      Difficulty.get().setAndSave(this.level);
      commandContext.sendMessage(Message.raw(String.format(
          "Difficulty set to %s (mob x%.2f, environment x%.2f, fall x%.2f, player x%.2f).",
          this.level.name(),
          this.level.getMobDamageMultiplier(),
          this.level.getEnvironmentDamageMultiplier(),
          this.level.getFallDamageMultiplier(),
          this.level.getPlayerDamageMultiplier())));
    }
  }

  @Nullable
  @Override
  protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
    context.sendMessage(Message.raw(String.format(
        "Difficulty: mob x%.2f, environment x%.2f, fall x%.2f, player x%.2f",
        DifficultySettings.getMobMultiplier(),
        DifficultySettings.getEnvMultiplier(),
        DifficultySettings.getFallMultiplier(),
        DifficultySettings.getPlayerMultiplier())));
    return CompletableFuture.completedFuture(null);
  }
}
