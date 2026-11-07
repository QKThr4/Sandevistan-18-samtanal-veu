package com.sandevistanmod.data;

import com.sandevistanmod.registry.ModRegistries;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

/** Estado dos implantes de um jogador (salvo no jogador e copiado ao morrer). */
public class SandevistanData implements INBTSerializable<CompoundTag> {
    public static final float MAX_MIND = 100f;
    /** Tempo, em segundos reais, que a barra de Mente cheia dura com o Sandevistan ligado. */
    public static final float USE_SECONDS = 30f;
    /** Tempo, em segundos reais, para a barra encher do zero (1 segundo de uso a cada 3 segundos parado) com o Sandevistan desligado. */
    public static final float REGEN_SECONDS = 90f;
    /** Mente minima para conseguir ligar o Sandevistan. */
    public static final float MIN_TO_START = 10f;
    /** Tick rate do jogo inteiro enquanto o Sandevistan esta ligado. */
    public static final int SLOW_TICK_RATE = 3;

    /** Ciberpsicose maxima guardada (a barra mostra 100%, mas ela pode passar disso). */
    public static final float MAX_PSY = 300f;
    /** A cada 50 de Mente gasta (50% de uso) o jogador ganha 5 de ciberpsicose. */
    public static final float PSY_USE_STEP = 50f;
    public static final float PSY_PER_STEP = 5f;
    /** Segundos parado (sem ligar o Sandevistan) ate os efeitos negativos comecarem. */
    public static final float PSY_IDLE_SECONDS = 0f;
    /** Duracao do efeito Mente Boa do remedio e tempo (s) para regenerar 1 de Mente com ele. */
    public static final float GOOD_MIND_SECONDS = 60f;
    public static final float GOOD_MIND_REGEN_SECONDS = 30f;

    // ---- implantes: encaixes 0 costas (Sandevistan), 1 braco direito, 2 braco esquerdo, 3 link neural ----
    public static final int SLOT_BACK = 0;
    public static final int SLOT_ARM_R = 1;
    public static final int SLOT_ARM_L = 2;
    public static final int SLOT_NEURAL = 3;
    public static final int SLOT_BONE = 4;
    public static final int SLOT_NERVE = 5;
    public static final int SLOT_HEART = 6;
    public static final int SLOT_LEG_R = 7;
    public static final int SLOT_LEG_L = 8;
    public static final int SLOT_COUNT = 9;

    /** Quanto cada implante soma na Carga Cibernetica (%) e cada chip. */
    public static final float LOAD_SANDEVISTAN = 30f;
    public static final float LOAD_ARM = 10f;
    public static final float LOAD_NEURAL = 0f;
    /** Estrutura Ossea Reforcada e Sistema Nervoso Aprimorado. */
    public static final float LOAD_BONE = 5f;
    public static final float LOAD_NERVE = 5f;
    /** Coracao de Ferro (5%) e cada Perna Cibernetica (10%). */
    public static final float LOAD_HEART = 5f;
    public static final float LOAD_LEG = 10f;
    public static final float LOAD_CHIP = 5f;
    /** Mente permanente que cada chip inserido tira e segundos para ele baixar antes de funcionar. */
    public static final float CHIP_MIND = 5f;
    public static final float CHIP_LOAD_SECONDS = 10f;
    /** Duracao de cada Imunossupressor (s). */
    public static final float DOSE_SECONDS = 60f;

    public static final int CHIP_MARTIAL = 1;
    public static final int CHIP_PAIN = 2;
    public static final int CHIP_MINING = 3;
    public static final int CHIP_WALKER = 4;
    public static final int CHIP_RAM = 5;
    public static final int CHIP_TYPES = 5;
    /** Carga a partir da qual a ciberpsicose por carga comeca (%) e quanto o Chip de RAM (tier I/II/III) soma a ela. */
    public static final float BASE_LOAD_THRESHOLD = 70f;
    public static final float[] RAM_BONUS = {5f, 7f, 10f};
    /** Durabilidade de cada chip por tipo e tier (1 uso = 1 de durabilidade); o de Mineracao nao tem tiers. */
    private static final int[][] CHIP_DURABILITY = {
            {0, 0, 0}, {800, 900, 1000}, {500, 600, 700}, {2000, 2000, 2000}, {400, 500, 600}, {600, 600, 600}};
    /** Mente permanente que cada chip tira, por tier. */
    private static final float[] CHIP_MIND_TIER = {5f, 4f, 3f};

