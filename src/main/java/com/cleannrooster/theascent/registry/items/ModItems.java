package com.cleannrooster.theascent.registry.items;

import com.cleannrooster.theascent.Ascent;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems {
    public static class giga extends BreakerDrill {
        public giga(Settings maxDamage) {
            super(maxDamage);
        }
    }
    public static class miner extends MiningDrill {
        public miner(Item.Settings settings) {
            super(settings);
        }
    }
    public static class fighter extends FighterDrill {
        public fighter(Item.Settings settings) {
            super(settings);
        }
    }
    public static class powered extends PoweredDrills {
        public powered(Item.Settings settings) {
            super(settings);
        }
    }
    public static class left extends PoweredDrillsLeft {
        public left(Item.Settings settings) {
            super(settings);
        }
    }

    public static giga gigadrill = new giga(new Item.Settings().maxDamage(3000).group(ItemGroup.TOOLS));
    public static miner miningdrill = new miner(new Item.Settings().maxDamage(3000).group(ItemGroup.TOOLS));
    public static fighter fighterdrill = new fighter(new Item.Settings().maxDamage(3000).group(ItemGroup.TOOLS));
    public static powered powereddrills = new powered(new Item.Settings().maxDamage(200).group(ItemGroup.TOOLS));
    public static left left = new left(new Item.Settings().maxDamage(200).group(ItemGroup.TOOLS));




    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(Ascent.MOD_ID, "breaker_drill"), gigadrill);
        Registry.register(Registry.ITEM, new Identifier(Ascent.MOD_ID, "mining_drill"), miningdrill);
        Registry.register(Registry.ITEM, new Identifier(Ascent.MOD_ID, "fighter_drill"), fighterdrill);
        Registry.register(Registry.ITEM, new Identifier(Ascent.MOD_ID, "right_drill"), powereddrills);
        Registry.register(Registry.ITEM, new Identifier(Ascent.MOD_ID, "left_drill"), left);


    }
}