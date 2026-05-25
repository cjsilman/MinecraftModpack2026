package dev.csilman.modpackutils.lootmodifiers;

import com.mojang.serialization.MapCodec;
import dev.csilman.modpackutils.ModpackUtilsMod;
import dev.csilman.modpackutils.lootmodifiers.custom.BossLootModifier;
import dev.csilman.modpackutils.lootmodifiers.custom.ModpackLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ModpackUtilsMod.MOD_ID);

    public static final Supplier<MapCodec<ModpackLootModifier>> MODPACK_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("modpack_loot_modifier", () -> ModpackLootModifier.CODEC);

    public static final Supplier<MapCodec<BossLootModifier>> BOSS_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("boss_loot_modifier", () -> BossLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
