package dev.csilman.modpackutils.lootmodifiers.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ModpackLootModifier extends LootModifier {

    public static final MapCodec<ModpackLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(inst.group(
                    Codec.STRING.fieldOf("field1").forGetter(e -> e.field1),
                    Codec.INT.fieldOf("quantity").forGetter(e -> e.quantity),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item)
            )).apply(inst, ModpackLootModifier::new));

    private final String field1;
    private final int quantity;
    private final Item item;

    protected ModpackLootModifier(LootItemCondition[] conditionsIn, String field1, int quantity, Item item) {
        super(conditionsIn);
        this.field1 = field1;
        this.quantity = quantity;
        this.item = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {

        ItemStack itemStack = new ItemStack(item, quantity);


        generatedLoot.add(itemStack);

        generatedLoot.add(new ItemStack(Items.STONE, 1));

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
