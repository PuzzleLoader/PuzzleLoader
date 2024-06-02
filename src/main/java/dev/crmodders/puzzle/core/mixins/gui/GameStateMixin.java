
package dev.crmodders.puzzle.core.mixins.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.flux.gui.interfaces.GameStateInterface;
import dev.crmodders.flux.ui.Component;
import dev.crmodders.flux.ui.UIRenderer;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameState.class)
public abstract class GameStateMixin implements GameStateInterface {

	@Shadow
	protected Viewport uiViewport;
	@Shadow
	protected OrthographicCamera uiCamera;
	@Shadow
	Vector2 mouse;

	@Unique
	private final List<Component> components = new ArrayList<>();

	@Override
	public Viewport getViewport() {
		return uiViewport;
	}

	@Override
	public OrthographicCamera getCamera() {
		return uiCamera;
	}

	@Override
	public List<Component> getComponents() {
		return components;
	}

	@Inject(method = "drawUIElements", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/graphics/g2d/SpriteBatch;setProjectionMatrix(Lcom/badlogic/gdx/math/Matrix4;)V"))
	private void drawCustomUIElements(CallbackInfo ci) {
		UIRenderer.uiRenderer.render(components, uiCamera, uiViewport, mouse);
	}

	@Inject(method = "switchToGameState", at = @At(value = "HEAD"))
	private static void cleanUpCustomUIElements(CallbackInfo ci) {
		if(GameState.currentGameState != null) {
			for(Component component : ((GameStateInterface) GameState.currentGameState).getComponents()) {
				component.deactivate();
			}
		}
	}



}