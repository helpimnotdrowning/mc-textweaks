package io.github.ultimateboomer.textweaks;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ultimateboomer.textweaks.config.TexTweaksConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class TexTweaks implements ClientModInitializer {
	public static final String MOD_ID = "textweaks";
	public static final String MOD_NAME = "TexTweaks";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static final Map<String, SpriteAtlasTexture.Data> dataMap = new LinkedHashMap<>();
	
	public static TexTweaksConfig config;

	private static KeyBinding keyInfo;

	public static NativeImage replaceAllImage = null;
	
	@Override
	public void onInitializeClient() {
		// Register config
		initConfig();

		// Register keybindings
		keyInfo = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.textweaks.info",
				InputUtil.Type.KEYSYM, -1, "category.textweaks"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyInfo.wasPressed()) {
				if (client.player != null) {
					displayInfo(client);
				}
			}
		});

		LOGGER.info("Mod initialization complete");
	}

	public static void initConfig() {
		if (config == null) {
			AutoConfig.register(TexTweaksConfig.class, GsonConfigSerializer::new);
			config = AutoConfig.getConfigHolder(TexTweaksConfig.class).getConfig();
			LOGGER.info("Mod config initialized");
		}
	}

	/**
	 * Show texture info
	 */
	public static void displayInfo(MinecraftClient client) {
		client.player.sendMessage(Text.literal(""), false);
		client.player.sendMessage(Text.literal("\u00a7l[Texture Info]\u00a7r"), false);
		client.player.sendMessage(Text.literal(
				String.format("Max Atlas Size: \u00a7a%s\u00a7r", RenderSystem.maxSupportedTextureSize())), false);

		dataMap.forEach((id, data) -> {
			client.player.sendMessage(Text.literal(
					String.format("Atlas: \u00a7e%s\u00a7r", id.toString())), false);
			client.player.sendMessage(Text.literal(
					String.format(" - Size: \u00a7a%s\u00a7rx\u00a7a%s\u00a7r", data.width, data.height)),
					false);
			client.player.sendMessage(Text.literal(
					String.format(" - # of textures: \u00a7a%s\u00a7r", data.spriteIds.size())), false);
			client.player.sendMessage(Text.literal(
					String.format(" - Mipmap Level: \u00a7a%s\u00a7r", data.maxLevel)), false);
		});
	}

	public static void displayNotice(MinecraftClient client) {
		if (!config.other.disableNotice
				&& (config.textureScaling.enableUpscale || config.textureScaling.enableDownscale)) {
			if (config.textureScaling.enableUpscale) {
				Text text = Text.translatable("textweaks.title")
						.append(Text.literal(" "))
						.append(Text.translatable("textweaks.notice.textureScaling.upscale",
								1 << config.textureScaling.resolution));
				client.player.sendMessage(text, false);
			}

			if (config.textureScaling.enableDownscale) {
				Text text = Text.translatable("textweaks.title")
						.append(Text.literal(" "))
						.append(Text.translatable("textweaks.notice.textureScaling.downscale",
								1 << config.textureScaling.resolution));
				client.player.sendMessage(text, false);
			}

			client.player.sendMessage(Text.translatable("textweaks.notice.disable"), false);
		}
	}
}
