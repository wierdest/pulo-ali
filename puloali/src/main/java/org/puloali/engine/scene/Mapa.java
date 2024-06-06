package org.puloali.engine.scene;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.puloali.engine.graph.Model;



public class Mapa {
    Map<String, List<Entity>> blocos;

    private List<String> fileiras;

    private String fileiraJag;

    private Scene cena;

    private Camera camera;

    private Model modeloCubo;

    private Entity player;

    private Entity blocoDeReferencia;

    private static final float POS_Z = 2.1f;
    
    public Mapa(Scene cena) {

        modeloCubo = ModelLoader.loadModel(
        "modelo-cubo", 
        "puloali/src/main/resources/models/tiles/basic-tile.obj", 
        cena.getTextureCache(), false);
        
        cena.addModel(modeloCubo);

        camera = cena.getCamera();

        blocos = new HashMap<>();

        fileiras = new ArrayList<>();


        desenharFileiraA(cena, modeloCubo, camera);

        desenharFileiraB(cena, modeloCubo, camera);

        desenharFileiraC(cena, modeloCubo, camera);

        desenharFileiraD(cena, modeloCubo, camera);

        desenharFileiraE(cena, modeloCubo, camera);

        this.cena = cena;
       

    }

    private void desenharFileiraA(Scene cena, Model modeloCubo, Camera camera) {
        int numCubosPorFileira = 5;
        float larguraCubo = 0.1f;
        float alturaCubo = 0.1f;  
        float espacamentoHorizontal = larguraCubo;
        float espacamentoVertical = alturaCubo;  
        float posXInicial = -3 * espacamentoHorizontal; 
        float posYInicial = 3 * espacamentoVertical; 
        float posZ = -POS_Z; 
        String idEntidade = "entidade-cubo-A";

        desenharCubosPorFileira(numCubosPorFileira, posXInicial, posYInicial, espacamentoHorizontal, posZ, idEntidade, modeloCubo, cena);

    }

    private void desenharFileiraB(Scene cena, Model modeloCubo, Camera camera) {
        int numCubosPorFileira = 4;
        float larguraCubo = 0.1f;
        float alturaCubo = 0.1f;  
        float espacamentoHorizontal = larguraCubo;
        float espacamentoVertical = alturaCubo;  
        float posXInicial = -2.5f * espacamentoHorizontal; 
        float posYInicial = 2.1f * espacamentoVertical; 
        float posZ = -(POS_Z - 0.1f); 
        String idEntidade = "entidade-cubo-B";

        desenharCubosPorFileira(numCubosPorFileira, posXInicial, posYInicial, espacamentoHorizontal, posZ, idEntidade, modeloCubo, cena);

    }

    private void desenharFileiraC(Scene cena, Model modeloCubo, Camera camera) {
        int numCubosPorFileira = 5;
        float larguraCubo = 0.1f;
        float alturaCubo = 0.1f;  
        float espacamentoHorizontal = larguraCubo;
        float espacamentoVertical = alturaCubo;  
        float posXInicial = -3f * espacamentoHorizontal; 
        float posYInicial = 1.2f * espacamentoVertical; 
        float posZ = -(POS_Z - 0.2f); 
        String idEntidade = "entidade-cubo-C";

        desenharCubosPorFileira(numCubosPorFileira, posXInicial, posYInicial, espacamentoHorizontal, posZ, idEntidade, modeloCubo, cena);
        
    }

    private void desenharFileiraD(Scene cena, Model modeloCubo, Camera camera) {
        int numCubosPorFileira = 4;
        float larguraCubo = 0.1f;
        float alturaCubo = 0.1f;  
        float espacamentoHorizontal = larguraCubo;
        float espacamentoVertical = alturaCubo;  
        float posXInicial = -2.5f * espacamentoHorizontal; 
        float posYInicial = 0.33f * espacamentoVertical; 
        float posZ = -(POS_Z - 0.3f); 
        String idEntidade = "entidade-cubo-D";

        desenharCubosPorFileira(numCubosPorFileira, posXInicial, posYInicial, espacamentoHorizontal, posZ, idEntidade, modeloCubo, cena);
        
    }

    private void desenharFileiraE(Scene cena, Model modeloCubo, Camera camera) {
        int numCubosPorFileira = 5;
        float larguraCubo = 0.1f;
        float alturaCubo = 0.1f;  
        float espacamentoHorizontal = larguraCubo;
        float espacamentoVertical = alturaCubo;  
        float posXInicial = -3.0f * espacamentoHorizontal; 
        float posYInicial = -0.56f * espacamentoVertical; 
        float posZ = -(POS_Z - 0.4f); 
        String idEntidade = "entidade-cubo-E";

        desenharCubosPorFileira(numCubosPorFileira, posXInicial, posYInicial, espacamentoHorizontal, posZ, idEntidade, modeloCubo, cena);
        
    }

