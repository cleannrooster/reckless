package com.cleannrooster.theascent.registry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PoweredDrills extends MiningDrill{

    public PoweredDrills(Settings maxDamage) {
        super(maxDamage);
    }
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        destroyBlocks(user.getBoundingBox(), world, user);
        if(user.getStackInHand(hand).getDamage() >= 20){
        user.getStackInHand(hand).damage(-20, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            user.getItemCooldownManager().set(this, 6);

        }
        else{
            if(!user.isCreative()) {
                user.getItemCooldownManager().set(this, 160);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(stack.getMaxDamage() - stack.getDamage() > 1 && entity instanceof PlayerEntity){
           stack.damage(1, (PlayerEntity)entity, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
