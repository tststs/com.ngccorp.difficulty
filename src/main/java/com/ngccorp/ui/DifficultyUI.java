package com.ngccorp.ui;

import au.ellie.hyui.builders.ButtonBuilder;
import au.ellie.hyui.builders.FloatSliderBuilder;
import au.ellie.hyui.builders.LabelBuilder;
import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.events.UIContext;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.ngccorp.Difficulty;
import com.ngccorp.DifficultyLevel;
import com.ngccorp.DifficultySettings;
import javax.annotation.Nonnull;

/**
 * Admin panel for adjusting server difficulty.
 *
 * <p>
 * Opened via {@code /difficulty ui}.
 * Provides four preset buttons and three fine-tuning sliders
 * (mob damage multiplier, environment damage multiplier, fall damage
 * multiplier).
 * Changes are applied and persisted immediately on every interaction.
 *
 */
public final class DifficultyUI {

  // Slider range: 0.1× to 10.0×, step 0.1.
  private static final float SLIDER_MIN = 0.1f;
  private static final float SLIDER_MAX = 10.0f;
  private static final float SLIDER_STEP = 0.1f;

  private static final String[] PRESET_IDS = {
      "preset-normal", "preset-medium", "preset-hard", "preset-nightmare"
  };

  private DifficultyUI() {
  }

  /**
   * Opens the difficulty panel for the given player.
   *
   * @param player the admin player to show the UI to
   * @param store  the entity store associated with the player's current world
   */
  @SuppressWarnings("null")
  public static void openFor(@Nonnull PlayerRef player, @Nonnull Store<EntityStore> store) {
    PageBuilder page = PageBuilder.detachedPage()
        .withLifetime(CustomPageLifetime.CanDismiss)
        .enablePersistentElementEdits(true)
        .loadHtml("Pages/DifficultyUI.html");

    // ── Preset buttons
    bindPreset(page, PRESET_IDS[0], DifficultyLevel.NORMAL);
    bindPreset(page, PRESET_IDS[1], DifficultyLevel.MEDIUM);
    bindPreset(page, PRESET_IDS[2], DifficultyLevel.HARD);
    bindPreset(page, PRESET_IDS[3], DifficultyLevel.NIGHTMARE);

    // ── Mob damage slider
    page.getById("mob-slider", FloatSliderBuilder.class)
        .ifPresent(slider -> {
          slider.addEventListener(CustomUIEventBindingType.ValueChanged,
              (Float value, UIContext ctx) -> {
                ctx.editById("mob-slider", FloatSliderBuilder.class,
                    s -> s.withValue(value));
                ctx.editById("mob-label", LabelBuilder.class,
                    l -> l.withText(formatMultiplier(value)));
                for (String id : PRESET_IDS) {
                  ctx.editById(id, ButtonBuilder.class,
                      b -> b.withDisabled(false));
                }
                Difficulty.get().setAndSave(value, DifficultySettings.getEnvMultiplier(),
                    DifficultySettings.getFallMultiplier(), DifficultySettings.getPlayerMultiplier());
                ctx.updatePage(false);
              });
        });

    // ── Environment damage slider
    page.getById("env-slider", FloatSliderBuilder.class)
        .ifPresent(slider -> {
          slider.addEventListener(CustomUIEventBindingType.ValueChanged,
              (Float value, UIContext ctx) -> {
                ctx.editById("env-slider", FloatSliderBuilder.class,
                    s -> s.withValue(value));
                ctx.editById("env-label", LabelBuilder.class,
                    l -> l.withText(formatMultiplier(value)));
                for (String id : PRESET_IDS) {
                  ctx.editById(id, ButtonBuilder.class,
                      b -> b.withDisabled(false));
                }
                Difficulty.get().setAndSave(DifficultySettings.getMobMultiplier(), value,
                    DifficultySettings.getFallMultiplier(), DifficultySettings.getPlayerMultiplier());
                ctx.updatePage(false);
              });
        });

    // ── Fall damage slider
    page.getById("fall-slider", FloatSliderBuilder.class)
        .ifPresent(slider -> {
          slider.addEventListener(CustomUIEventBindingType.ValueChanged,
              (Float value, UIContext ctx) -> {
                ctx.editById("fall-slider", FloatSliderBuilder.class,
                    s -> s.withValue(value));
                ctx.editById("fall-label", LabelBuilder.class,
                    l -> l.withText(formatMultiplier(value)));
                for (String id : PRESET_IDS) {
                  ctx.editById(id, ButtonBuilder.class,
                      b -> b.withDisabled(false));
                }
                Difficulty.get().setAndSave(DifficultySettings.getMobMultiplier(),
                    DifficultySettings.getEnvMultiplier(), value, DifficultySettings.getPlayerMultiplier());
                ctx.updatePage(false);
              });
        });

    // ── Player damage slider
    page.getById("player-slider", FloatSliderBuilder.class)
        .ifPresent(slider -> {
          slider.addEventListener(CustomUIEventBindingType.ValueChanged,
              (Float value, UIContext ctx) -> {
                ctx.editById("player-slider", FloatSliderBuilder.class,
                    s -> s.withValue(value));
                ctx.editById("player-label", LabelBuilder.class,
                    l -> l.withText(formatMultiplier(value)));
                for (String id : PRESET_IDS) {
                  ctx.editById(id, ButtonBuilder.class,
                      b -> b.withDisabled(false));
                }
                Difficulty.get().setAndSave(DifficultySettings.getMobMultiplier(),
                    DifficultySettings.getEnvMultiplier(), DifficultySettings.getFallMultiplier(), value);
                ctx.updatePage(false);
              });
        });

    // ── Close button
    page.getById("button-close", ButtonBuilder.class)
        .ifPresent(btn -> btn.addEventListener(CustomUIEventBindingType.Activating,
            (ignored, ctx) -> ctx.getPage().ifPresent(p -> p.close())));

    page.editById("mob-slider", FloatSliderBuilder.class,
        s -> s.withValue(DifficultySettings.getMobMultiplier()));
    page.editById("mob-label", LabelBuilder.class,
        l -> l.withText(formatMultiplier(DifficultySettings.getMobMultiplier())));

    page.editById("env-slider", FloatSliderBuilder.class,
        s -> s.withValue(DifficultySettings.getEnvMultiplier()));
    page.editById("env-label", LabelBuilder.class,
        l -> l.withText(formatMultiplier(DifficultySettings.getEnvMultiplier())));

    page.editById("fall-slider", FloatSliderBuilder.class,
        s -> s.withValue(DifficultySettings.getFallMultiplier()));
    page.editById("fall-label", LabelBuilder.class,
        l -> l.withText(formatMultiplier(DifficultySettings.getFallMultiplier())));

    page.editById("player-slider", FloatSliderBuilder.class,
        s -> s.withValue(DifficultySettings.getPlayerMultiplier()));
    page.editById("player-label", LabelBuilder.class,
        l -> l.withText(formatMultiplier(DifficultySettings.getPlayerMultiplier())));

    // Visual feedback: enable the active preset
    if (DifficultySettings.isVanilla()) {
      page.editById(
          PRESET_IDS[0], ButtonBuilder.class,
          b -> b.withDisabled(true));
    } else if (DifficultySettings.isMedium()) {
      page.editById(
          PRESET_IDS[1], ButtonBuilder.class,
          b -> b.withDisabled(true));
    } else if (DifficultySettings.isHard()) {
      page.editById(
          PRESET_IDS[2], ButtonBuilder.class,
          b -> b.withDisabled(true));
    } else if (DifficultySettings.isNightmare()) {
      page.editById(
          PRESET_IDS[3], ButtonBuilder.class,
          b -> b.withDisabled(true));
    }

    page.open(player, store);
  }

