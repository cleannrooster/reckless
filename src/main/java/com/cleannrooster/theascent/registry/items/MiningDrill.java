package com.cleannrooster.theascent.registry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.Random;

public class MiningDrill
    extends Item
    implements
        Vanishable {

    public final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public MiningDrill(Settings maxDamage){
        super(maxDamage);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 7, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", 1, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();


    }
    public boolean isSuitableFor(BlockState state){
        return true;
    }
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return 10.0f;
    }
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        /*if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }*/
        return true;
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        destroyBlocks(user.getBoundingBox(), world, user);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    public boolean destroyBlocks(Box box, World world, LivingEntity entity) {
        float tridentEntity = entity.getYaw();
        float f = entity.getPitch();
        float g = -MathHelper.sin(tridentEntity * ((float)Math.PI / 180)) * MathHelper.cos(f * ((float)Math.PI / 180));
        float h = -MathHelper.sin(f * ((float)Math.PI / 180));
        float r = MathHelper.cos(tridentEntity * ((float)Math.PI / 180)) * MathHelper.cos(f * ((float)Math.PI / 180));
        int i = MathHelper.roundDownToMultiple(box.minX+(0.75*g)-0.5, 1);
        int j = MathHelper.roundDownToMultiple(0.5+box.minY+(0.75*h),1);
        int k = MathHelper.roundDownToMultiple(box.minZ+(0.75*r)-0.5,1);
        int l = MathHelper.roundUpToMultiple((int) (box.maxX+(0.75*g)+0.5),1);
        int m = MathHelper.roundUpToMultiple((int) (box.maxY+(0.75*h)),1);
        int n = MathHelper.roundUpToMultiple((int) (box.maxZ+(0.75*r)-0.5),1);
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
            //world.syncWorldEvent(WorldEvents.ENDER_DRAGON_BREAKS_BLOCK, o, 0);
        }
        return bl;
    }
}

