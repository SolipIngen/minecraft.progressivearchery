package solipingen.progressivearchery.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import solipingen.progressivearchery.client.util.interfaces.mixin.render.PlayerEntityRenderStateInterface;


@Mixin(PlayerEntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateInterface {
    @Unique private ItemStack quiverStack;

    @Unique private int stuckWoodenArrowCount;
    @Unique private int stuckWoodenKidArrowCount;

    @Unique private int stuckFlintArrowCount;
    @Unique private int stuckFlintKidArrowCount;

    @Unique private int stuckCopperArrowCount;
    @Unique private int stuckCopperKidArrowCount;

    @Unique private int stuckGoldenArrowCount;
    @Unique private int stuckGoldenKidArrowCount;

    @Unique private int stuckIronArrowCount;
    @Unique private int stuckIronKidArrowCount;

    @Unique private int stuckDiamondArrowCount;
    @Unique private int stuckDiamondKidArrowCount;

    @Unique private int stuckSpectralArrowCount;
    @Unique private int stuckSpectralKidArrowCount;

    @Override
    public ItemStack getQuiverStack() {
        return this.quiverStack;
    }

    @Override
    public void setQuiverStack(ItemStack stack) {
        this.quiverStack = stack;
    }

    @Override
    public int getStuckWoodenArrowCount() {
        return this.stuckWoodenArrowCount;
    }

    @Override
    public void setStuckWoodenArrowCount(int count) {
        this.stuckWoodenArrowCount = count;
    }

    @Override
    public int getStuckWoodenKidArrowCount() {
        return this.stuckWoodenKidArrowCount;
    }

    @Override
    public void setStuckWoodenKidArrowCount(int count) {
        this.stuckWoodenKidArrowCount = count;
    }

    @Override
    public int getStuckFlintArrowCount() {
        return this.stuckFlintArrowCount;
    }

    @Override
    public void setStuckFlintArrowCount(int count) {
        this.stuckFlintArrowCount = count;
    }

    @Override
    public int getStuckFlintKidArrowCount() {
        return this.stuckFlintKidArrowCount;
    }

    @Override
    public void setStuckFlintKidArrowCount(int count) {
        this.stuckFlintKidArrowCount = count;
    }

    @Override
    public int getStuckCopperArrowCount() {
        return this.stuckCopperArrowCount;
    }

    @Override
    public void setStuckCopperArrowCount(int count) {
        this.stuckCopperArrowCount = count;
    }

    @Override
    public int getStuckCopperKidArrowCount() {
        return this.stuckCopperKidArrowCount;
    }

    @Override
    public void setStuckCopperKidArrowCount(int count) {
        this.stuckCopperKidArrowCount = count;
    }

    @Override
    public int getStuckGoldenArrowCount() {
        return this.stuckGoldenArrowCount;
    }

    @Override
    public void setStuckGoldenArrowCount(int count) {
        this.stuckGoldenArrowCount = count;
    }

    @Override
    public int getStuckGoldenKidArrowCount() {
        return this.stuckGoldenKidArrowCount;
    }

    @Override
    public void setStuckGoldenKidArrowCount(int count) {
        this.stuckGoldenKidArrowCount = count;
    }

    @Override
    public int getStuckIronArrowCount() {
        return this.stuckIronArrowCount;
    }

    @Override
    public void setStuckIronArrowCount(int count) {
        this.stuckIronArrowCount = count;
    }

    @Override
    public int getStuckIronKidArrowCount() {
        return this.stuckIronKidArrowCount;
    }

    @Override
    public void setStuckIronKidArrowCount(int count) {
        this.stuckIronKidArrowCount = count;
    }

    @Override
    public int getStuckDiamondArrowCount() {
        return this.stuckDiamondArrowCount;
    }

    @Override
    public void setStuckDiamondArrowCount(int count) {
        this.stuckDiamondArrowCount = count;
    }

    @Override
    public int getStuckDiamondKidArrowCount() {
        return this.stuckDiamondKidArrowCount;
    }

    @Override
    public void setStuckDiamondKidArrowCount(int count) {
        this.stuckDiamondKidArrowCount = count;
    }

    @Override
    public int getStuckSpectralArrowCount() {
        return this.stuckSpectralArrowCount;
    }

    @Override
    public void setStuckSpectralArrowCount(int count) {
        this.stuckSpectralArrowCount = count;
    }

    @Override
    public int getStuckSpectralKidArrowCount() {
        return this.stuckSpectralKidArrowCount;
    }

    @Override
    public void setStuckSpectralKidArrowCount(int count) {
        this.stuckSpectralKidArrowCount = count;
    }




}