  // ── Helpers
  private static void bindPreset(
      @Nonnull PageBuilder page,
      @Nonnull String buttonId,
      @Nonnull DifficultyLevel level) {

    page.getById(buttonId, ButtonBuilder.class)
        .ifPresent(btn -> btn.addEventListener(CustomUIEventBindingType.Activating,
            (ignored, ctx) -> {
              float mob = level.getMobDamageMultiplier();
              float env = level.getEnvironmentDamageMultiplier();
              float fall = level.getFallDamageMultiplier();
              float player = level.getPlayerDamageMultiplier();

              // Sync slider positions and labels to the new preset values.
              ctx.editById("mob-slider", FloatSliderBuilder.class,
                  s -> s.withValue(mob));
              ctx.editById("env-slider", FloatSliderBuilder.class,
                  s -> s.withValue(env));
              ctx.editById("fall-slider", FloatSliderBuilder.class,
                  s -> s.withValue(fall));
              ctx.editById("player-slider", FloatSliderBuilder.class,
                  s -> s.withValue(player));
              ctx.editById("mob-label", LabelBuilder.class,
                  l -> l.withText(formatMultiplier(mob)));
              ctx.editById("env-label", LabelBuilder.class,
                  l -> l.withText(formatMultiplier(env)));
              ctx.editById("fall-label", LabelBuilder.class,
                  l -> l.withText(formatMultiplier(fall)));
              ctx.editById("player-label", LabelBuilder.class,
                  l -> l.withText(formatMultiplier(player)));

              // Visual feedback: disable the active preset, re-enable the rest.
              for (String id : PRESET_IDS) {
                ctx.editById(id, ButtonBuilder.class,
                    b -> b.withDisabled(id.equals(buttonId)));
              }

              ctx.updatePage(true);
              Difficulty.get().setAndSave(mob, env, fall, player);
            }));
  }

  private static String formatMultiplier(float v) {
    return String.format("%.2f\u00d7", v); // "1.50×"
  }
}
