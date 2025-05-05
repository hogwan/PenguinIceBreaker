package GameObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import Interface.Drawable;
import GameEnum.*;
import Manager.*;
import javax.swing.Timer;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : Penguin.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 얼음 위에 올라갈 펭귄 클래스
 */

/**
 * Penguin 클래스는 게임 내 펭귄 객체를 나타내며,
 * 펭귄의 상태와 색상, 이름 등을 관리하고 화면에 그리는 기능을 수행합니다.
 */
public class Penguin extends GameObject implements Drawable {

    // 펭귄의 현재 상태 (생존, 죽음, 떨어짐 등)
    private PenguinState curState;

    // 펭귄의 색상
    private PenguinColor color;

    // 펭귄의 이름
    private String name;

    /**
     * 생성자: 이름과 색상을 받아 펭귄 객체를 초기화합니다.
     * 
     * @param name 펭귄의 이름
     * @param color 펭귄의 색상
     */
    public Penguin(String name, PenguinColor color) {
        this.name = name;
        this.color = color;
        this.curState = PenguinState.ALIVE; // 초기 상태는 살아있는 상태
        setType(GameEnum.ObjectType.PENGUIN); // 객체 유형 설정
    }

    // 현재 펭귄의 상태를 반환합니다.
    public PenguinState getCurState() {
        return curState;
    }

    /**
     * 펭귄의 상태를 설정합니다.
     * 상태가 FALLING이면, 500ms 후 펭귄을 삭제하고 DEAD 상태로 설정합니다.
     * 
     * @param curState 새로 설정할 펭귄의 상태
     */
    public void setCurState(PenguinState curState) {
        this.curState = curState;

        // FALLING 상태일 때 500ms 후 죽은 상태로 처리
        if (this.curState == PenguinState.FALLING) {
            new Timer(500, e -> {
                // 타이머 종료 후 펭귄 제거 및 상태 변경
                ((Timer) e.getSource()).stop();
                setRemoved(true);
                setCurState(PenguinState.DEAD);
            }).start();
        }
    }

    // 펭귄의 이름을 반환합니다.
    public String getName() {
        return name;
    }

    // 펭귄의 이름을 설정합니다.
    public void setName(String name) {
        this.name = name;
    }

    // 펭귄의 색상을 반환합니다.
    public PenguinColor getPenguinColor() {
        return color;
    }

    // 펭귄의 색상을 설정합니다.
    public void setPenguinColor(PenguinColor color) {
        this.color = color;
    }

    /**
     * 펭귄을 화면에 그립니다.
     * 상태와 색상에 따라 적절한 이미지를 선택하여 그립니다.
     * 
     * @param g 그래픽 객체
     * @param point 펭귄의 위치
     * @param observer 이미지 옵저버
     */
    @Override
    public void draw(Graphics g, Point point, ImageObserver observer) {
        Image imageToDraw = null;
        PenguinColor color = getPenguinColor();

        // FALLING 상태일 경우 떨어지는 펭귄 이미지 선택
        if (curState == PenguinState.FALLING) {
            imageToDraw = getFallingPenguinImage(color);
        } else {
            // 살아있는 상태일 경우 일반 펭귄 이미지 선택
            imageToDraw = getNormalPenguinImage(color);
        }

        // 이미지가 존재하면 화면에 그리기
        if (imageToDraw != null) {
            g.drawImage(imageToDraw, point.getPosition().getX(), point.getPosition().getY(),
                    GameSetting.HEX_SIZE, GameSetting.HEX_HEIGHT, observer);
        }

        // 2D 그래픽으로 변환하여 텍스트 처리
        Graphics2D g2 = (Graphics2D) g;

        // 텍스트 설정: 텍스트는 검은색, 폰트는 '맑은 고딕'으로 설정
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        // 텍스트 위치 계산 (이미지 위쪽 중앙에 텍스트 표시)
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int textX = point.getPosition().getX() + Math.abs(GameSetting.HEX_SIZE - textWidth) / 2;
        int textY = point.getPosition().getY() + 10; // 이미지보다 약간 위에 표시

        // 이름 텍스트 그리기
        g2.drawString(name, textX, textY);
    }

    /**
     * FALLING 상태일 때 사용할 이미지 선택
     * 
     * @param color 펭귄의 색상
     * @return 해당 색상에 맞는 떨어지는 펭귄 이미지
     */
    private Image getFallingPenguinImage(PenguinColor color) {
        switch (color) {
            case RED:
                return ResourceManager.getInst().getImage("fallingRedPenguin");
            case BLUE:
                return ResourceManager.getInst().getImage("fallingBluePenguin");
            case GREEN:
                return ResourceManager.getInst().getImage("fallingGreenPenguin");
            case YELLOW:
                return ResourceManager.getInst().getImage("fallingYellowPenguin");
            case PURPLE:
                return ResourceManager.getInst().getImage("fallingPurplePenguin");
            default:
                return null;
        }
    }

    /**
     * ALIVE 상태일 때 사용할 이미지 선택
     * 
     * @param color 펭귄의 색상
     * @return 해당 색상에 맞는 살아있는 펭귄 이미지
     */
    private Image getNormalPenguinImage(PenguinColor color) {
        switch (color) {
            case RED:
                return ResourceManager.getInst().getImage("redPenguin");
            case BLUE:
                return ResourceManager.getInst().getImage("bluePenguin");
            case GREEN:
                return ResourceManager.getInst().getImage("greenPenguin");
            case YELLOW:
                return ResourceManager.getInst().getImage("yellowPenguin");
            case PURPLE:
                return ResourceManager.getInst().getImage("purplePenguin");
            default:
                return null;
        }
    }
}
