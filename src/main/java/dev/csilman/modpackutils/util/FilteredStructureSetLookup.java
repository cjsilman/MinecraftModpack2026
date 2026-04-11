package dev.csilman.modpackutils.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class FilteredStructureSetLookup implements HolderLookup<StructureSet> {
    private final HolderLookup<StructureSet> delegate;
    private final Set<Holder.Reference<StructureSet>> allowed;

    public FilteredStructureSetLookup(HolderLookup<StructureSet> delegate,
                                      Set<Holder.Reference<StructureSet>> allowed) {
        this.delegate = delegate;
        this.allowed = allowed;
    }

    @Override
    public Stream<Holder.Reference<StructureSet>> listElements() {
        return allowed.stream();
    }

    @Override
    public Stream<HolderSet.Named<StructureSet>> listTags() {
        return Stream.empty();
    }

    @Override
    public Optional<Holder.Reference<StructureSet>> get(ResourceKey<StructureSet> key) {
        return delegate.get(key).filter(allowed::contains);
    }

    @Override
    public Optional<HolderSet.Named<StructureSet>> get(TagKey<StructureSet> tag) {
        return delegate.get(tag);
    }
}
