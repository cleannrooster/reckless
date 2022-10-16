package com.cleannrooster.theascent.registry.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.dimension.DimensionType;

import java.awt.*;
import java.util.Random;

import static net.minecraft.entity.effect.StatusEffectCategory.BENEFICIAL;

public class Digging extends StatusEffect {
    public Digging() {
        super(
                BENEFICIAL, // whether beneficial or harmful for entities
                0x98D982); // color in RGB
    }
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the status effect every tick.
        return true;
    }
    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity && entity.isUsingRiptide() && !entity.isSneaking()) {
            World world = entity.world;
            destroyBlocks(entity.getBoundingBox(), world, entity);
            int i = MathHelper.floor(entity.getBoundingBox().minX+1);
            int j = MathHelper.floor(entity.getBoundingBox().minY+1);
            int k = MathHelper.floor(entity.getBoundingBox().minZ+1);
            int l = MathHelper.floor(entity.getBoundingBox().maxX+1);
            int m = MathHelper.floor(entity.getBoundingBox().maxY+1);
            int n = MathHelper.floor(entity.getBoundingBox().maxZ+1);
            Box stretched = new Box(i, j, k, l, m, n);
            boolean Collision = entity.world.isSpaceEmpty(entity, stretched) && !entity.world.containsFluid(stretched);
            if (entity.getPos().y < entity.getWorld().getDimension().getMinimumY()+16) {
                return;
            }

            entity.noClip = Collision;
            entity.horizontalCollision = false;
            entity.verticalCollision = false;
        }
    }
    public boolean destroyBlocks(Box box, World world, LivingEntity entity) {

        int i = MathHelper.floor(box.minX-(0.75));
        int j = MathHelper.floor(box.minY-(0.75));
        int k = MathHelper.floor(box.minZ-(0.75));
        int l = MathHelper.floor(box.maxX+(0.75));
        int m = MathHelper.floor(box.maxY+(0.75));
        int n = MathHelper.floor(box.maxZ+(0.75));
        Random rand = new Random();
        boolean bl = false;
        boolean bl2 = false;
        for (int o = i; o <= l; ++o) {
            for (int p = j; p <= m; ++p) {
                for (int q = k; q <= n; ++q) {
                    BlockPos blockPos = new BlockPos(o, p, q);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.isAir() || blockState.getMaterial() == Material.FIRE) continue;
                    if (blockState.getBlock().getHardness() > 50 || blockState.getBlock().getHardness() < 0) {
                        bl = true;
                        continue;
                    }
                    bl2 = world.breakBlock(blockPos, false, entity) || bl2;
                }
            }
        }
        if (bl2) {
            BlockPos o = new BlockPos(i + rand.nextInt(l - i + 1), j + rand.nextInt(m - j + 1), k + rand.nextInt(n - k + 1));
            world.syncWorldEvent(WorldEvents.ENDER_DRAGON_BREAKS_BLOCK, o, 0);
        }
        return bl;
    }
}