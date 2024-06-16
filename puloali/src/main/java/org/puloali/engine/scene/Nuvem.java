package org.puloali.engine.scene;

import org.joml.Vector3f;
import org.puloali.engine.graph.Model;
import org.puloali.engine.scene.lights.PointLight;

public class Nuvem {

    private Entity entidadeNuvem;
    private Model modeloNuvem;
    private AnimationData dadosAnimacao;

    public Nuvem(Scene cena, String id, String path) {

        modeloNuvem = ModelLoader.loadModel(
            "modelo-nuvem-" + id,
            path,
            cena.getTextureCache(),
            true
        );

        cena.addModel(modeloNuvem);

        entidadeNuvem = new Entity("entidade-nuvem-" + id, modeloNuvem.getId());

        entidadeNuvem.setPosition(-1, 0, -3.7f);
        entidadeNuvem.setScale(0.05f);
        // System.out.println("Animações " + modeloNuvem.getAnimationList().size());
        // dadosAnimacao = new AnimationData(modeloNuvem.getAnimationList().get(0));

        entidadeNuvem.updateModelMatrix();
        // System.out.println(modeloNuvem.getAnimationList().get(0).duration() + " " + modeloNuvem.getAnimationList().get(0).name());

        
        
        // cena.addEntity(entidadeNuvem);

    }

    public void setNextFrameAnimationData() {
        dadosAnimacao.nextFrame();
    }

    


}
