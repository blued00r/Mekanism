package mekanism.common;

import net.minecraft.src.*;

public class ItemStopwatch extends ItemMekanism {
	
	public ItemStopwatch(int i)
	{
		super(i);
		setMaxStackSize(1);
		setMaxDamage(5000);
		setCreativeTab(Mekanism.tabMekanism);
	}
	
	@Override
	public boolean hasEffect(ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if(itemstack.getItemDamage() == 0)
		{
			entityplayer.openGui(Mekanism.instance, 0, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
		}
		return itemstack;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
		if(itemstack.getItemDamage() > 0)
		{
			itemstack.damageItem(-1, (EntityLiving)entity);
		}
	}
}
