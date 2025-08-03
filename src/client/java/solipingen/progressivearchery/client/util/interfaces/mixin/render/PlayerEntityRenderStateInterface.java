package solipingen.progressivearchery.client.util.interfaces.mixin.render;


import net.minecraft.item.ItemStack;

public interface PlayerEntityRenderStateInterface {

    public int getStuckWoodenArrowCount();
    public void setStuckWoodenArrowCount(int count);

    public int getStuckWoodenKidArrowCount();
    public void setStuckWoodenKidArrowCount(int count);

    public int getStuckFlintArrowCount();
    public void setStuckFlintArrowCount(int count);

    public int getStuckFlintKidArrowCount();
    public void setStuckFlintKidArrowCount(int count);

    public int getStuckCopperArrowCount();
    public void setStuckCopperArrowCount(int count);

    public int getStuckCopperKidArrowCount();
    public void setStuckCopperKidArrowCount(int count);

    public int getStuckGoldenArrowCount();
    public void setStuckGoldenArrowCount(int count);

    public int getStuckGoldenKidArrowCount();
    public void setStuckGoldenKidArrowCount(int count);

    public int getStuckIronArrowCount();
    public void setStuckIronArrowCount(int count);

    public int getStuckIronKidArrowCount();
    public void setStuckIronKidArrowCount(int count);

    public int getStuckDiamondArrowCount();
    public void setStuckDiamondArrowCount(int count);

    public int getStuckDiamondKidArrowCount();
    public void setStuckDiamondKidArrowCount(int count);

    public int getStuckSpectralArrowCount();
    public void setStuckSpectralArrowCount(int count);

    public int getStuckSpectralKidArrowCount();
    public void setStuckSpectralKidArrowCount(int count);

    public ItemStack getQuiverStack();
    public void setQuiverStack(ItemStack stack);

}
