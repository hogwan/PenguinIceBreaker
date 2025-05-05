package GameObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import Manager.IceManager;
import Manager.ResourceManager;
import Interface.Drawable;
import Manager.GameSetting;
import GameEnum.*;
import Interface.AfterHammerSwing;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : Hammer.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 버튼 클릭 시 생성되는 해머
 */


/**
 * Hammer 클래스는 게임 내 해머 오브젝트를 나타내며,
 * 등장 시 특정 얼음을 랜덤하게 골라 그 위치로 낙하하며,
 * 회전 애니메이션 후 충격을 전달합니다.
 */
public class Hammer extends GameObject implements Drawable {

    // 회전 각도 관련 변수
    private double theta = 0.0;              // 현재 회전 각도
    private final float rotateSpeed = 300.f; // 사용되지 않음 (보류 가능)
    private final float angle = 90.f;         // 사용되지 않음 (보류 가능)

    /**
     * 생성자: 등장 가능한 얼음 타일 중 하나를 랜덤하게 골라 그 위치로 해머를 떨어뜨림.
     * 유효한 타일이 없다면 삭제됨으로 처리.
     */
    public Hammer() {
        setType(GameEnum.ObjectType.HAMMER);

        // 충격 가능한 타일 목록 조회
        ArrayList<Ice> hitableIces = IceManager.getInst().searchValidHammerPosition();
        Random random = new Random();

        if (!hitableIces.isEmpty()) {
            // 랜덤으로 타일 선택
            Ice selectedIce = hitableIces.get(random.nextInt(hitableIces.size()));

            // 해당 위치에 해머 생성 (약간의 위치 오프셋 적용)
            Point point = new Point(selectedIce.getPoint().getY(), selectedIce.getPoint().getX(), selectedIce.getPoint().isOdd());
            point.setXOffset(GameSetting.HEX_SIZE * 3 / 2);      // X축 오프셋: 오른쪽으로 이동
            point.setYOffset(-GameSetting.HEX_HEIGHT / 2);       // Y축 오프셋: 위로 이동
            this.setPoint(point);

            swing(); // 회전 애니메이션 시작
        } else {
            setRemoved(true); // 등장할 수 있는 위치가 없을 경우 삭제
        }
    }

    /**
     * 해머를 회전 애니메이션시키며 일정 각도 이후 충격을 발생시킴.
     */
    public void swing() {
        new Timer(1000 / GameSetting.FPS, e -> {
            theta += rotateSpeed * GameSetting.DELTATIME;

            // 일정 각도 도달 시 타이머 종료 및 충격 처리
            if (theta >= angle) {
                ((Timer) e.getSource()).stop();
                hit();
                setRemoved(true);
            }
        }).start();
    }

    /**
     * 실제 얼음에 충격을 주는 로직 수행.
     */
    private void hit() {
        IceManager.getInst().impact(getPoint(), new AfterHammerSwing());
    }

    /**
     * 해머 이미지 렌더링
     *
     * @param g        그래픽 컨텍스트
     * @param point    해머가 그려질 위치
     * @param observer 이미지 옵저버
     */
    @Override
    public void draw(Graphics g, Point point, ImageObserver observer) {
        if (isRemoved()) return;

        Image imageToDraw = ResourceManager.getInst().getImage("hammer");

        if (imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g;

            double angleRad = Math.toRadians(theta);

            // 해머 중심 기준으로 회전 적용
            int centerX = point.getPosition().getX() + GameSetting.HEX_SIZE / 2;
            int centerY = point.getPosition().getY() + GameSetting.HEX_HEIGHT;

            g2d.rotate(-angleRad, centerX, centerY);
            g.drawImage(imageToDraw,
                    point.getPosition().getX(),
                    point.getPosition().getY(),
                    GameSetting.HEX_SIZE,
                    GameSetting.HEX_HEIGHT,
                    observer);
            g2d.rotate(angleRad, centerX, centerY); // 회전 복원
        }
    }
}
