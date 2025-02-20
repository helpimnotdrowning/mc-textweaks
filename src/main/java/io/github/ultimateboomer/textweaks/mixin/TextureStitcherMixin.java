package io.github.ultimateboomer.textweaks.mixin;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureStitcher;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextureStitcher.class)
public class TextureStitcherMixin {
    /**
     * Reduce atlas size if mipmap level is set higher than necessary
     */
    @Redirect(method = "add", at = @At(value = "NEW",
            target = "net/minecraft/client/texture/TextureStitcher$Holder"))
    private TextureStitcher.Holder onAdd(Sprite.Info sprite, int mipLevel) {
        return new TextureStitcher.Holder(sprite, MathHelper.floorLog2(Math.min(sprite.getWidth(), sprite.getHeight())));
    }
}
