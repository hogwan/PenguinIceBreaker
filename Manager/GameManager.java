package Manager;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import GameObject.*;
import Interface.Drawable;
import GameEnum.*;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : GameManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 전체적인 게임의 흐름과 객체들을 관리하는 클래스
 */

/**
 * GameManager는 게임 내 모든 객체를 관리하고, 게임 상태를 제어하는 클래스입니다.
 * 객체의 추가, 삭제, 렌더링 및 게임 진행 상태를 관리합니다.
 */
public class GameManager {

    // 싱글턴 패턴을 위한 GameManager 인스턴스
    private static GameManager instance;

    // 게임 객체들을 저장할 맵 (객체 종류별로 구분)
    private HashMap<ObjectType, LinkedList<GameObject>> objectMap;
    
    // 살아있는 펭귄 리스트
    private ArrayList<Penguin> alivePenguins;
    
    // 우승 펭귄
    private Penguin winnerPenguin = null;

    // GameManager 생성자 (싱글턴 패턴)
    private GameManager() {
        objectMap = new HashMap<>();
        alivePenguins = new ArrayList<>();
        // 게임 객체 유형에 대한 빈 리스트 초기화
        for (int i = 0; i < ObjectType.END.ordinal(); i++) {
            objectMap.put(ObjectType.fromInt(i), new LinkedList<>());
        }
    }

    // GameManager 인스턴스를 반환하는 메소드 (싱글턴)
    public static GameManager getInst() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * 게임 객체를 추가하는 메소드.
     * @param obj 추가할 게임 객체
     */
    public void addObject(GameObject obj) {
        GameEnum.ObjectType type = obj.getType();
        objectMap.get(type).add(obj);
    }

    /**
     * 게임 객체를 삭제하는 메소드.
     * @param obj 삭제할 게임 객체
     */
    public void deleteObject(GameObject obj) {
        objectMap.get(obj.getType()).remove(obj);
    }

    /**
     * 게임 화면을 그리는 메소드.
     * @param g Graphics 객체
     * @param observer 이미지 옵저버
     */
    public void render(Graphics g, ImageObserver observer) {
        ArrayList<GameObject> allObjects = new ArrayList<>();

        // 모든 객체를 순회하여 Drawable 객체만 모음
        for (int i = 0; i < ObjectType.END.ordinal(); i++) {
            LinkedList<GameObject> list = objectMap.get(ObjectType.fromInt(i));
            for (GameObject obj : list) {
                if (obj.isRemoved()) continue;
                if (!(obj instanceof Drawable)) continue;
                allObjects.add(obj);
            }
        }

        // 객체들을 정렬
        Collections.sort(allObjects);

        // 객체들을 그리기
        for (GameObject obj : allObjects) {
            Drawable dobj = (Drawable) obj;
            dobj.draw(g, obj.getPoint(), observer);
        }

        // 제거된 객체는 리스트에서 제거
        release();
    }

    /**
     * 제거된 객체들을 리스트에서 제거하는 메소드
     */
    public void release() {
        for (int i = 0; i < GameEnum.ObjectType.END.ordinal(); i++) {
            LinkedList<GameObject> list = objectMap.get(ObjectType.fromInt(i));
            Iterator<GameObject> iter = list.iterator();
            while (iter.hasNext()) {
                if (iter.next().isRemoved()) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * 게임을 재시작하는 메소드.
     */
    public void restart() {
        allClear();
        start();
    }

    /**
     * 게임을 시작하는 메소드.
     */
    public void start() {
        ResourceManager.getInst().init();
        IceManager.getInst().generateSquare();
        placePenguin();
    }

    /**
     * 망치 객체를 생성하여 추가하는 메소드.
     */
    public void SpawnHammer() {
        GameObject object = new Hammer();
        addObject(object);
    }

    /**
     * 모든 객체를 초기화하는 메소드.
     */
    private void allClear() {
        for (int i = 0; i < ObjectType.END.ordinal(); i++) {
            objectMap.get(ObjectType.fromInt(i)).clear();
        }
        IceManager.getInst().clearIceMap();
    }

    /**
     * 살아있는 펭귄 리스트를 반환하는 메소드.
     * @return 살아있는 펭귄 리스트
     */
    public ArrayList<Penguin> getAlivePenguins() {
        return alivePenguins;
    }

    /**
     * 펭귄을 게임 맵에 배치하는 메소드.
     */
    public void placePenguin() {
        for (GameObject object : objectMap.get(ObjectType.PENGUIN)) {
            if (!(object instanceof Penguin)) continue;

            Penguin penguin = (Penguin) object;
            IceManager.getInst().placePenguin(penguin);
        }
    }

    /**
     * 펭귄 이름을 받아 펭귄을 생성하고 게임에 등록하는 메소드.
     * @param penguinNames 펭귄 이름 리스트
     */
    public void registerPenguin(List<String> penguinNames) {
        for (int i = 0; i < penguinNames.size(); i++) {
            Penguin penguin = new Penguin(penguinNames.get(i), PenguinColor.fromInt(i % PenguinColor.END.ordinal()));
            alivePenguins.add(penguin);
            addObject(penguin);
        }
    }

    /**
     * 펭귄이 죽었을 때 호출되는 메소드.
     * @param penguin 죽은 펭귄
     */
    public void deathCheckPenguin(Penguin penguin) {
        alivePenguins.remove(penguin);
    }

    /**
     * 우승 펭귄을 반환하는 메소드.
     * @return 우승 펭귄
     */
    public Penguin getWinnerPenguin() {
        return winnerPenguin;
    }

    /**
     * 게임 종료 여부를 확인하는 메소드.
     * @return 게임 종료 여부
     */
    public boolean endCheck() {
        int aliveCount = 0;
        for (Penguin penguin : alivePenguins) {
            if (penguin.getCurState() == PenguinState.ALIVE) {
                aliveCount++;
                winnerPenguin = penguin;
            }
        }

        // 살아있는 펭귄이 1명이라면 게임 종료
        if (aliveCount == 1) {
            return true;
        }
        // 살아있는 펭귄이 없으면 무작위로 우승자를 선택하여 종료
        else if (aliveCount == 0) {
            int random = new Random().nextInt(alivePenguins.size());
            winnerPenguin = alivePenguins.get(random);
            return true;
        } else {
            // 죽은 펭귄 제거
            Iterator<Penguin> iter = alivePenguins.iterator();
            while (iter.hasNext()) {
                Penguin penguin = iter.next();
                if (penguin.getCurState() == PenguinState.DEAD) {
                    iter.remove();
                }
            }
        }

        return false;
    }
}
