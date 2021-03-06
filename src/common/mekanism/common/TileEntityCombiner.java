package mekanism.common;

import java.util.List;
import java.util.Vector;

import net.minecraft.src.*;

public class TileEntityCombiner extends TileEntityAdvancedElectricMachine
{
	public static List recipes = new Vector();
	
	public TileEntityCombiner()
	{
		super("Combiner.ogg", "Combiner", "/resources/mekanism/gui/GuiCombiner.png", 5, 1, 200, 1000, 200);
	}
	
	@Override
	public List getRecipes()
	{
		return recipes;
	}
	
	@Override
	public int getFuelTicks(ItemStack itemstack)
	{
		if(itemstack.getItem() instanceof ItemBlock && itemstack.itemID == Block.cobblestone.blockID)
		{
			return 200;
		}
		return 0;
	}
}
