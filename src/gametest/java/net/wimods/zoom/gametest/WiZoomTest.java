/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wimods.zoom.gametest;

import static net.wimods.zoom.gametest.WiModsTestHelper.*;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.TestInput;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestClientWorldContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.world.TestWorldBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

public final class WiZoomTest implements FabricClientGameTest
{
	public static final Logger LOGGER = LoggerFactory.getLogger("WI Zoom Test");
	
	@Override
	public void runTest(ClientGameTestContext context)
	{
		LOGGER.info("Starting WI Zoom Client GameTest");
		hideSplashTexts(context);
		waitForTitleScreenFade(context);
		
		LOGGER.info("Reached title screen");
		context.takeScreenshot("title_screen");
		
		LOGGER.info("Creating test world");
		TestWorldBuilder worldBuilder = context.worldBuilder();
		worldBuilder.adjustSettings(creator -> {
			String mcVersion = SharedConstants.getCurrentVersion().name();
			creator.setName("E2E Test " + mcVersion);
			creator.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
			creator.getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK)
				.set(false, null);
			applyFlatPresetWithSmoothStone(creator);
		});
		
		try(TestSingleplayerContext spContext = worldBuilder.create())
		{
			testInWorld(context, spContext);
			LOGGER.info("Exiting test world");
		}
		
		LOGGER.info("Test complete");
	}
	
	private void testInWorld(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		TestInput input = context.getInput();
		TestClientWorldContext world = spContext.getClientWorld();
		TestServerContext server = spContext.getServer();
		
		runCommand(server, "time set noon");
		runCommand(server, "tp 0 -57 0");
		runCommand(server, "fill ~ ~-3 ~ ~ ~-1 ~ smooth_stone");
		runCommand(server, "fill ~-12 ~-3 ~10 ~12 ~9 ~10 smooth_stone");
		
		LOGGER.info("Loading chunks");
		context.waitTicks(2);
		world.waitForChunksRender();
		
		assertScreenshotEquals(context, "in_game",
			"https://i.imgur.com/i2Nr9is.png");
		
		LOGGER.info("Recording debug menu");
		input.pressKey(GLFW.GLFW_KEY_F3);
		context.takeScreenshot("debug_menu");
		input.pressKey(GLFW.GLFW_KEY_F3);
		
		LOGGER.info("Checking for broken mixins");
		MixinEnvironment.getCurrentEnvironment().audit();
		
		LOGGER.info("Opening inventory");
		input.pressKey(GLFW.GLFW_KEY_E);
		String invTemplate = "https://i.imgur.com/GP74ZNS.png";
		assertScreenshotEquals(context, "inventory", invTemplate);
		// Try to zoom in inventory to confirm it does nothing
		input.holdKey(GLFW.GLFW_KEY_V);
		context.waitTick();
		assertScreenshotEquals(context, "trying_to_zoom_in_inventory",
			invTemplate);
		input.releaseKey(GLFW.GLFW_KEY_V);
		context.waitTick();
		input.pressKey(GLFW.GLFW_KEY_ESCAPE);
		
		testZoomInWorld(context);
		
		LOGGER.info("Opening game menu");
		input.pressKey(GLFW.GLFW_KEY_ESCAPE);
		context.takeScreenshot("game_menu");
		
		LOGGER.info("Clicking Options button");
		for(int i = 0; i < 6; i++)
			input.pressKey(GLFW.GLFW_KEY_TAB);
		input.pressKey(GLFW.GLFW_KEY_ENTER);
		context.takeScreenshot("options_screen");
		
		LOGGER.info("Clicking Controls button");
		for(int i = 0; i < 6; i++)
			input.pressKey(GLFW.GLFW_KEY_TAB);
		input.pressKey(GLFW.GLFW_KEY_ENTER);
		context.takeScreenshot("controls_screen");
		
		LOGGER.info("Clicking Key Binds button");
		input.pressKey(GLFW.GLFW_KEY_TAB);
		input.pressKey(GLFW.GLFW_KEY_ENTER);
		// Select the last keybind in the list
		for(int i = 0; i < 2; i++)
			pressKeyWithModifiers(context, GLFW.GLFW_KEY_TAB,
				GLFW.GLFW_MOD_SHIFT);
		assertScreenshotEquals(context, "zoom_keybind_default",
			"https://i.imgur.com/CRlFBfx.png");
		
		LOGGER.info("Changing zoom keybind to B");
		input.pressKey(GLFW.GLFW_KEY_ENTER);
		input.pressKey(GLFW.GLFW_KEY_B);
		assertScreenshotEquals(context, "zoom_keybind_changed",
			"https://i.imgur.com/fPfEkm6.png");
		
		LOGGER.info("Closing screens");
		for(int i = 0; i < 4; i++)
			input.pressKey(GLFW.GLFW_KEY_ESCAPE);
		
		testZoomWithChangedKeybind(context);
	}
	
	private void testZoomInWorld(ClientGameTestContext context)
	{
		TestInput input = context.getInput();
		
		// Press V to enable zoom
		input.holdKey(GLFW.GLFW_KEY_V);
		context.waitTick();
		assertScreenshotEquals(context, "3x_zoom",
			"https://i.imgur.com/Xy0VdFE.png");
		
		scrollUpToMaxZoom(context);
		assertScreenshotEquals(context, "50x_zoom",
			"https://i.imgur.com/DF5Tr8F.png");
		assertSelectedSlotIsZero(context);
		
		// Release V to disable zoom
		input.releaseKey(GLFW.GLFW_KEY_V);
		context.waitTick();
	}
	
	private void testZoomWithChangedKeybind(ClientGameTestContext context)
	{
		TestInput input = context.getInput();
		
		input.holdKey(GLFW.GLFW_KEY_B);
		context.waitTick();
		assertScreenshotEquals(context, "custom_keybind_3x_zoom",
			"https://i.imgur.com/Xy0VdFE.png");
		
		scrollUpToMaxZoom(context);
		assertScreenshotEquals(context, "custom_keybind_50x_zoom",
			"https://i.imgur.com/DF5Tr8F.png");
		assertSelectedSlotIsZero(context);
		
		input.releaseKey(GLFW.GLFW_KEY_B);
		context.waitTick();
	}
	
	private void scrollUpToMaxZoom(ClientGameTestContext context)
	{
		TestInput input = context.getInput();
		int scrollsNeededFor50x = Mth.ceil(Math.log(50 / 3) / Math.log(1.1));
		for(int i = 0; i < scrollsNeededFor50x; i++)
			input.scroll(0, 1);
		context.waitTick();
	}
	
	private void assertSelectedSlotIsZero(ClientGameTestContext context)
	{
		if(context.computeOnClient(
			mc -> mc.player.getInventory().getSelectedSlot()) != 0)
			throw new RuntimeException(
				"Scrolling up while zooming changed the selected slot");
	}
	
	// because the grass texture is randomized and smooth stone isn't
	private void applyFlatPresetWithSmoothStone(WorldCreationUiState creator)
	{
		FlatLevelGeneratorSettings config = ((FlatLevelSource)creator
			.getSettings().selectedDimensions().overworld()).settings();
		
		List<FlatLayerInfo> layers =
			List.of(new FlatLayerInfo(1, Blocks.BEDROCK),
				new FlatLayerInfo(2, Blocks.DIRT),
				new FlatLayerInfo(1, Blocks.SMOOTH_STONE));
		
		creator.updateDimensions(
			(drm, dorHolder) -> dorHolder.replaceOverworldGenerator(drm,
				new FlatLevelSource(config.withBiomeAndLayers(layers,
					config.structureOverrides(), config.getBiome()))));
	}
}
