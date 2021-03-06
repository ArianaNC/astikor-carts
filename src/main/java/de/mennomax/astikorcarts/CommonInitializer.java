package de.mennomax.astikorcarts;

import de.mennomax.astikorcarts.config.AstikorCartsConfig;
import de.mennomax.astikorcarts.entity.PostilionEntity;
import de.mennomax.astikorcarts.entity.ai.goal.PullCartGoal;
import de.mennomax.astikorcarts.world.AstikorWorld;
import de.mennomax.astikorcarts.world.SimpleAstikorWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;

public class CommonInitializer implements Initializer {
    @Override
    public void init(final Context mod) {
        mod.context().registerConfig(ModConfig.Type.COMMON, AstikorCartsConfig.COMMON_SPEC);
        mod.modBus().<FMLCommonSetupEvent>addListener(e -> {
            CapabilityManager.INSTANCE.register(AstikorWorld.class, new Capability.IStorage<AstikorWorld>() {
                @Nullable
                @Override
                public INBT writeNBT(final Capability<AstikorWorld> capability, final AstikorWorld instance, final Direction side) {
                    return null;
                }

                @Override
                public void readNBT(final Capability<AstikorWorld> capability, final AstikorWorld instance, final Direction side, final INBT nbt) {
                }
            }, SimpleAstikorWorld::new);
        });
        mod.bus().<AttachCapabilitiesEvent<World>, World>addGenericListener(World.class, e ->
            e.addCapability(new ResourceLocation(AstikorCarts.ID, "astikor"), AstikorWorld.createProvider(SimpleAstikorWorld::new))
        );
        mod.bus().<EntityJoinWorldEvent>addListener(e -> {
            final Entity entity = e.getEntity();
            if (!e.getWorld().isRemote && entity instanceof MobEntity) {
                ((MobEntity) entity).goalSelector.addGoal(1, new PullCartGoal(entity));
            }
        });
        mod.bus().<PlayerInteractEvent.EntityInteract>addListener(e -> {
            final Entity rider = e.getTarget().getControllingPassenger();
            if (rider instanceof PostilionEntity) {
                rider.stopRiding();
            }
        });
        mod.bus().<TickEvent.WorldTickEvent>addListener(e -> {
            if (e.phase == TickEvent.Phase.END) {
                AstikorWorld.get(e.world).ifPresent(AstikorWorld::tick);
            }
        });
    }
}