    private void desenharCubosPorFileira(
        int numCubosPorFileira, float posXInicial, float posYInicial, 
        float espacamentoHorizontal, float posZ, String idEntidade, 
        Model modeloCubo, Scene cena) {

        List<Entity> fileira = new ArrayList<>();

        for (int coluna = 0; coluna < numCubosPorFileira; coluna++) {
            float posX = posXInicial + coluna * espacamentoHorizontal;
            float posY = posYInicial;
    
            Entity entidadeCubo = new Entity(idEntidade + coluna, modeloCubo.getId());
            entidadeCubo.setPosition(posX, posY, posZ);
            entidadeCubo.setRotation(0, 1, 0, (float)Math.toRadians(45)); // Rotação de 45 graus no eixo Y
            entidadeCubo.setScale(0.1f); // Ajuste a escala conforme necessário
            entidadeCubo.updateModelMatrix();
    
            cena.addEntity(entidadeCubo);

            fileira.add(entidadeCubo);

        }
        blocos.put(idEntidade, fileira);
        fileiras.add(idEntidade);
    }

    public void moverFileiraTopoParaBaixo() {
        if(player == null) {
            Model modeloJogador = cena.getModelMap().get("modelo-jogador");
            player = modeloJogador.getEntityById("entidade-jogador", modeloJogador);
        }
        // usamos o nosso Map blocos para mover o mapa.
        // a primeira tarefa é pegar a primeira fileira do topo e jogar pra baixo
        List<Entity> topo = blocos.get(fileiras.get(0));
        // pega um bloco do final
        blocoDeReferencia  = blocos.get(fileiras.get(fileiras.size() - 1)).get(0);
   
        // move a fileira do topo para baixo
        for(Entity e : topo) {
            e.setPosition(e.getPosition().x, blocoDeReferencia.getPosition().y, blocoDeReferencia.getPosition().z);
            e.setRotation(blocoDeReferencia.getRotation());
            e.updateModelMatrix();
        }
        // move na lista de ordem das fileiras
        String ultimoTopo = fileiras.remove(0);
        fileiraJag = ultimoTopo;
        fileiras.add(ultimoTopo);
    }

    public void moverTudoParaEsquerda() {
        for(int i = 0; i < fileiras.size(); i++) {
            List<Entity> fileira = blocos.get(fileiras.get(i));
            for(Entity e : fileira) {
                e.setPosition(e.getPosition().x -0.05f, e.getPosition().y, e.getPosition().z);
                e.updateModelMatrix();
            }
        }
        
        player.setPosition(player.getPosition().x -0.05f, player.getPosition().y, player.getPosition().z);
        player.updateModelMatrix();
    }

    public void moverTudoParaCima() {
        // o segundo passo é rolar todas as fileiras menos a ultima para cima
        for(int i = 0; i < fileiras.size()-1; i++) {
            List<Entity> fileira = blocos.get(fileiras.get(i));
            for(Entity e : fileira) {
                e.setPosition(e.getPosition().x, e.getPosition().y + 0.1f, e.getPosition().z - 0.09f);
                e.updateModelMatrix();
            }
        }
        
        player.setPosition(player.getPosition().x, player.getPosition().y + 0.1f, player.getPosition().z -0.09f);
        player.updateModelMatrix();

        List<Entity> quartaFileira = blocos.get(fileiras.get(3));
        List<Entity> quintaFileira = blocos.get(fileiras.get(4));

        for(Entity e : quintaFileira) {
            float posicaoX = e.getPosition().x;
            if(e.getId().contains(fileiraJag)) {
                posicaoX += 0.05f;         
            } 
            e.setPosition(posicaoX, e.getPosition().y, e.getPosition().z);
            e.updateModelMatrix();
        }

        for(Entity e : quartaFileira) {
            float posicaoX = e.getPosition().x;
            if(e.getId().contains(fileiraJag)) {   
                posicaoX -= 0.05f;         
            } 
            e.setPosition(posicaoX, e.getPosition().y, e.getPosition().z);
            e.updateModelMatrix();
        }

    }
    
    public float getXPosicaoReferencia() {
        return blocoDeReferencia.getPosition().x;
    }

    public void removerMapaDeCena() {
        for(String fileira : fileiras) {
            for(Entity entidade : blocos.get(fileira)) {
                cena.removeEntity(entidade);
            }
        }
    }
}