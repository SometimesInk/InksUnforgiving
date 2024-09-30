package com.ink.unforgiving.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventTick {

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        List<EntityPlayer> entities = Minecraft.func_71410_x().field_71441_e.field_73010_i;

    }
}