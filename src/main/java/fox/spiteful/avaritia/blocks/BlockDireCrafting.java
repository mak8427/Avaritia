package fox.spiteful.avaritia.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.tile.TileEntityDireCrafting;

public class BlockDireCrafting extends BlockContainer {

    private static IIcon top, sides, bottom;

    public BlockDireCrafting() {
        super(Material.iron);
        setStepSound(Block.soundTypeGlass);
        setHardness(50.0F);
        setResistance(2000.0F);
        setBlockName("dire_crafting");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(Avaritia.tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        top = iconRegister.registerIcon("avaritia:dire_crafting_top");
        sides = iconRegister.registerIcon("avaritia:dire_crafting_side");
        bottom = iconRegister.registerIcon("avaritia:block_crystal_matrix");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if (side == 0) return bottom;
        if (side == 1) return top;
        return sides;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        if (world.isRemote) {
            return true;
        } else {
            player.openGui(Avaritia.instance, 1, world, x, y, z);
            return true;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDireCrafting();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int wut) {
        TileEntityDireCrafting craft = (TileEntityDireCrafting) world.getTileEntity(x, y, z);

        if (craft != null) {
            for (int i = 1; i < 82; i++) {
                ItemStack itemstack = craft.getStackInSlot(i);

                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(
                                world,
                                (float) x + f,
                                (float) y + f1,
                                (float) z + f2,
                                new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem()
                                    .setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (float) world.rand.nextGaussian() * f3;
                        entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
                        world.spawnEntityInWorld(entityitem);
                    }
                }

                world.func_147453_f(x, y, z, block); // updateNeighborsAboutBlockChange
            }
        }

        super.breakBlock(world, x, y, z, block, wut);
    }
}
