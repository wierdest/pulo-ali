package org.puloali.engine.graph;

import org.joml.Matrix4f;
import org.puloali.engine.scene.Entity;

import java.util.*;

public class Model {

    private final String id;
    private List<Animation> animationList;
    private List<Entity> entitiesList;
    private List<Material> materialList;

    public Model(String id, List<Material> materialList, List<Animation> animationList) {
        entitiesList = new ArrayList<>();
        this.id = id;
        this.materialList = materialList;
        this.animationList = animationList;
    }

    public void cleanup() {
        materialList.forEach(Material::cleanup);
    }

    public List<Animation> getAnimationList() {
        return animationList;
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public Entity getEntityById(String id, Model modelo) {
		for (Entity e : modelo.getEntitiesList()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public record AnimatedFrame(Matrix4f[] boneMatrices) {
    }

    public record Animation(String name, double duration, List<AnimatedFrame> frames) {
    }
}