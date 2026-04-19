package dev.csilman.modpackutils.block;

import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.block.custom.BeaconPedestalBlock;
import dev.csilman.modpackutils.block.custom.BossBeaconBlock;
import dev.csilman.modpackutils.block.custom.SacredStoneBlock;
import dev.csilman.modpackutils.component.MemoryDestination;
import dev.csilman.modpackutils.dimension.ModDimensions;
import dev.csilman.modpackutils.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ModpackUtilsMod.MOD_ID);

    // Sample test
    public static final DeferredBlock<Block> BLACK_OPAL_BLOCK = registerBlock("black_opal_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SACRED_STONE = registerBlock("sacred_stone",
            () -> new SacredStoneBlock(BlockBehaviour.Properties.of().strength(20.0f)));

    // Beacon Pedestal Blocks - Must be before Boss Beacons
    public static final DeferredBlock<Block> ABYSS_BEACON_PEDESTAL_BLOCK = registerBlock("abyss_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                        ModDimensions.ABYSS_WORLD,
                        86.5,
                        100,
                        87.5,
                    5
                    )
            )
    );

    public static final DeferredBlock<Block> CURSED_PEDESTAL_BLOCK = registerBlock("cursed_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.CURSED_WORLD,
                            151.5,
                            100,
                            138.5,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> DESERT_PEDESTAL_BLOCK = registerBlock("desert_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.DESERT_WORLD,
                            26.5,
                            100,
                            -118.5,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> IGNIS_BEACON_PEDESTAL_BLOCK = registerBlock("ignis_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.IGNIS_WORLD,
                            -5.5,
                            80,
                            -102.5,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> MECH_BEACON_PEDESTAL_BLOCK = registerBlock("mech_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.ABYSS_WORLD,
                            0,
                            100,
                            0,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> MONSTROUS_BEACON_PEDESTAL_BLOCK = registerBlock("monstrous_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.ABYSS_WORLD,
                            0,
                            100,
                            0,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> STORM_BEACON_PEDESTAL_BLOCK = registerBlock("storm_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.STORM_WORLD,
                            -5.5,
                            150,
                            -102.5,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> VOID_BEACON_PEDESTAL_BLOCK = registerBlock("void_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            ModDimensions.ABYSS_WORLD,
                            0,
                            100,
                            0,
                            5
                    )
            )
    );

    public static final DeferredBlock<Block> HOME_BEACON_PEDESTAL_BLOCK = registerBlock("home_beacon_pedestal_block",
            () -> new BeaconPedestalBlock(
                    BlockBehaviour.Properties.of().strength(20.0f),
                    new MemoryDestination(
                            Level.OVERWORLD,
                            0,
                            100,
                            0,
                            1
                    )
            )
    );

    // Boss Beacon Blocks
    public static final DeferredBlock<Block> ABYSS_BEACON_BLOCK = registerBlock("abyss_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    6427105,
                    ABYSS_BEACON_PEDESTAL_BLOCK,
                    MobEffects.WATER_BREATHING
            ));

    public static final DeferredBlock<Block>
            CURSED_BEACON_BLOCK = registerBlock("cursed_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    3789490,
                    CURSED_PEDESTAL_BLOCK,
                    MobEffects.DAMAGE_RESISTANCE
            ));

    public static final DeferredBlock<Block> DESERT_BEACON_BLOCK = registerBlock("desert_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    16747048,
                    DESERT_PEDESTAL_BLOCK,
                    MobEffects.REGENERATION
            ));

    public static final DeferredBlock<Block> IGNIS_BEACON_BLOCK = registerBlock("ignis_beacon_block",
            () -> new BossBeaconBlock(
                    BlockBehaviour.Properties.of()
                            .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    106404, 
                    IGNIS_BEACON_PEDESTAL_BLOCK,
                    MobEffects.FIRE_RESISTANCE
            ));

    public static final DeferredBlock<Block> MECH_BEACON_BLOCK = registerBlock("mech_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    16476957,
                    MECH_BEACON_PEDESTAL_BLOCK,
                    MobEffects.DAMAGE_BOOST
            ));

    public static final DeferredBlock<Block> MONSTROUS_BEACON_BLOCK = registerBlock("monstrous_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    15098896,
                    MONSTROUS_BEACON_PEDESTAL_BLOCK,
                    MobEffects.HEALTH_BOOST
            ));

    public static final DeferredBlock<Block> STORM_BEACON_BLOCK = registerBlock("storm_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    10148061,
                    STORM_BEACON_PEDESTAL_BLOCK,
                    MobEffects.JUMP
            ));

    public static final DeferredBlock<Block> VOID_BEACON_BLOCK = registerBlock("void_beacon_block",
            () -> new BossBeaconBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> state.getValue(BossBeaconBlock.ACTIVE) ? 15 : 0),
                    6569060,
                    VOID_BEACON_PEDESTAL_BLOCK,
                    MobEffects.SATURATION
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
