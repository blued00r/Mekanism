package mekanism.api;

import net.minecraft.src.*;

/**
 * Implement this in your item class if it can store or transfer energy.
 * @author AidanBrady
 *
 */
public interface IEnergizedItem
{
	/**
	 * Gets the amount of energy the item has from NBT storage.
	 * @param itemstack
	 * @return amount of energy
	 */
	public int getEnergy(ItemStack itemstack);
	
	/**
	 * Sets the energy the item has with NBT.
	 * @param itemstack
	 * @param energy
	 */
	public void setEnergy(ItemStack itemstack, int energy);
	
	/**
	 * Gets the maximum amount of energy this item can hold.
	 * @return maximum energy
	 */
	public int getMaxEnergy();
	
	/**
	 * Gets the rate of transfer this item can handle.
	 * @return
	 */
	public int getRate();
	
	/**
	 * Charges the item with the defined amount of energy.
	 * @param itemstack
	 * @param amount
	 * @return leftover energy
	 */
	public int charge(ItemStack itemstack, int amount);
	
	/**
	 * Removes the defined amount of energy from the item.
	 * @param itemstack
	 * @param amount
	 * @return energy discharged
	 */
	public int discharge(ItemStack itemstack, int amount);
	
	/**
	 * Gets the divider that gets that returns the max damage as 100.
	 * @return divider
	 */
	public int getDivider();
	
	/**
	 * Whether or not this energized item be charged.
	 * @return if the item be charged
	 */
	public boolean canBeCharged();
	
	/**
	 * Whether or not this energized item can charge an energy receiver.
	 * @return if the item can charge
	 */
	public boolean canBeDischarged();
}
