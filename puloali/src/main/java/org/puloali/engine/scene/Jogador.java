package org.puloali.engine.scene;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.puloali.engine.graph.Model;
import org.puloali.engine.graph.Model.Animation;

public class Jogador {

    private Entity entidadeJogador;
    private Model modeloJogador;
    private Model modeloCubo;
    private String idCuboAtual = "B1";
    private Entity cuboAtual;
    private Vector3f posInicial;
    private Quaternionf rotInicial;
    private Scene cena;
    private AnimationData dadosAnimacao;
    public boolean isJumping = false;


    public Jogador(Scene cena) {

        modeloJogador = ModelLoader.loadModel(
        "modelo-jogador", 
        "puloali/src/main/resources/models/puloali-character/puloali-character.gltf", 
        cena.getTextureCache(), true);

        // Model modeloJogador = ModelLoader.loadModel(
        // "modelo-jogador", 
        // "puloali/src/main/resources/models/jogador/player.obj", 
        // cena.getTextureCache(), false);


        cena.addModel(modeloJogador);

        modeloCubo = cena.getModelMap().get("modelo-cubo");
        if(modeloCubo == null) {
            System.out.println("Cubo modelo é NULO!!");
            return;
        }

        cuboAtual = modeloCubo.getEntityById("entidade-cubo-"+ idCuboAtual, modeloCubo);

        entidadeJogador = new Entity("entidade-jogador", modeloJogador.getId());

        posInicial = cuboAtual.getPosition();
        entidadeJogador.setPosition(posInicial.x, posInicial.y + 0.1f, posInicial.z);

        rotInicial = cuboAtual.getRotation();

        entidadeJogador.setRotation(0, 1, 0, (float)(Math.PI / 4));
        entidadeJogador.setScale(0.04f);
        
        dadosAnimacao = new AnimationData(modeloJogador.getAnimationList().get(0));
        entidadeJogador.setAnimationData(dadosAnimacao);

        entidadeJogador.updateModelMatrix();

        cena.addEntity(entidadeJogador);
        this.cena = cena;
    }

    public void removerJogadorDeCena() {
        cena.removeEntity(entidadeJogador);
    }
    public boolean descerDireita() {
        int numeroColuna = Integer.parseInt(idCuboAtual.charAt(1) + "");
        char letraFileira = idCuboAtual.charAt(0);
        entidadeJogador.setRotation(0, 1, 0, (float)(Math.PI / 4));

        if(letraFileira == 'B' || letraFileira == 'D') {
            numeroColuna++;
        } else {
            // nas fileiras A, C e E, o numero da coluna permanece o mesmo
            if((letraFileira == 'A' || letraFileira == 'C') && numeroColuna == 4) {
                // movimento impossível
                System.out.println("MOVIMENTO P/ DIREITA IMPOSSÍVEL NA FILEIRA A, C");
                return false;
            }
        }
        moverJogador(letraFileira, numeroColuna);
        return true;
    }

    public void setNextFrameAnimationData() {
        dadosAnimacao.nextFrame();
    }

    public void setJumpAnimation() {
        isJumping = true;
        dadosAnimacao.setCurrentAnimation(modeloJogador.getAnimationList().get(2));

    }

    public void setIdleAnimation() {
        isJumping = false;
        dadosAnimacao.setCurrentAnimation(modeloJogador.getAnimationList().get(1));

    }
    public boolean isLastAnimationFrame() {
        return dadosAnimacao.isLastFrame();
    }



    public void setPosicalInicial() {
        entidadeJogador.setPosition(posInicial.x, posInicial.y + 0.1f, posInicial.z);
        rotInicial = cuboAtual.getRotation();
        entidadeJogador.setRotation(rotInicial);

        entidadeJogador.setScale(0.04f);

        entidadeJogador.updateModelMatrix();
    }
    
    public boolean descerEsquerda() {

        int numeroColuna = Integer.parseInt(idCuboAtual.charAt(1) + "");
        char letraFileira = idCuboAtual.charAt(0);
        entidadeJogador.setRotation(0, 1, 0, (float)-(Math.PI / 4));

        if(letraFileira == 'A' || letraFileira == 'C' || letraFileira == 'E') {
            if(numeroColuna == 0 && letraFileira != 'E') {
                // movimento impossível
                System.out.println("MOVIMENTO P/ ESQUERDA IMPOSSÍVEL NA FILEIRA A, C OU E");
                return false;
            }
            if(numeroColuna > 0) {
                numeroColuna--;
            }
        }

        moverJogador(letraFileira, numeroColuna);
        return true;

    }

    private void moverJogador(char letraFileira, int numeroColuna) {
        

        char proximaFileira = letraFileira == 'E' ? 'A' : (char) (letraFileira + 1);
        String proximaPosicao = "" + proximaFileira + numeroColuna;
        // System.out.println("Proxima posição seria " +  proximaPosicao);
        // System.out.println("Int coluna " + numeroColuna);
        cuboAtual = modeloCubo.getEntityById("entidade-cubo-" + proximaPosicao, modeloCubo);
        var posicaoAtual= cuboAtual.getPosition();

        entidadeJogador.setPosition(posicaoAtual.x, posicaoAtual.y + (0.1f), posicaoAtual.z);
        entidadeJogador.updateModelMatrix();
        idCuboAtual = proximaPosicao;
        setIdleAnimation();
    }

    public float getPosicaoY() {
        return entidadeJogador.getPosition().y;
    }

}