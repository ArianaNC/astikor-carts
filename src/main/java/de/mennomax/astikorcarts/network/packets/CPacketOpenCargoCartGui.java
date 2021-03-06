package de.mennomax.astikorcarts.network.packets;

import de.mennomax.astikorcarts.entity.CargoCartEntity;
import de.mennomax.astikorcarts.network.Message;
import de.mennomax.astikorcarts.network.ServerMessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public final class CPacketOpenCargoCartGui implements Message {
    @Override
    public void encode(final PacketBuffer buf) {
    }

    @Override
    public void decode(final PacketBuffer buf) {
    }

    public static void handle(final CPacketOpenCargoCartGui msg, final ServerMessageContext ctx) {
        final PlayerEntity player = ctx.getPlayer();
        final Entity ridden = player.getRidingEntity();
        if (ridden instanceof CargoCartEntity) {
            ((CargoCartEntity) ridden).openContainer(player);
        }
    }
}
