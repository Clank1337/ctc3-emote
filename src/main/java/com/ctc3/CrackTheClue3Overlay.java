package com.ctc3;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.cluescrolls.clues.emote.Emote;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class CrackTheClue3Overlay extends Overlay {

    private final CrackTheClue3Plugin plugin;
    private final Client client;

    private boolean hasScrolled;

    @Inject
    private CrackTheClue3Overlay(CrackTheClue3Plugin plugin, Client client) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.plugin = plugin;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if (client.getGameState() != GameState.LOGGED_IN) {
            return null;
        }

        Emote currentEmote = plugin.getCurrentEmote();

        if (currentEmote == null || !currentEmote.hasSprite()) {
            return null;
        }

        Player localPlayer = client.getLocalPlayer();
        // Check to make sure we're in the basement or standing next to the gates to get in/out
        if (localPlayer == null || localPlayer.getWorldLocation().getRegionID() != 12697 ||
                (!localPlayer.getWorldLocation().equals(new WorldPoint(3191, 9825, 0))
                        && !localPlayer.getWorldLocation().equals(new WorldPoint(3192, 9825, 0))
                        && !localPlayer.getWorldLocation().equals(new WorldPoint(3191, 9824, 0))
                        && !localPlayer.getWorldLocation().equals(new WorldPoint(3192, 9824, 0)))) {
            return null;
        }

        Widget emoteContainer = client.getWidget(ComponentID.EMOTES_EMOTE_CONTAINER);

        if (emoteContainer == null || emoteContainer.isHidden()) {
            return null;
        }

        Widget emoteWindow = client.getWidget(ComponentID.EMOTES_WINDOW);

        if (emoteWindow == null) {
            return null;
        }

        Widget firstEmoteWidget = null;
        Widget secondEmoteWidget = null;

        for (Widget emoteWidget : emoteContainer.getDynamicChildren()) {
            if (emoteWidget.getSpriteId() == currentEmote.getSpriteId()) {
                firstEmoteWidget = emoteWidget;
                plugin.highlightWidget(graphics, emoteWidget, emoteWindow, null, null);
            }
        }
        if (!hasScrolled) {
            hasScrolled = true;
            plugin.scrollToWidget(ComponentID.EMOTES_EMOTE_SCROLL_CONTAINER, ComponentID.EMOTES_EMOTE_SCROLLBAR, firstEmoteWidget, secondEmoteWidget);
        }
        return null;
    }
}