    public static final int MAX_TIER = 3;

    public static int clampTier(int t) {
        return Math.max(1, Math.min(MAX_TIER, t));
    }

    /** Este tipo de chip tem tiers? (so o de Mineracao nao tem) */
    public static boolean chipHasTiers(int type) {
        return type == CHIP_MARTIAL || type == CHIP_PAIN || type == CHIP_WALKER || type == CHIP_RAM;
    }

    public static int chipMax(int type) {
        return chipMax(type, 1);
    }

    public static int chipMax(int type, int tier) {
        return type >= 1 && type <= CHIP_TYPES ? CHIP_DURABILITY[type][clampTier(tier) - 1] : 0;
    }

    public static float chipMindOf(int tier) {
        return CHIP_MIND_TIER[clampTier(tier) - 1];
    }

    /** Mente permanente que o chip tira: os tiers baixam o custo, menos no Chip de RAM (sempre 5). */
    public static float chipMindFor(int type, int tier) {
        return type == CHIP_RAM ? CHIP_MIND : chipMindOf(tier);
    }

    /** Carga Cibernetica que o chip soma: o Chip de RAM nao soma nada (senao anularia o proprio bonus). */
    public static float chipLoadOf(int type) {
        return type == CHIP_RAM ? 0f : LOAD_CHIP;
    }

    /** Bonus (%) que o Chip de RAM deste tier da ao limite de carga. */
    public static float ramBonus(int tier) {
        return RAM_BONUS[clampTier(tier) - 1];
    }

    /** Este encaixe de implante tem tiers? (Sandevistan e Link Neural nao tem) */
    public static boolean slotHasTiers(int slot) {
        return slot != SLOT_BACK && slot != SLOT_NEURAL;
    }

    // ---- valores por tier (indice 0 = tier I) ----
    public static final double[] ARM_DAMAGE = {2.0, 2.5, 3.0};
    public static final double[] LEG_SPEED = {0.15, 0.18, 0.20};
    public static final double[] LEG_JUMP = {0.04, 0.05, 0.06};
    public static final double[] LEG_FALL = {0.15, 0.18, 0.21};
    public static final double[] BONE_ARMOR = {3.0, 4.0, 5.0};
    public static final double[] BONE_KB = {0.25, 0.30, 0.35};
    public static final double[] BONE_FALL = {0.30, 0.35, 0.40};
    public static final double[] NERVE_SPEED = {0.08, 0.10, 0.12};
    public static final double[] NERVE_ATTACK = {0.12, 0.15, 0.18};
    public static final double[] HEART_HP = {10.0, 12.0, 14.0};
    public static final double[] HEART_HEAL = {0.10, 0.15, 0.20};
    private static final float[] LOAD_BIG_T = {10f, 12f, 14f};
    private static final float[] LOAD_SMALL_T = {5f, 6f, 7f};

    public static double at(double[] arr, int tier) {
        return arr[clampTier(tier) - 1];
    }

    /** Texto de um numero para a interface: 2, 2.5, 3. */
    public static String num(double v) {
        return v == Math.rint(v) ? Integer.toString((int) v) : Double.toString(v);
    }

    /** Percentual inteiro de uma fracao (0.18 -> 18). */
    public static int pct(double v) {
        return (int) Math.round(v * 100.0);
    }

    public static float loadOf(int slot) {
        return loadOf(slot, 1);
    }

    public static float loadOf(int slot, int tier) {
        int i = clampTier(tier) - 1;
        return switch (slot) {
            case SLOT_BACK -> LOAD_SANDEVISTAN;
            case SLOT_ARM_R, SLOT_ARM_L, SLOT_LEG_R, SLOT_LEG_L -> LOAD_BIG_T[i];
            case SLOT_BONE, SLOT_NERVE, SLOT_HEART -> LOAD_SMALL_T[i];
            default -> LOAD_NEURAL;
        };
    }

