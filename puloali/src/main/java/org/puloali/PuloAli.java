package org.puloali;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.puloali.engine.Engine;
import org.puloali.engine.IAppLogic;
import org.puloali.engine.MouseInput;
import org.puloali.engine.Window;
import org.puloali.engine.graph.Render;
import org.puloali.engine.scene.Camera;
import org.puloali.engine.scene.Fog;
import org.puloali.engine.scene.Jogador;
import org.puloali.engine.scene.Mapa;
import org.puloali.engine.scene.Scene;
import org.puloali.engine.scene.SkyBox;
import org.puloali.engine.scene.lights.AmbientLight;
import org.puloali.engine.scene.lights.DirLight;
import org.puloali.engine.scene.lights.SceneLights;

import static org.lwjgl.glfw.GLFW.*;

public class PuloAli implements IAppLogic {


    private static final float MOUSE_SENSITIVITY = 0.1f;

    private static final float MOVEMENT_SPEED = 0.005f;

	private boolean esperandoClique = false;
	private long tempoUltimoClique = 0;
	private final long atrasoClique = 500; 

	private boolean rolagemLigada = false;

	private long tempoFileiraTopo = 0;
	private final long atrasoFileiraTopo = 10;
	private boolean esperandoRolar = false;
	private long tempoRolarParaCima = 0;
	private final long atrasoRolarParaCima = 10;

    private Mapa mapa;
	private Jogador jogador;

	private boolean gameOver;

    private float lightAngle;

    public static void main(String[] args) {
        PuloAli main = new PuloAli();
        Window.WindowOptions opts = new Window.WindowOptions();
        opts.antiAliasing = true;
        Engine gameEng = new Engine("pulo ali new code base", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {

        mapa = new Mapa(scene);
		jogador = new Jogador(scene);

        SkyBox skyBox = new SkyBox(
        "puloali/src/main/resources/models/skybox/skybox.obj", 
        scene.getTextureCache());
		skyBox.getSkyBoxEntity().setScale(200);
		skyBox.getSkyBoxEntity().updateModelMatrix();
		scene.setSkyBox(skyBox);

		scene.setFog(
			new Fog(
				true,
				new Vector3f(0.5f, 0.5f, 0.5f),
				0.57f
			)
		);

        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(0.5f);
        ambientLight.setColor(0.3f, 0.3f, 0.3f);

        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(0, 1, 0);
        dirLight.setIntensity(1.0f);
        scene.setSceneLights(sceneLights);


        // Camera camera = scene.getCamera();
        // camera.setPosition(-1.5f, 3.0f, 4.5f);
        // camera.addRotation((float) Math.toRadians(15.0f), (float) Math.toRadians(390.f));

        lightAngle = 0;
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
       
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            lightAngle -= 2.5f;
            if (lightAngle < -90) {
                lightAngle = -90;
            }
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            lightAngle += 2.5f;
            if (lightAngle > 90) {
                lightAngle = 90;
            }
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }
        long tempoAtual = System.currentTimeMillis();
        if (mouseInput.isLeftButtonPressed()) {
            rolagemLigada = true;
			if (!esperandoClique ||(tempoAtual - tempoUltimoClique) >= atrasoClique) {
				esperandoClique = true;
				tempoUltimoClique = tempoAtual;
	
				Vector2f posicaoMouse = mouseInput.getCurrentPos();
				float metadeLarguraJanela = window.getWidth() / 2.0f;
				boolean estaNaMetadeEsquerda = posicaoMouse.x < metadeLarguraJanela;

				if (estaNaMetadeEsquerda) {
					// System.out.println("CLIQUE METADE ESQUERDA");
					if(jogador.getPosicaoY() > 0.14) {
						jogador.descerEsquerda();
					}
				
				} else {
					// System.out.println("CLIQUE NA METADE DIREITA");
					if(jogador.getPosicaoY() > 0.14) {
						jogador.descerDireita();
					}
				}
			}
		} else {
			esperandoClique = false;
		}

        SceneLights sceneLights = scene.getSceneLights();
        DirLight dirLight = sceneLights.getDirLight();
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().z = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        if(rolagemLigada) {
			if(!gameOver && jogador.getPosicaoY() > 0.45) {
				System.out.println("GAME OVER" + jogador.getPosicaoY());
				rolagemLigada = false;
				gameOver = true;
				// mapa.removerMapaDeCena();
				// jogador.removerJogadorDeCena();
				return; 
			};
			if(!esperandoRolar) {
				tempoFileiraTopo += 1;
			}
			if(!esperandoRolar && tempoFileiraTopo >= atrasoFileiraTopo) {
				mapa.moverFileiraTopoParaBaixo();
				esperandoRolar = true;
			}

			if(esperandoRolar) {
				tempoRolarParaCima += 1;
				if(tempoRolarParaCima >= atrasoRolarParaCima) {
					mapa.moverTudoParaCima();
					esperandoRolar = false;
					tempoFileiraTopo = 0;
					tempoRolarParaCima = 0;
					if(mapa.getXPosicaoReferencia() > -0.2f) {
						mapa.moverTudoParaEsquerda();
					}

				}
			}

		}
		
    }

}
