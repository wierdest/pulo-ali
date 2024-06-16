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
import org.puloali.engine.scene.BlocoUI;
import org.puloali.engine.scene.Scene;
import org.puloali.engine.scene.SkyBox;
import org.puloali.engine.scene.lights.AmbientLight;
import org.puloali.engine.scene.lights.DirLight;
import org.puloali.engine.scene.lights.PointLight;
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
    private boolean estaNaMetadeEsquerda = false;
	private long tempoRolarParaCima = 0;
	private final long atrasoRolarParaCima = 10;

    private Mapa mapa;
	private Jogador jogador;
    private BlocoUI nuvemA;

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

        BlocoUI uiBlock = new BlocoUI(
            scene, 
            "a", 
            "puloali/src/main/resources/models/puloali-ui-block/ui-block.gltf"
        );
        // nuvemA = new Nuvem(
        //     scene, 
        //     "a", 
        //     "puloali/src/main/resources/models/teste/test.gltf"
        // );

        SkyBox skyBox = new SkyBox(
        "puloali/src/main/resources/models/puloali-skybox/skybox.gltf", 
        scene.getTextureCache());

		skyBox.getSkyBoxEntity().setScale(50);
		skyBox.getSkyBoxEntity().updateModelMatrix();
		scene.setSkyBox(skyBox);

		// scene.setFog(
		// 	new Fog(
		// 		true,
		// 		new Vector3f(0.5f, 0.5f, 0.5f),
		// 		0.3f
		// 	)
		// );

        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(1f);
        ambientLight.setColor(0.3f, 0.3f, 0.3f);

        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(jogador.getPosicaoX(), jogador.getPosicaoY(), jogador.getPosicaoZ());
        dirLight.setIntensity(1.2f);

        PointLight pontoLuzJogador = new PointLight(
            new Vector3f(1, 0f, 1),
            new Vector3f(uiBlock.getPosicaoX() - 1.4f, uiBlock.getPosicaoY() + 2.6f, uiBlock.getPosicaoZ() + 0.2f),
            8.2f
        );

        sceneLights.getPointLights().add(pontoLuzJogador);


        scene.setSceneLights(sceneLights);

        Camera camera = scene.getCamera();
        // camera.setPosition(-1.5f, 3.0f, 4.5f);
        camera.addRotation((float) Math.toRadians(1.0f), (float) Math.toRadians(1.0f));

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
        if(mouseInput.isLeftButtonPressed()) {
            
            // rolagemLigada = true;
			if(!esperandoClique ||(tempoAtual - tempoUltimoClique) >= atrasoClique) {
				esperandoClique = true;
				tempoUltimoClique = tempoAtual;
	
				Vector2f posicaoMouse = mouseInput.getCurrentPos();
				float metadeLarguraJanela = window.getWidth() / 2.0f;
				estaNaMetadeEsquerda = posicaoMouse.x < metadeLarguraJanela;

                jogador.setJumpAnimation(estaNaMetadeEsquerda);

				// if (estaNaMetadeEsquerda) {
				// 	// System.out.println("CLIQUE METADE ESQUERDA");
				// 	if(jogador.getPosicaoY() > 0.14) {
				// 		jogador.descerEsquerda();
				// 	}
				
				// } else {
				// 	// System.out.println("CLIQUE NA METADE DIREITA");
				// 	if(jogador.getPosicaoY() > 0.14) {
				// 		jogador.descerDireita();
				// 	}
				// }
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
        jogador.setNextFrameAnimationData();
        // nuvemA.setNextFrameAnimationData();
        if(jogador.isJumping && jogador.isLastAnimationFrame()) {
            if (estaNaMetadeEsquerda) {
            	if(jogador.getPosicaoY() > 0.14) {
            		rolagemLigada = jogador.descerEsquerda();
            	}
            
            } else {
            	// System.out.println("CLIQUE NA METADE DIREITA");
            	if(jogador.getPosicaoY() > 0.14) {
            		rolagemLigada = jogador.descerDireita();
            	}
            }
            
        }
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
                    rolagemLigada = jogador.getPosicaoY() <= 0.4;
                    // System.out.println(jogador.getPosicaoY());

				}
			}

		}
		
    }

}
