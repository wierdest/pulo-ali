package org.puloali.engine.graph;

import org.lwjgl.opengl.GL;
import org.puloali.engine.Window;
import org.puloali.engine.scene.Scene;

import static org.lwjgl.opengl.GL13.*;

public class Render {

    private SceneRender sceneRender;
    private SkyBoxRender skyBoxRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_DEPTH_TEST);

        sceneRender = new SceneRender();
        // guiRender = new GuiRender(window);
        skyBoxRender = new SkyBoxRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
        skyBoxRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        skyBoxRender.render(scene);
        sceneRender.render(scene);
    }

    public void resize(int width, int height) {
        
    }
}
