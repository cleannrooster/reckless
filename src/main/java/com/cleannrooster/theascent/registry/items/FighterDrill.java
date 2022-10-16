package com.cleannrooster.theascent.registry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static java.lang.Math.min;
import static net.minecraft.util.math.MathHelper.absMax;
import static net.minecraft.util.math.MathHelper.sqrt;


public class FighterDrill
        extends Item
        implements Vanishable{
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public FighterDrill(Item.Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 11.0, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", (double)-2.9f, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }



    /*    public static void applyPlayerEffects(ItemStack stack, PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(HASTE, 20, 5, false, false, true));
            if (player.getItemsHand() == DrillItem) {
                boolean drillinhand = true;
                if (drillinhand) {
                    Vec3d v = player.getVelocity();
                    float x = (float)v.x;
                    float y = (float)v.y;
                    float z = (float)v.z;
                    float miningSpeedMultiplier = sqrt((x * x) + (y * y) + (z * z)) ;
                    int miningSpeedInteger = (int)miningSpeedMultiplier;

                }
            }
        }*/
    public boolean isSuitableFor(BlockState state){
        return true;
    }
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return 10.0f;
    }
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        if (i < 10) {
            return;
        }
        /*if (!world.isClient) {
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
            TridentEntity tridentEntity = new TridentEntity(world, (LivingEntity)playerEntity, stack);
            tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 2.5f + (float)j * 0.5f, 1.0f);
            if (playerEntity.getAbilities().creativeMode) {
                tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            world.spawnEntity(tridentEntity);
            world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!playerEntity.getAbilities().creativeMode) {
                playerEntity.getInventory().removeOne(stack);
            }
        }*/
        //playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        float tridentEntity = playerEntity.getYaw();
        float f = playerEntity.getPitch();
        float g = -MathHelper.sin(tridentEntity * ((float)Math.PI / 180)) * MathHelper.cos(f * ((float)Math.PI / 180));
        float h = -MathHelper.sin(f * ((float)Math.PI / 180));
        float k = MathHelper.cos(tridentEntity * ((float)Math.PI / 180)) * MathHelper.cos(f * ((float)Math.PI / 180));
        float l = sqrt(g * g + h * h + k * k);
        float m = 3.0f * ((1.0f + (float)2) / 4.0f);
        user.addVelocity(g *= m / l, h *= m / l, k *= m / l);
        user.setRiptideTicks(20);


        if (user.isOnGround()) {
            float n = 1.1999999f;
            user.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
        }
        SoundEvent n = 2 >= 3 ? SoundEvents.ITEM_TRIDENT_RIPTIDE_1 : (6 == 2 ? SoundEvents.ITEM_TRIDENT_RIPTIDE_2 : SoundEvents.ITEM_TRIDENT_RIPTIDE_1);
        world.playSoundFromEntity(null, user, n, SoundCategory.PLAYERS, 1.0f, 1.0f);

    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        /*if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isTouchingWaterOrRain()) {
            return TypedActionResult.fail(itemStack);
        }*/
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    //Level world = player.level;
       /* if(player.isShiftKeyDown()||world.isClientSide||!(player instanceof ServerPlayer))
            return false;
        HitResult mop = getPlayerPOVHitResult(world, player, Fluid.NONE);
        ItemStack head = getHead(stack);
        if(mop==null||head.isEmpty()||!canToolBeUsed(stack, player))
            return false;
        ImmutableList<BlockPos> additional = ((IDrillHead)head.getItem()).getExtraBlocksDug(head, world, player, mop);*/
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}


/*    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }*/

/*    @Override
    public int getEnchantability() {
        return 1;
    }*/

