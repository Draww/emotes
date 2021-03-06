package com.kosmx.emotecraft;

import com.kosmx.emotecraft.config.EmoteHolder;
import com.kosmx.emotecraft.config.SerializableConfig;
import com.kosmx.emotecraftCommon.EmoteData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

import javax.annotation.Nullable;


/**
 * Most important comment for modders!
 *
 * If you rewrite the mod's source code, you'll have to update your code everytime when there is an update.
 * Try to use these below.
 *
 * Never forget to you can apply {@link org.spongepowered.asm.mixin.Mixin} and other Fabric tools on mods and not only on the vanilla Minecraft game!
 * maybe you should check it with {@link net.fabricmc.loader.api.FabricLoader#isModLoaded(String)} to is the mod in the mods directory.
 *
 * If you have any question, you can find me on Discord.
 *
 * Here is 3 callbacks, you can use these.
 * With {@link ActionResult#FAIL} result you can cancel playing the emote.
 * {@link ActionResult#PASS} continue the callbacks checking
 *
 * {@link ActionResult#SUCCESS} and {@link ActionResult#CONSUME} will stop the callbacks but won't stop the emote
 *
 * Good luck for modding the mod :D
 */
public interface EmotecraftCallbacks {

    /**
     * Triggers every time when the client receive an emote
     */
    Event<PlayReceivedEmote> startPlayReceivedEmote = EventFactory.createArrayBacked(PlayReceivedEmote.class, (listeners) -> (emote, player, enable) -> {
        for (PlayReceivedEmote listener : listeners){
            ActionResult result = listener.playReceivedEmote(emote, player, enable);
            if(result != ActionResult.PASS){
                return result;
            }
        }
        return ActionResult.PASS;
    });

    interface PlayReceivedEmote {
        /**
         *
         * @param emote Emote to play
         * @param player Player
         * @param isPlayerBlocked Is the player blocked by player safety
         * @return
         */
        ActionResult playReceivedEmote(EmoteData emote, PlayerEntity player, boolean isPlayerBlocked);
    }

    /**
     * Triggers when the client start an emote. With cancelling, the play won't be synced.
     */
    Event<PlayClientEmote> startPlayClientEmote = EventFactory.createArrayBacked(PlayClientEmote.class, (listeners) -> (emote, player, emoteHolder, hasServerEmotecraft) -> {
        for (PlayClientEmote listener : listeners){
            ActionResult result = listener.playClientEmote(emote, player, emoteHolder, hasServerEmotecraft);
            if(result != ActionResult.PASS){
                return result;
            }
        }
        return ActionResult.PASS;
    });

    interface PlayClientEmote {
        ActionResult playClientEmote(EmoteData emote, PlayerEntity player, @Nullable EmoteHolder emoteHolder, boolean hasServerEmotecraft);
    }

    /**
     * Triggers every time when an emote start to playing. This is after synchronising the emote.
     */
    Event<PlayEmote> startPlayEmote = EventFactory.createArrayBacked(PlayEmote.class, (listeners) -> (emote, player) -> {
        for (PlayEmote listener : listeners){
            ActionResult result = listener.playEmote(emote, player);
            if(result != ActionResult.PASS){
                return result;
            }
        }
        return ActionResult.PASS;
    });

    interface PlayEmote {
        ActionResult playEmote(EmoteData emote, PlayerEntity player);
    }

    /**
     * Triggers every time when an emote start to playing. This is after synchronising the emote.
     */
    Event<LoadConfig> loadConfig = EventFactory.createArrayBacked(LoadConfig.class, (listeners) -> (config) -> {
                for (LoadConfig listener : listeners){
                    ActionResult result = listener.loadConfig(config);
                    if(result != ActionResult.PASS){
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    interface LoadConfig {
        ActionResult loadConfig(SerializableConfig config);
    }
}