    /** Argumentos dos textos de efeito de cada implante neste tier (para tooltips e aba de efeitos). */
    public static Object[] statArgs(int slot, int tier) {
        switch (slot) {
            case SLOT_ARM_R, SLOT_ARM_L:
                return new Object[] {num(at(ARM_DAMAGE, tier)), num(at(ARM_DAMAGE, tier) * 2)};
            case SLOT_LEG_R, SLOT_LEG_L:
                return new Object[] {pct(at(LEG_SPEED, tier)), num(at(LEG_JUMP, tier)), pct(at(LEG_FALL, tier))};
            case SLOT_BONE:
                return new Object[] {(int) at(BONE_ARMOR, tier), pct(at(BONE_KB, tier)), pct(at(BONE_FALL, tier))};
            case SLOT_NERVE:
                return new Object[] {pct(at(NERVE_SPEED, tier)), pct(at(NERVE_ATTACK, tier))};
            case SLOT_HEART:
                return new Object[] {(int) (at(HEART_HP, tier) / 2), pct(at(HEART_HEAL, tier))};
            default:
                return new Object[0];
        }
    }

    /** Carga que o Sandevistan soma, por modelo (0 Apogee ... 4 QianT). */
    public static float loadOfVariant(int variant) {
        return SandevistanVariant.of(variant).load;
    }

    /** True depois que o video da primeira vez ja tocou para este implante das costas. */
    public boolean introSeen;
    public boolean implanted;
    public boolean armR;
    public boolean armL;
    public boolean neural;
    public boolean bone;
    public boolean nerve;
    public boolean heart;
    public boolean legR;
    public boolean legL;
    /** Modelo do Sandevistan implantado: 0 Apogee, 1 Falcon, 2 Dynalar, 3 Zetatech, 4 QianT. */
    public int variant;
    /** Os bracos de ferro aparecem no modelo do jogador. */
    public boolean armsVisible = true;
    /** As pernas de ferro aparecem no modelo do jogador. */
    public boolean legsVisible = true;
    /** Dois encaixes de chip no Link Neural: tipo (0 vazio), dano de durabilidade e segundos que faltam para baixar. */
    public final int[] chip = new int[2];
    public final int[] chipDmg = new int[2];
    /** Tier (1 a 3) de cada chip inserido e de cada implante implantado (por encaixe). */
    public final int[] chipTier = {1, 1};
    public final int[] tier = {1, 1, 1, 1, 1, 1, 1, 1, 1};
    public final float[] chipLoad = new float[2];

    public boolean active;
    public float mind = MAX_MIND;
    /** Ciberpsicose (0 a 300; 100 = limite da barra) e Mente Boa restante (segundos reais). */
    public float psy;
    public float goodMind;
    /** Mente perdida PARA SEMPRE pela ciberpsicose (a Mente maxima cai); so o Imunossupressor recupera. */
    public float mindLost;
    /** Doses de Imunossupressor ativas: {segundos restantes, ciberpsicose que volta quando acabar}. Cada uma tem o seu tempo. */
    public final List<float[]> doses = new ArrayList<>();
    /** Apagao da ciberpsicose (a partir de 70%): o jogador nao ve nada e age sozinho. Enviado ao cliente. */
    public boolean blind;
    /** Sequencia de Imunossupressores: quantos seguidos e segundos restantes para a sequencia quebrar. */
    public int immuSeq;
    public float immuSeqT;
    /** Mente gasta ainda nao convertida em ciberpsicose. */
    public float useAcc;
    /** Sintomas ativos agora (parou de usar ha mais de 5 s): enviado ao cliente para os efeitos de tela. */
    public boolean psychotic;

