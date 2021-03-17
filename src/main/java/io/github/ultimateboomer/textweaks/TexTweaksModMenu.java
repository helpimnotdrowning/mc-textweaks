package io.github.ultimateboomer.textweaks;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.ultimateboomer.textweaks.config.TexTweaksConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public class TexTweaksModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(TexTweaksConfig.class, parent).get();
	}
}
