package io.github.stupidrepo.EntityNames.mixin;

import io.github.stupidrepo.EntityNames.EntityNames;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class NameOverriderMixin {
	@Shadow protected abstract ListTag newDoubleList(double... numbers);

	@Inject(method = "getName", at = @At("TAIL"), cancellable = true)
	public void onGetName(CallbackInfoReturnable<Component> cir) {
		Entity entity = (Entity) (Object) this;
		Component original = cir.getReturnValue();
		if(!(entity instanceof Player) && !entity.hasCustomName()) {
			MutableComponent ours = Component.literal(EntityNames.getEntityRandomName(entity));
			ours.setStyle(
					original.getStyle()
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, original))
			); // Show original name on hover :P

			cir.setReturnValue(ours);
		}
	}
}