    // temporizadores so do servidor (nao salvos)
    public transient float idle;
    public transient float decayT;
    public transient float drainT;
    public transient float secT;
    public transient float goodT;
    public transient boolean zeroWarned;
    public transient float zeroT;
    public transient float zeroDmgT;
    public transient float riseT;
    public transient float goodHealT;
    public transient float blindT;
    public transient float forceT;
    public transient float zeroHitCd;
    public transient float loadT;
    public transient float ramT;
    /** Temporizadores dos sintomas da Mente baixa (so do servidor). */
    public final transient float[] lowT = new float[8];
    public transient int swapA = -1;
    public transient int swapB = -1;
    public transient ItemStack swapRefA = ItemStack.EMPTY;
    public transient ItemStack swapRefB = ItemStack.EMPTY;

    public static SandevistanData get(Player p) {
        return p.getData(ModRegistries.SANDEVISTAN);
    }

    public boolean has(int slot) {
        return switch (slot) {
            case SLOT_BACK -> implanted;
            case SLOT_ARM_R -> armR;
            case SLOT_ARM_L -> armL;
            case SLOT_BONE -> bone;
            case SLOT_NERVE -> nerve;
            case SLOT_HEART -> heart;
            case SLOT_LEG_R -> legR;
            case SLOT_LEG_L -> legL;
            default -> neural;
        };
    }

    public int tierOf(int slot) {
        return slot >= 0 && slot < SLOT_COUNT && slotHasTiers(slot) ? clampTier(tier[slot]) : 1;
    }

    public void set(int slot, boolean value, int newTier) {
        set(slot, value);
        tier[slot] = value && slotHasTiers(slot) ? clampTier(newTier) : 1;
    }

    public void set(int slot, boolean value) {
        switch (slot) {
            case SLOT_BACK -> implanted = value;
            case SLOT_ARM_R -> armR = value;
            case SLOT_ARM_L -> armL = value;
            case SLOT_BONE -> bone = value;
            case SLOT_NERVE -> nerve = value;
            case SLOT_HEART -> heart = value;
            case SLOT_LEG_R -> legR = value;
            case SLOT_LEG_L -> legL = value;
            default -> neural = value;
        }
    }

    /** Mente maxima PERMANENTE (100 menos o que a ciberpsicose tirou; os chips sao removiveis e nao contam). */
    public float permMind() {
        return Math.max(0f, MAX_MIND - mindLost);
    }

    /** Sintomas da Mente baixa: 0 nenhum, 1 abaixo de 60%, 2 abaixo de 30%, 3 abaixo de 10%. */
    public int lowTier() {
        float m = permMind();
        return m < 10f ? 3 : m < 30f ? 2 : m < 60f ? 1 : 0;
    }

    /** Ciberpsicose a partir da qual forca, nausea e tela tremendo comecam (15%, ou 10% com a Mente muito baixa). */
    public float effectStart() {
        return lowTier() >= 2 ? 10f : 15f;
    }

    public boolean hasAny() {
        return implanted || armR || armL || neural || bone || nerve || heart || legR || legL;
    }

    public int arms() {
        return (armR ? 1 : 0) + (armL ? 1 : 0);
    }

    public int legs() {
        return (legR ? 1 : 0) + (legL ? 1 : 0);
    }

    public int chips() {
        return (chip[0] != 0 ? 1 : 0) + (chip[1] != 0 ? 1 : 0);
    }

    public SandevistanVariant variant() {
        return SandevistanVariant.of(variant);
    }

    /** Carga Cibernetica (%): Sandevistan 10 a 30 (pelo modelo), cada braco 10, cada chip 5, osso e nervos 5, Link Neural 0. */
    public float capacity() {
        float sum = implanted ? variant().load : 0f;
        for (int i = 0; i < 2; i++) if (chip[i] != 0) sum += chipLoadOf(chip[i]);
        for (int s = 1; s < SLOT_COUNT; s++) if (has(s)) sum += loadOf(s, tierOf(s));
        return sum;
    }

    /** Carga (%) a partir da qual a ciberpsicose por carga comeca: 70, mais o bonus do Chip de RAM ativo. */
    public float loadThreshold() {
        for (int i = 0; i < 2; i++) {
            if (chip[i] == CHIP_RAM && chipActive(CHIP_RAM)) return BASE_LOAD_THRESHOLD + ramBonus(chipTier[i]);
        }
        return BASE_LOAD_THRESHOLD;
    }

