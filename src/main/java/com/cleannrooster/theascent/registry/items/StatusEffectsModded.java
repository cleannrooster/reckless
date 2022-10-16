package com.cleannrooster.theascent.registry.items;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffectsModded {

    public static final StatusEffect TUNNELLING = new Digging();
    public static void registerStatusEffects() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier("theascent", "tunnelling"), TUNNELLING);
    }
}
