package solipingen.progressivearchery.mixin.client;

import net.minecraft.client.gui.screen.TitleScreen;
import solipingen.progressivearchery.ProgressiveArchery;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class WelcomeMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		ProgressiveArchery.LOGGER.info("Welcome to Progressive Archery! May all your your aims be true -- in-game, and in life.");
	}
}
