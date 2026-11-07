package com.sandevistanmod.registry;

import com.sandevistanmod.SandevistanMod;
import com.sandevistanmod.data.SandevistanData;
import com.sandevistanmod.logic.ChipItem;
import com.sandevistanmod.logic.ClinicBlock;
import com.sandevistanmod.logic.ImplantItem;
import com.sandevistanmod.logic.GoodMindEffect;
import com.sandevistanmod.logic.ImmunoItem;
import com.sandevistanmod.logic.StabilizerItem;
import com.sandevistanmod.logic.SandevistanItem;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModRegistries {
    private ModRegistries() {}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SandevistanMod.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, SandevistanMod.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, SandevistanMod.MODID);
    public static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, SandevistanMod.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, SandevistanMod.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SandevistanMod.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SandevistanMod.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SandevistanMod.MODID);

    public static final Supplier<AttachmentType<SandevistanData>> SANDEVISTAN =
            ATTACHMENTS.register("sandevistan",
                    () -> AttachmentType.serializable(() -> new SandevistanData()).copyOnDeath().build());

    private static Supplier<SoundEvent> sound(String name) {
        return SOUNDS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, name)));
    }

    public static final Supplier<SoundEvent> SND_ON = sound("sandevistan_on");
    public static final Supplier<SoundEvent> SND_OFF = sound("sandevistan_off");
    /** Sons de ligar/desligar de todos os Sandevistans, menos o Apogee. */
    public static final Supplier<SoundEvent> SND_ON_V = sound("sandevistan_on_v");
    public static final Supplier<SoundEvent> SND_OFF_V = sound("sandevistan_off_v");
    public static final Supplier<SoundEvent> TINNITUS = sound("tinnitus");
    public static final Supplier<SoundEvent> PSY_SND_1 = sound("psy_snd_1");
    public static final Supplier<SoundEvent> PSY_SND_2 = sound("psy_snd_2");
    public static final Supplier<SoundEvent> ALERT = sound("alert");
    public static final Supplier<SoundEvent> PSY_MUSIC_1 = sound("psy_music_1");
    public static final Supplier<SoundEvent> PSY_MUSIC_2 = sound("psy_music_2");

    private static DeferredItem<SandevistanItem> sandevistan(String name, int variant) {
        return ITEMS.register(name, () -> new SandevistanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), variant));
    }

    /** Modelos de Sandevistan na ordem: Apogee, Falcon, Dynalar, Zetatech, QianT. */
    public static final List<DeferredItem<SandevistanItem>> SANDEVISTANS = List.of(
            sandevistan("sandevistan", 0), sandevistan("sandevistan_falcon", 1), sandevistan("sandevistan_dynalar", 2),
            sandevistan("sandevistan_zetatech", 3), sandevistan("sandevistan_qiant", 4));
    public static final DeferredItem<SandevistanItem> SANDEVISTAN_ITEM = SANDEVISTANS.get(0);

    public static final DeferredHolder<MobEffect, GoodMindEffect> GOOD_MIND =
            EFFECTS.register("good_mind", () -> new GoodMindEffect());

    /** Seringa vazia: base do Imunossupressor e do Estabilizador Neural; sobra depois de injetar. */
    public static final DeferredItem<Item> SERINGA = ITEMS.registerSimpleItem("seringa", new Item.Properties().stacksTo(16));
    public static final DeferredItem<StabilizerItem> ESTABILIZADOR = ITEMS.register("estabilizador_neural",
            () -> new StabilizerItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<ImmunoItem> IMUNOSSUPRESSOR = ITEMS.register("imunossupressor",
            () -> new ImmunoItem(new Item.Properties().stacksTo(16)));

    public static final DeferredBlock<ClinicBlock> CLINICA = BLOCKS.register("clinica_ripperdoc",
            () -> new ClinicBlock(BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)
                    .lightLevel(s -> 7)));
    public static final DeferredItem<BlockItem> CLINICA_ITEM = ITEMS.register("clinica_ripperdoc",
            () -> new BlockItem(CLINICA.get(), new Item.Properties()));

    /** Posto de trabalho do Ripperdoc: o aldeao que pega o bloco da clinica vira o doutor. */
    public static final ResourceKey<PoiType> RIPPERDOC_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE,
            ResourceLocation.fromNamespaceAndPath(SandevistanMod.MODID, "ripperdoc"));
    public static final Supplier<PoiType> RIPPERDOC_POI = POIS.register("ripperdoc",
            () -> new PoiType(ImmutableSet.copyOf(CLINICA.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final Supplier<VillagerProfession> RIPPERDOC = PROFESSIONS.register("ripperdoc",
            () -> new VillagerProfession("ripperdoc",
                    holder -> holder.is(RIPPERDOC_POI_KEY), holder -> holder.is(RIPPERDOC_POI_KEY),
                    ImmutableSet.of(), ImmutableSet.of(), null));

    public static final DeferredItem<Item> CRISTAL = ITEMS.registerSimpleItem("cristal_neural", new Item.Properties());
    public static final DeferredItem<Item> FIBRA = ITEMS.registerSimpleItem("fibra_sinaptica", new Item.Properties());
    public static final DeferredItem<Item> VENTOINHA = ITEMS.registerSimpleItem("ventoinha", new Item.Properties());
    public static final DeferredItem<Item> NUCLEO = ITEMS.registerSimpleItem("nucleo", new Item.Properties());
    public static final DeferredItem<Item> SERVO = ITEMS.registerSimpleItem("servo_motor", new Item.Properties());
    public static final DeferredItem<Item> BATERIA = ITEMS.registerSimpleItem("bateria_energia", new Item.Properties());

    public static final DeferredItem<Item> CHIP = ITEMS.registerSimpleItem("chip", new Item.Properties());
    public static final DeferredItem<Item> NEURALINK = ITEMS.registerSimpleItem("neuralink", new Item.Properties());
    public static final DeferredItem<Item> ESPINHA = ITEMS.registerSimpleItem("espinha", new Item.Properties());
    public static final DeferredItem<Item> ACELERADOR = ITEMS.registerSimpleItem("acelerador", new Item.Properties());

    /** Pecas coloridas de cada modelo (Falcon, Dynalar, Zetatech, QianT): Neuralink, Espinha e Acelerador. */
    public static final List<String> VARIANT_IDS = List.of("falcon", "dynalar", "zetatech", "qiant");
    public static final List<DeferredItem<Item>> VARIANT_NEURALINK = variantParts("neuralink");
    public static final List<DeferredItem<Item>> VARIANT_ESPINHA = variantParts("espinha");
    public static final List<DeferredItem<Item>> VARIANT_ACELERADOR = variantParts("acelerador");

    private static List<DeferredItem<Item>> variantParts(String part) {
        java.util.ArrayList<DeferredItem<Item>> out = new java.util.ArrayList<>();
        for (String v : VARIANT_IDS) out.add(ITEMS.registerSimpleItem(part + "_" + v, new Item.Properties()));
        return List.copyOf(out);
    }

    private static DeferredItem<ImplantItem> implant(String name, int slot, int tier, String... tips) {
        return ITEMS.register(name, () -> new ImplantItem(new Item.Properties().stacksTo(1).rarity(
                tier == 3 ? Rarity.EPIC : Rarity.RARE), slot, tier, tips));
    }

    public static final DeferredItem<ImplantItem> BRACO_FERRO = implant("braco_ferro", SandevistanData.SLOT_ARM_R, 1,
            "tooltip.arm.1", "tooltip.arm.2", "tooltip.arm.3");
    public static final DeferredItem<ImplantItem> LINK_NEURAL = implant("link_neural", SandevistanData.SLOT_NEURAL, 1,
            "tooltip.neural.1", "tooltip.neural.2");
    public static final DeferredItem<ImplantItem> ESTRUTURA_OSSEA = implant("estrutura_ossea", SandevistanData.SLOT_BONE, 1,
            "tooltip.bone.1", "tooltip.bone.2");
    public static final DeferredItem<ImplantItem> CORACAO_FERRO = implant("coracao_ferro", SandevistanData.SLOT_HEART, 1,
            "tooltip.heart.1", "tooltip.heart.2");
    public static final DeferredItem<ImplantItem> PERNAS = implant("pernas_ciberneticas", SandevistanData.SLOT_LEG_R, 1,
            "tooltip.leg.1", "tooltip.leg.2", "tooltip.leg.3");
    public static final DeferredItem<ImplantItem> SISTEMA_NERVOSO = implant("sistema_nervoso", SandevistanData.SLOT_NERVE, 1,
            "tooltip.nerve.1", "tooltip.nerve.2");

    /** Tiers II e III de cada implante (indice 0 = tier II, 1 = tier III). */
    public static final List<DeferredItem<ImplantItem>> BRACO_FERRO_T = List.of(
            implant("braco_ferro_2", SandevistanData.SLOT_ARM_R, 2, "tooltip.arm.1", "tooltip.arm.2", "tooltip.arm.3"),
            implant("braco_ferro_3", SandevistanData.SLOT_ARM_R, 3, "tooltip.arm.1", "tooltip.arm.2", "tooltip.arm.3"));
    public static final List<DeferredItem<ImplantItem>> ESTRUTURA_OSSEA_T = List.of(
            implant("estrutura_ossea_2", SandevistanData.SLOT_BONE, 2, "tooltip.bone.1", "tooltip.bone.2"),
            implant("estrutura_ossea_3", SandevistanData.SLOT_BONE, 3, "tooltip.bone.1", "tooltip.bone.2"));
    public static final List<DeferredItem<ImplantItem>> CORACAO_FERRO_T = List.of(
            implant("coracao_ferro_2", SandevistanData.SLOT_HEART, 2, "tooltip.heart.1", "tooltip.heart.2"),
            implant("coracao_ferro_3", SandevistanData.SLOT_HEART, 3, "tooltip.heart.1", "tooltip.heart.2"));
    public static final List<DeferredItem<ImplantItem>> PERNAS_T = List.of(
            implant("pernas_ciberneticas_2", SandevistanData.SLOT_LEG_R, 2, "tooltip.leg.1", "tooltip.leg.2", "tooltip.leg.3"),
            implant("pernas_ciberneticas_3", SandevistanData.SLOT_LEG_R, 3, "tooltip.leg.1", "tooltip.leg.2", "tooltip.leg.3"));
    public static final List<DeferredItem<ImplantItem>> SISTEMA_NERVOSO_T = List.of(
            implant("sistema_nervoso_2", SandevistanData.SLOT_NERVE, 2, "tooltip.nerve.1", "tooltip.nerve.2"),
            implant("sistema_nervoso_3", SandevistanData.SLOT_NERVE, 3, "tooltip.nerve.1", "tooltip.nerve.2"));

    public static final DeferredItem<Item> CHIPWARE = ITEMS.registerSimpleItem("chipware", new Item.Properties());

    private static DeferredItem<ChipItem> chip(String name, int type, int tier, String effectKey) {
        return ITEMS.register(name, () -> new ChipItem(new Item.Properties().durability(SandevistanData.chipMax(type, tier))
                .rarity(tier == 3 ? Rarity.RARE : Rarity.UNCOMMON), type, tier, effectKey));
    }

    /** Chips tier I na ordem dos tipos 1 a 4: Artes Marciais, Supressao de Dor, Mineracao, Andarilho. */
    public static final List<DeferredItem<ChipItem>> CHIPS = List.of(
            chip("chip_artes_marciais", SandevistanData.CHIP_MARTIAL, 1, "tooltip.chip.martial"),
            chip("chip_supressao_dor", SandevistanData.CHIP_PAIN, 1, "tooltip.chip.pain"),
            chip("chip_mineracao", SandevistanData.CHIP_MINING, 1, "tooltip.chip.mining"),
            chip("chip_andarilho", SandevistanData.CHIP_WALKER, 1, "tooltip.chip.walker"),
            chip("chip_ram", SandevistanData.CHIP_RAM, 1, "tooltip.chip.ram"));
    public static final List<DeferredItem<ChipItem>> CHIPS_RAM_T = List.of(
            chip("chip_ram_2", SandevistanData.CHIP_RAM, 2, "tooltip.chip.ram"),
            chip("chip_ram_3", SandevistanData.CHIP_RAM, 3, "tooltip.chip.ram"));

    /** Tiers II e III dos chips que tem tiers: Artes Marciais, Supressao de Dor e Andarilho. */
    public static final List<DeferredItem<ChipItem>> CHIPS_MARTIAL_T = List.of(
            chip("chip_artes_marciais_2", SandevistanData.CHIP_MARTIAL, 2, "tooltip.chip.martial"),
            chip("chip_artes_marciais_3", SandevistanData.CHIP_MARTIAL, 3, "tooltip.chip.martial"));
    public static final List<DeferredItem<ChipItem>> CHIPS_PAIN_T = List.of(
            chip("chip_supressao_dor_2", SandevistanData.CHIP_PAIN, 2, "tooltip.chip.pain"),
            chip("chip_supressao_dor_3", SandevistanData.CHIP_PAIN, 3, "tooltip.chip.pain"));
    public static final List<DeferredItem<ChipItem>> CHIPS_WALKER_T = List.of(
            chip("chip_andarilho_2", SandevistanData.CHIP_WALKER, 2, "tooltip.chip.walker"),
            chip("chip_andarilho_3", SandevistanData.CHIP_WALKER, 3, "tooltip.chip.walker"));

    /** Item do chip deste tipo (1 a 4) e tier (1 a 3). */
    public static Item chipItem(int type, int tier) {
        int t = SandevistanData.chipHasTiers(type) ? SandevistanData.clampTier(tier) : 1;
        if (t == 1) return CHIPS.get(type - 1).get();
        List<DeferredItem<ChipItem>> l = type == SandevistanData.CHIP_MARTIAL ? CHIPS_MARTIAL_T
                : type == SandevistanData.CHIP_PAIN ? CHIPS_PAIN_T
                : type == SandevistanData.CHIP_RAM ? CHIPS_RAM_T : CHIPS_WALKER_T;
        return l.get(t - 2).get();
    }

    /** Item do implante dos encaixes com tiers (tier 1 a 3); null se o encaixe nao tem esse item. */
    public static Item tierImplant(int slot, int tier) {
        int t = SandevistanData.clampTier(tier);
        switch (slot) {
            case SandevistanData.SLOT_ARM_R, SandevistanData.SLOT_ARM_L:
                return t == 1 ? BRACO_FERRO.get() : BRACO_FERRO_T.get(t - 2).get();
            case SandevistanData.SLOT_BONE:
                return t == 1 ? ESTRUTURA_OSSEA.get() : ESTRUTURA_OSSEA_T.get(t - 2).get();
            case SandevistanData.SLOT_NERVE:
                return t == 1 ? SISTEMA_NERVOSO.get() : SISTEMA_NERVOSO_T.get(t - 2).get();
            case SandevistanData.SLOT_HEART:
                return t == 1 ? CORACAO_FERRO.get() : CORACAO_FERRO_T.get(t - 2).get();
            case SandevistanData.SLOT_LEG_R, SandevistanData.SLOT_LEG_L:
                return t == 1 ? PERNAS.get() : PERNAS_T.get(t - 2).get();
            default:
                return null;
        }
    }

    public static final Supplier<CreativeModeTab> TAB = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.sandevistan"))
                    .icon(() -> new ItemStack(SANDEVISTAN_ITEM.get()))
                    .displayItems((params, out) -> {
                        out.accept(CHIP.get());
                        out.accept(CRISTAL.get());
                        out.accept(FIBRA.get());
                        out.accept(VENTOINHA.get());
                        out.accept(NUCLEO.get());
                        out.accept(SERVO.get());
                        out.accept(BATERIA.get());
                        out.accept(CLINICA_ITEM.get());
                        out.accept(NEURALINK.get());
                        out.accept(ESPINHA.get());
                        out.accept(ACELERADOR.get());
                        for (DeferredItem<Item> it : VARIANT_NEURALINK) out.accept(it.get());
                        for (DeferredItem<Item> it : VARIANT_ESPINHA) out.accept(it.get());
                        for (DeferredItem<Item> it : VARIANT_ACELERADOR) out.accept(it.get());
                        for (DeferredItem<SandevistanItem> it : SANDEVISTANS) out.accept(it.get());
                        out.accept(ESTRUTURA_OSSEA.get());
                        out.accept(SISTEMA_NERVOSO.get());
                        out.accept(CORACAO_FERRO.get());
                        out.accept(PERNAS.get());
                        out.accept(BRACO_FERRO.get());
                        out.accept(LINK_NEURAL.get());
                        out.accept(CHIPWARE.get());
                        for (DeferredItem<ChipItem> c : CHIPS) out.accept(c.get());
                        for (DeferredItem<ImplantItem> it : BRACO_FERRO_T) out.accept(it.get());
                        for (DeferredItem<ImplantItem> it : PERNAS_T) out.accept(it.get());
                        for (DeferredItem<ImplantItem> it : ESTRUTURA_OSSEA_T) out.accept(it.get());
                        for (DeferredItem<ImplantItem> it : SISTEMA_NERVOSO_T) out.accept(it.get());
                        for (DeferredItem<ImplantItem> it : CORACAO_FERRO_T) out.accept(it.get());
                        for (DeferredItem<ChipItem> c : CHIPS_MARTIAL_T) out.accept(c.get());
                        for (DeferredItem<ChipItem> c : CHIPS_PAIN_T) out.accept(c.get());
                        for (DeferredItem<ChipItem> c : CHIPS_WALKER_T) out.accept(c.get());
                        for (DeferredItem<ChipItem> c : CHIPS_RAM_T) out.accept(c.get());
                        out.accept(SERINGA.get());
                        out.accept(ESTABILIZADOR.get());
                        out.accept(IMUNOSSUPRESSOR.get());
                    })
                    .build());
}
