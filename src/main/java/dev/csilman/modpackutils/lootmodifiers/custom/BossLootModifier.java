package dev.csilman.modpackutils.lootmodifiers.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class BossLootModifier extends LootModifier {

    public static final MapCodec<BossLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(inst.group(
                    Codec.INT.fieldOf("quantity").forGetter(e -> e.quantity),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item2").forGetter(e -> e.item2)
            )).apply(inst, BossLootModifier::new));

    private final int quantity;
    private final Item item;
    private final Item item2;

    protected BossLootModifier(LootItemCondition[] conditionsIn, int quantity, Item item, Item item2) {
        super(conditionsIn);
        this.quantity = quantity;
        this.item = item;
        this.item2 = item2;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        ItemStack itemStack = new ItemStack(item, quantity);
        ItemStack itemStack2 = new ItemStack(item2, 1);

        generatedLoot.add(itemStack);
        generatedLoot.add(itemStack2);

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
