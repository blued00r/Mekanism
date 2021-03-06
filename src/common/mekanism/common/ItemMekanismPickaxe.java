package mekanism.common;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class ItemMekanismPickaxe extends ItemMekanismTool
{
    private static Block blocksEffectiveAgainst[];

    public ItemMekanismPickaxe(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        super(par1, 2, par2EnumToolMaterial, blocksEffectiveAgainst);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block)
    {
        if (par1Block == Block.obsidian)
        {
            return toolMaterial.getHarvestLevel() == 3;
        }

        if (par1Block == Block.blockDiamond || par1Block == Block.oreDiamond)
        {
            return toolMaterial.getHarvestLevel() >= 2;
        }

        if (par1Block == Block.blockGold || par1Block == Block.oreGold)
        {
            return toolMaterial.getHarvestLevel() >= 2;
        }

        if (par1Block == Block.blockSteel || par1Block == Block.oreIron)
        {
            return toolMaterial.getHarvestLevel() >= 1;
        }

        if (par1Block == Block.blockLapis || par1Block == Block.oreLapis)
        {
            return toolMaterial.getHarvestLevel() >= 1;
        }

        if (par1Block == Block.oreRedstone || par1Block == Block.oreRedstoneGlowing)
        {
            return toolMaterial.getHarvestLevel() >= 2;
        }

        if (par1Block.blockMaterial == Material.rock)
        {
            return true;
        }

        return par1Block.blockMaterial == Material.iron;
    }

    @Override
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        if (par2Block != null && (par2Block.blockMaterial == Material.iron || par2Block.blockMaterial == Material.rock))
        {
            return efficiencyOnProperMaterial;
        }
        else
        {
            return super.getStrVsBlock(par1ItemStack, par2Block);
        }
    }

    static
    {
        blocksEffectiveAgainst = (new Block[]
                {
                    Block.cobblestone, Block.stoneSingleSlab, Block.stoneDoubleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold,
                    Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail,
                    Block.railDetector, Block.railPowered
                });
    }
}
