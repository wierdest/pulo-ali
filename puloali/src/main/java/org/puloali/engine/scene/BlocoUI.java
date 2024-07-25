package org.puloali.engine.scene;

import org.joml.Vector3f;
import org.puloali.engine.graph.Model;
import org.puloali.engine.scene.lights.PointLight;

public class BlocoUI {

    private Entity entidade;
    private Model modelo;
    private AnimationData dadosAnimacao;

    public BlocoUI(Scene cena, String id, String path) {

        modelo = ModelLoader.loadModel(
            "modelo-bloco-ui-" + id,
            path,
            cena.getTextureCache(),
            true
        );

        cena.addModel(modelo);

        entidade = new Entity("entidade-bloco-ui" + id, modelo.getId());

        entidade.setPosition(-1f, 0.2f, -2.8f);
        entidade.setScale(0.4f);
        entidade.setRotation(0f, 1f, 0f, (float)-(Math.PI) / 3);


        entidade.updateModelMatrix();

        
        cena.addEntity(entidade);

    }

    public void setNextFrameAnimationData() {
        dadosAnimacao.nextFrame();
    }

    public float getPosicaoY() {
        return entidade.getPosition().y;
    }
    public float getPosicaoX() {
        return entidade.getPosition().x;
    }
    public float getPosicaoZ() {
        return entidade.getPosition().z;
    }


    


}
