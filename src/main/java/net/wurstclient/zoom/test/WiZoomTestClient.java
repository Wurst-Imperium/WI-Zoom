/*
 * Copyright (c) 2019-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.zoom.test;

import static net.wurstclient.zoom.test.WiModsTestHelper.*;

import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.util.math.MathHelper;
import net.wurstclient.zoom.WiZoom;

public final class WiZoomTestClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		if(System.getProperty("wi_zoom.e2eTest") == null)
			return;
		
		Thread.ofVirtual().name("WI Zoom End-to-End Test")
			.uncaughtExceptionHandler((t, e) -> {
				e.printStackTrace();
				System.exit(1);
			}).start(this::runTests);
	}
	
	private void runTests()
	{
		System.out.println("Starting WI Zoom End-to-End Test");
		waitForResourceLoading();
		
		// Disable anisotropic filtering
		submitAndWait(mc -> mc.options.method_76247().setValue(0));
		
		if(submitAndGet(mc -> mc.options.onboardAccessibility))
		{
			System.out.println("Onboarding is enabled. Waiting for it");
			waitForScreen(AccessibilityOnboardingScreen.class);
			System.out.println("Reached onboarding screen");
			clickButton("gui.continue");
		}
		
		waitForScreen(TitleScreen.class);
		waitForTitleScreenFade();
		System.out.println("Reached title screen");
		takeScreenshot("title_screen", Duration.ZERO);
		
		System.out.println("Clicking singleplayer button");
		clickButton("menu.singleplayer");
		
		if(submitAndGet(mc -> !mc.getLevelStorage().getLevelList().isEmpty()))
		{
			System.out.println("World list is not empty. Waiting for it");
			waitForScreen(SelectWorldScreen.class);
			System.out.println("Reached select world screen");
			takeScreenshot("select_world_screen");
			clickButton("selectWorld.create");
		}
		
		waitForScreen(CreateWorldScreen.class);
		System.out.println("Reached create world screen");
		
		// Set MC version as world name
		setTextFieldText(0,
			"E2E Test " + SharedConstants.getGameVersion().name());
		// Select creative mode
		clickButton("selectWorld.gameMode");
		clickButton("selectWorld.gameMode");
		takeScreenshot("create_world_screen");
		
		System.out.println("Creating test world");
		clickButton("selectWorld.create");
		
		waitForWorldLoad();
		dismissTutorialToasts();
		waitForWorldTicks(200);
		runChatCommand("seed");
		System.out.println("Reached singleplayer world");
		takeScreenshot("in_game", Duration.ZERO);
		clearChat();
		
		System.out.println("Opening debug menu");
		toggleDebugHud();
		takeScreenshot("debug_menu");
		
		System.out.println("Closing debug menu");
		toggleDebugHud();
		
		System.out.println("Checking for broken mixins");
		MixinEnvironment.getCurrentEnvironment().audit();
		
		System.out.println("Opening inventory");
		openInventory();
		// Try to zoom in inventory to confirm it does nothing
		setKeyPressState(GLFW.GLFW_KEY_V, true);
		waitForWorldTicks(1);
		takeScreenshot("inventory");
		setKeyPressState(GLFW.GLFW_KEY_V, false);
		waitForWorldTicks(1);
		
		System.out.println("Closing inventory");
		closeScreen();
		
		// Build a test platform and clear out the space above it
		runChatCommand("fill ~-1 ~-1 ~-1 ~1 ~-1 ~15 stone");
		runChatCommand("fill ~-1 ~ ~-1 ~1 ~30 ~15 air");
		
		// Clear inventory and chat before running tests
		runChatCommand("clear");
		clearChat();
		
		testZoomInWorld();
		
		System.out.println("Opening game menu");
		openGameMenu();
		takeScreenshot("game_menu");
		
		System.out.println("Clicking Options button");
		clickButton("menu.options");
		waitForScreen(OptionsScreen.class);
		System.out.println("Reached options screen");
		takeScreenshot("options_screen", Duration.ZERO);
		
		System.out.println("Clicking Controls button");
		clickButton("options.controls");
		waitForScreen(ControlsOptionsScreen.class);
		System.out.println("Reached controls screen");
		takeScreenshot("controls_screen", Duration.ZERO);
		
		System.out.println("Clicking Key Binds button");
		setKeyPressState(GLFW.GLFW_KEY_TAB, true);
		setKeyPressState(GLFW.GLFW_KEY_TAB, false);
		setKeyPressState(GLFW.GLFW_KEY_TAB, true);
		setKeyPressState(GLFW.GLFW_KEY_TAB, false);
		setKeyPressState(GLFW.GLFW_KEY_ENTER, true);
		setKeyPressState(GLFW.GLFW_KEY_ENTER, false);
		waitForScreen(KeybindsScreen.class);
		System.out.println("Reached keybinds screen");
		// Select the last keybind in the list
		for(int i = 0; i < 2; i++)
		{
			setKeyPressState(GLFW.GLFW_KEY_TAB, true, 1);// shift+tab
			setKeyPressState(GLFW.GLFW_KEY_TAB, false);
		}
		takeScreenshot("key_binds_screen");
		
		System.out.println("Changing zoom keybind to B");
		setKeyPressState(GLFW.GLFW_KEY_ENTER, true);
		setKeyPressState(GLFW.GLFW_KEY_ENTER, false);
		setKeyPressState(GLFW.GLFW_KEY_B, true);
		setKeyPressState(GLFW.GLFW_KEY_B, false);
		takeScreenshot("zoom_keybind_changed");
		
		System.out.println("Closing screens");
		clickButton("gui.done");
		clickButton("gui.done");
		clickButton("gui.done");
		clickButton("menu.returnToGame");
		
		testZoomWithChangedKeybind();
		
		System.out.println("Returning to title screen");
		openGameMenu();
		clickButton("menu.returnToMenu");
		waitForScreen(TitleScreen.class);
		
		System.out.println("Changing zoom keybind back to V");
		submitAndWait(mc -> WiZoom.INSTANCE.getZoomKey()
			.setBoundKey(WiZoom.INSTANCE.getZoomKey().getDefaultKey()));
		submitAndWait(mc -> mc.options.write());
		
		System.out.println("Stopping the game");
		clickButton("menu.quit");
	}
	
	private void testZoomInWorld()
	{
		// Spawn a chicken in front of the player
		runChatCommand(
			"summon minecraft:chicken ~ ~0.85 ~15 {NoAI:1,NoGravity:1,Rotation:[180f,0f]}");
		waitForWorldTicks(5);
		takeScreenshot("chicken_no_zoom");
		
		// Press V to enable zoom
		setKeyPressState(GLFW.GLFW_KEY_V, true);
		waitForWorldTicks(1);
		takeScreenshot("chicken_3x_zoom");
		
		scrollUpToMaxZoom();
		takeScreenshot("chicken_50x_zoom");
		
		assertSelectedSlotIsZero();
		
		// Release V to disable zoom
		setKeyPressState(GLFW.GLFW_KEY_V, false);
		waitForWorldTicks(1);
	}
	
	private void testZoomWithChangedKeybind()
	{
		setKeyPressState(GLFW.GLFW_KEY_B, true);
		waitForWorldTicks(1);
		
		scrollUpToMaxZoom();
		takeScreenshot("custom_keybind_50x_zoom");
		assertSelectedSlotIsZero();
		
		setKeyPressState(GLFW.GLFW_KEY_B, false);
		waitForWorldTicks(1);
	}
	
	private void scrollUpToMaxZoom()
	{
		int scrollsNeededFor50x =
			MathHelper.ceil(Math.log(50 / 3) / Math.log(1.1));
		for(int i = 0; i < scrollsNeededFor50x; i++)
			scrollMouse(0, 1);
		waitForWorldTicks(1);
	}
	
	private void assertSelectedSlotIsZero()
	{
		if(submitAndGet(mc -> mc.player.getInventory().getSelectedSlot() != 0))
			throw new RuntimeException(
				"Scrolling up while zooming changed the selected slot");
	}
}
