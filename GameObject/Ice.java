package GameObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.Timer;
import Interface.Drawable;
import Manager.*;
import GameEnum.*;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : Ice.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 빙판 클래스
 */

/**
 * Ice 클래스는 게임 내 얼음 블록을 나타내며, 
 * 상태에 따라 다르게 그려지고, HP가 0이 되면 깨지는 등의 동작을 수행합니다.
 */
public class Ice extends GameObject implements Drawable {

    // 얼음의 최대 HP
    public static final int MAX_HP = 10;

    // 현재 얼음의 상태
    private IceState curState = IceState.NONE;

    // 얼음 위에 올려진 펭귄 (있을 수도 있고 없을 수도 있음)
    private Penguin penguin = null;

    // 얼음의 현재 HP
    private int hp;

    // 얼음의 충격 애니메이션을 위한 각도
    private double theta = 0.0;

    /**
     * 기본 생성자: 기본 상태의 얼음 생성.
     * 
     * @param point 얼음이 위치할 좌표.
     */
    public Ice(Point point) {
        super(point);
        curState = IceState.NONE;
        setType(GameEnum.ObjectType.ICE);
    }

    /**
     * HP를 지정하여 얼음을 생성하는 생성자.
     * 
     * @param point 얼음이 위치할 좌표.
     * @param hp 얼음의 초기 HP.
     */
    public Ice(Point point, int hp) {
        super(point);
        setHp(hp);
        setType(GameEnum.ObjectType.ICE);
    }

    /**
     * 얼음을 화면에 그립니다.
     * 
     * @param g        그래픽 컨텍스트
     * @param point    얼음의 위치
     * @param observer 이미지 옵저버
     */
    @Override
    public void draw(Graphics g, Point point, ImageObserver observer) {
        if (isRemoved()) return;

        Image imageToDraw = null;
        IceState state = getCurState();

        // 현재 얼음 상태에 맞는 이미지를 선택
        switch (state) {
            case NORMAL:
                imageToDraw = ResourceManager.getInst().getImage("normalIce");
                break;
            case CRACKED:
                imageToDraw = ResourceManager.getInst().getImage("crackedIce");
                break;
            case BROKEN:
                imageToDraw = ResourceManager.getInst().getImage("brokenIce");
                break;
            default:
                break;
        }

        // 이미지를 그리기
        if (imageToDraw != null) {
            g.drawImage(imageToDraw, point.getPosition().getX(), point.getPosition().getY(),
                    GameSetting.HEX_SIZE, GameSetting.HEX_HEIGHT, observer);
        }
    }

    /**
     * 현재 얼음 상태를 반환합니다.
     * 
     * @return 얼음의 현재 상태
     */
    public IceState getCurState() {
        return curState;
    }

    /**
     * 얼음의 상태를 설정하고, 상태가 'BROKEN'이면 1초 후 삭제 처리.
     * 
     * @param State 새로 설정할 얼음의 상태
     */
    public void setCurState(IceState State) {
        this.curState = State;

        if (State == IceState.BROKEN) {
            // 'BROKEN' 상태가 되면 1초 후 얼음을 삭제
            new Timer(200, e -> {
                setRemoved(true);

                // 만약 펭귄이 올려져 있으면 펭귄의 상태를 'FALLING'으로 변경
                if (getPenguin() != null) {
                    getPenguin().setCurState(PenguinState.FALLING);
                }

                // 타이머 종료
                ((Timer) e.getSource()).stop();
            }).start();
        }
    }

    /**
     * 얼음의 현재 HP를 반환합니다.
     * 
     * @return 얼음의 HP
     */
    public int getHp() {
        return hp;
    }

    /**
     * 얼음의 HP를 설정하고 상태에 따라 얼음의 상태를 업데이트합니다.
     * 
     * @param hp 새로운 HP 값
     */
    public void setHp(int hp) {
        this.hp = Math.max(hp, 0); // HP는 0 이하로 떨어지지 않음

        // 상태에 따라 얼음 상태 변경
        if (this.hp <= 0) {
            setCurState(IceState.BROKEN);
        } else if (hp <= MAX_HP / 2) {
            setCurState(IceState.CRACKED);
        } else {
            setCurState(IceState.NORMAL);
        }
    }

    /**
     * 얼음이 충격을 받았을 때 호출되어 애니메이션과 HP 감소 처리.
     * 
     * @param damage 충격에 의한 피해량
     */
    public void getHit(int damage) {
        startBounceAnimation(damage);
    }

    /**
     * 얼음이 충격을 받았을 때 튕겨나가는 애니메이션을 시작합니다.
     * 
     * @param damage 충격에 의한 피해량
     */
    private void startBounceAnimation(int damage) {
        new Timer(1000 / GameSetting.FPS, e -> {
            theta += 900.0 * GameSetting.DELTATIME;
            double offset = -GameSetting.IMPACTAMPLITUDE * Math.sin(theta);
            getPoint().setYOffset((int) offset);

            // 펭귄이 있을 경우, 펭귄의 Y 오프셋도 동일하게 변경
            if (getPenguin() != null) {
                getPenguin().getPoint().setYOffset(GameSetting.PENGUINYOFFSET + (int) offset);
            }

            // 애니메이션 종료 후, 얼음의 HP를 감소시키고 초기화
            if (theta >= 180.0) {
                ((Timer) e.getSource()).stop();
                theta = 0.f;
                getPoint().setYOffset(0);

                // 펭귄의 위치를 원래대로 복원
                if (getPenguin() != null) {
                    getPenguin().getPoint().setYOffset(GameSetting.PENGUINYOFFSET);
                }

                // HP 감소 처리
                setHp(hp - damage);
            }
        }).start();
    }

    /**
     * 얼음 위에 올려진 펭귄을 반환합니다.
     * 
     * @return 펭귄 객체
     */
    public Penguin getPenguin() {
        return this.penguin;
    }

    /**
     * 펭귄을 얼음에 올립니다.
     * 
     * @param penguin 올릴 펭귄 객체
     */
    public void putOnPenguin(Penguin penguin) {
        this.penguin = penguin;
        Point point = new Point(getPoint().getY(), getPoint().getX(), getPoint().isOdd());
        point.setYOffset(GameSetting.PENGUINYOFFSET);
        this.penguin.setPoint(point);
    }

    /**
     * 얼음에서 펭귄을 떼어냅니다.
     */
    public void detachPenguin() {
        this.penguin = null;
    }
}