    /** Este tipo de chip esta inserido, ja baixou e ainda tem durabilidade? */
    public boolean chipActive(int type) {
        for (int i = 0; i < 2; i++) {
            if (chip[i] == type && chipLoad[i] <= 0f && chipDmg[i] < chipMax(type, chipTier[i])) return true;
        }
        return false;
    }

    /** Encaixe (0 ou 1) onde esta o chip deste tipo, ou -1. */
    public int chipSlot(int type) {
        for (int i = 0; i < 2; i++) if (chip[i] == type) return i;
        return -1;
    }

    /** Mente permanente que os chips inseridos tiram. */
    public float chipMind() {
        float sum = 0f;
        for (int i = 0; i < 2; i++) if (chip[i] != 0) sum += chipMindFor(chip[i], chipTier[i]);
        return sum;
    }

    /** Mente maxima atual (100 menos o que a ciberpsicose e os chips tiraram). */
    public float maxMind() {
        return Math.max(0f, Math.min(MAX_MIND, MAX_MIND - mindLost - chipMind()));
    }

    /** Segundos reais restantes de uso com a Mente atual. */
    public int secondsLeft() {
        return Math.max(0, (int) Math.ceil(mind / (MAX_MIND / variant().seconds)));
    }

    /**
     * Codigo enviado aos outros jogadores: bit 1 Sandevistan, bit 2 ligado, bit 4 com sintomas, bits 3 a 9 ciberpsicose (0 a 100),
     * bit 10 braco direito, bit 11 braco esquerdo, bit 12 Link Neural, bit 13 bracos visiveis, bits 14 a 16 modelo do Sandevistan, bit 17 perna direita, bit 18 perna esquerda.
     */
    public int stateCode() {
        int level = Math.max(0, Math.min(100, (int) Math.floor(psy)));
        boolean sick = psychotic && psy > 0f;
        return (implanted ? 1 : 0) | (active && implanted ? 2 : 0) | (sick ? 4 : 0) | ((sick ? level : 0) << 3)
                | (armR ? 1 << 10 : 0) | (armL ? 1 << 11 : 0) | (neural ? 1 << 12 : 0) | (armsVisible ? 1 << 13 : 0)
                | ((variant & 7) << 14) | (legR ? 1 << 17 : 0) | (legL ? 1 << 18 : 0) | (legsVisible ? 1 << 19 : 0);
    }

