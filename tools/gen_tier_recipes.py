"""Gera as receitas de melhoria de tier: o implante (ou chip) do tier anterior fica no CENTRO da mesa de trabalho."""
import json
import os

OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "src", "main", "resources", "data", "sandevistan", "recipe")

IMPLANTS = {  # nome: item tematico
    "braco_ferro": "minecraft:iron_block",
    "pernas_ciberneticas": "minecraft:piston",
    "estrutura_ossea": "minecraft:bone_block",
    "sistema_nervoso": "sandevistan:fibra_sinaptica",
    "coracao_ferro": "minecraft:golden_apple",
}
CHIPS = ["chip_artes_marciais", "chip_supressao_dor", "chip_andarilho", "chip_ram"]


def prev(name, tier):
    return "sandevistan:" + (name if tier == 2 else "%s_%d" % (name, tier - 1))


def write(name, tier, pattern, key):
    rec = {"type": "minecraft:crafting_shaped", "category": "misc", "pattern": pattern,
           "key": {k: {"item": v} for k, v in key.items()},
           "result": {"id": "sandevistan:%s_%d" % (name, tier), "count": 1}}
    with open(os.path.join(OUT, "%s_%d.json" % (name, tier)), "w") as f:
        json.dump(rec, f, indent=2)


for name, thematic in IMPLANTS.items():
    write(name, 2, ["DSD", "TXT", "DBD"], {"D": "minecraft:diamond", "S": "sandevistan:servo_motor",
          "B": "sandevistan:bateria_energia", "T": thematic, "X": prev(name, 2)})
    write(name, 3, ["MCG", "TXT", "MNG"], {"M": "minecraft:diamond_block", "G": "minecraft:gold_block",
          "C": "sandevistan:cristal_neural", "N": "sandevistan:nucleo", "T": thematic, "X": prev(name, 3)})
for name in CHIPS:
    write(name, 2, ["DRD", "FXF", "DRD"], {"D": "minecraft:diamond", "R": "minecraft:redstone_block",
          "F": "sandevistan:fibra_sinaptica", "X": prev(name, 2)})
    write(name, 3, ["M M", "CXC", "R R"], {"M": "minecraft:diamond_block", "R": "minecraft:redstone_block",
          "C": "sandevistan:cristal_neural", "X": prev(name, 3)})