    /** Muda quando algo que o cliente precisa ver mudou (usado para decidir se reenvia os dados). */
    public long syncSig() {
        long h = 17L;
        h = h * 31L + (long) Math.floor(psy);
        h = h * 31L + (long) Math.ceil(goodMind);
        h = h * 31L + (psychotic ? 1 : 0);
        h = h * 31L + (blind ? 1 : 0);
        h = h * 31L + (long) Math.floor(mindLost);
        h = h * 31L + (implanted ? 1 : 0) + (armR ? 2 : 0) + (armL ? 4 : 0) + (neural ? 8 : 0) + (armsVisible ? 16 : 0)
                + (bone ? 32 : 0) + (nerve ? 64 : 0) + (heart ? 128 : 0) + (legR ? 256 : 0) + (legL ? 512 : 0) + (legsVisible ? 2048 : 0) + variant * 4096L;
        for (int i = 0; i < 2; i++) {
            h = h * 31L + chip[i];
            h = h * 31L + chipDmg[i];
            h = h * 31L + chipTier[i];
            h = h * 31L + (long) Math.ceil(chipLoad[i]);
        }
        for (int s = 0; s < SLOT_COUNT; s++) h = h * 31L + tier[s];
        h = h * 31L + doses.size();
        for (float[] ds : doses) h = h * 31L + (long) Math.ceil(ds[0]);
        return h;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag t = new CompoundTag();
        t.putBoolean("implanted", implanted);
        t.putBoolean("intro", introSeen);
        t.putBoolean("armR", armR);
        t.putBoolean("armL", armL);
        t.putBoolean("neural", neural);
        t.putBoolean("bone", bone);
        t.putBoolean("nerve", nerve);
        t.putBoolean("heart", heart);
        t.putBoolean("legR", legR);
        t.putBoolean("legL", legL);
        t.putInt("var", variant);
        t.putBoolean("armvis", armsVisible);
        t.putBoolean("legvis", legsVisible);
        t.putIntArray("chip", new int[] {chip[0], chip[1]});
        t.putIntArray("chipd", new int[] {chipDmg[0], chipDmg[1]});
        t.putIntArray("chipt", new int[] {chipTier[0], chipTier[1]});
        t.putIntArray("tiers", tier.clone());
        t.putFloat("chipl0", chipLoad[0]);
        t.putFloat("chipl1", chipLoad[1]);
        t.putBoolean("active", active);
        t.putFloat("mind", mind);
        t.putFloat("psy", psy);
        t.putFloat("lost", mindLost);
        ListTag list = new ListTag();
        for (float[] ds : doses) {
            CompoundTag c = new CompoundTag();
            c.putFloat("t", ds[0]);
            c.putFloat("p", ds[1]);
            list.add(c);
        }
        t.put("doses", list);
        t.putBoolean("blind", blind);
        t.putInt("is", immuSeq);
        t.putFloat("ist", immuSeqT);
        t.putFloat("good", goodMind);
        t.putFloat("acc", useAcc);
        t.putBoolean("psychotic", psychotic);
        return t;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag t) {
        implanted = t.getBoolean("implanted");
        introSeen = !t.contains("intro") || t.getBoolean("intro");
        armR = t.getBoolean("armR");
        armL = t.getBoolean("armL");
        neural = t.getBoolean("neural");
        bone = t.getBoolean("bone");
        nerve = t.getBoolean("nerve");
        heart = t.getBoolean("heart");
        legR = t.getBoolean("legR");
        legL = t.getBoolean("legL");
        variant = Math.max(0, Math.min(SandevistanVariant.values().length - 1, t.getInt("var")));
        armsVisible = !t.contains("armvis") || t.getBoolean("armvis");
        legsVisible = !t.contains("legvis") || t.getBoolean("legvis");
        int[] c = t.getIntArray("chip");
        int[] cd = t.getIntArray("chipd");
        int[] ct = t.getIntArray("chipt");
        int[] tr = t.getIntArray("tiers");
        for (int s = 0; s < SLOT_COUNT; s++) tier[s] = s < tr.length && slotHasTiers(s) ? clampTier(tr[s]) : 1;
        for (int i = 0; i < 2; i++) {
            chip[i] = i < c.length && c[i] >= 1 && c[i] <= CHIP_TYPES ? c[i] : 0;
            chipTier[i] = chip[i] != 0 && i < ct.length && chipHasTiers(chip[i]) ? clampTier(ct[i]) : 1;
            chipDmg[i] = chip[i] != 0 && i < cd.length ? Math.max(0, Math.min(chipMax(chip[i], chipTier[i]), cd[i])) : 0;
            chipLoad[i] = chip[i] != 0 ? Math.max(0f, t.getFloat("chipl" + i)) : 0f;
        }
        active = t.getBoolean("active");
        mind = t.contains("mind") ? t.getFloat("mind") : MAX_MIND;
        psy = t.getFloat("psy");
        mindLost = Math.max(0f, Math.min(MAX_MIND, t.getFloat("lost")));
        doses.clear();
        ListTag list = t.getList("doses", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag d = list.getCompound(i);
            float left = d.getFloat("t");
            if (left > 0f) doses.add(new float[] {left, Math.max(0f, d.getFloat("p"))});
        }
        blind = t.getBoolean("blind");
        immuSeq = Math.max(0, t.getInt("is"));
        immuSeqT = Math.max(0f, t.getFloat("ist"));
        goodMind = t.getFloat("good");
        useAcc = t.getFloat("acc");
        psychotic = t.getBoolean("psychotic");
    }
}
