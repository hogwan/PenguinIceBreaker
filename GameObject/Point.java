package GameObject;

import Manager.GameSetting;
import GameEnum.*;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : Point.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 좌표를 나타내는 클래스
 */

/**
 * Point 클래스는 육각형 그리드 상의 좌표를 나타냅니다.
 * x, y 좌표와 오프셋 값을 관리하며, 홀수/짝수 행에 따른 좌표 이동을 지원합니다.
 */
public class Point {
    private int y;           // y 좌표
    private int x;           // x 좌표
    private int yOffset;     // y 방향 오프셋 (맵에 그릴 때 위치를 약간씩 이동시킬 수 있음)
    private int xOffset;     // x 방향 오프셋
    private boolean isOdd;   // 홀수 행인지 여부 (짝수 행은 false)

    /**
     * Point 객체를 초기화합니다.
     * 
     * @param y y 좌표
     * @param x x 좌표
     */
    public Point(int y, int x) {
        this.y = y;
        this.x = x;
    }

    /**
     * Point 객체를 초기화합니다. 홀수/짝수 행 여부를 설정할 수 있습니다.
     * 
     * @param y y 좌표
     * @param x x 좌표
     * @param isOdd 홀수 행 여부
     */
    public Point(int y, int x, boolean isOdd) {
        this.y = y;
        this.x = x;
        this.isOdd = isOdd;
    }

    // 홀수 행과 짝수 행에 대한 각 방향별 좌표 이동 값 설정
    public static final int[][] evenOffsets = {
        { -1, -1, 1 }, // 위 왼쪽 홀수
        { -1, 0, 1 },  // 위 오른쪽 홀수
        { 0, +1, 0 },  // 오른쪽 짝수
        { +1, 0, 1 },  // 아래 오른쪽 홀수
        { +1, -1, 1 }, // 아래 왼쪽 홀수
        { 0, -1, 0 },  // 왼쪽 짝수
    };

    public static final int[][] oddOffsets = {
        { -1, 0, 0 },  // 위 왼쪽 짝수
        { -1, +1, 0 }, // 위 오른쪽 짝수
        { 0, +1, 1 },  // 오른쪽 홀수
        { +1, +1, 0 }, // 아래 오른쪽 짝수
        { +1, 0, 0 },  // 아래 왼쪽 짝수
        { 0, -1, 1 },  // 왼쪽 홀수
    };

    /**
     * 주어진 방향에 따라 x, y 좌표 오프셋을 반환합니다.
     * 
     * @param dir 이동 방향
     * @return 해당 방향의 오프셋 값
     */
    public int[] getOffset(HexDir dir) {
        // 홀수 행이라면 홀수 방향 오프셋 사용, 짝수 행이라면 짝수 방향 오프셋 사용
        return isOdd ? oddOffsets[dir.ordinal()] : evenOffsets[dir.ordinal()];
    }

    // y 좌표 getter
    public int getY() {
        return y;
    }

    // y 좌표 setter
    public void setY(int y) {
        this.y = y;
    }

    // x 좌표 getter
    public int getX() {
        return x;
    }

    // x 좌표 setter
    public void setX(int x) {
        this.x = x;
    }

    // y 오프셋 getter
    public int getYOffset() {
        return this.yOffset;
    }

    // x 오프셋 getter
    public int getXOffset() {
        return this.xOffset;
    }

    // y 오프셋 setter
    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    // x 오프셋 setter
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    // 홀수 행 여부 getter
    public boolean isOdd() {
        return isOdd;
    }

    // 홀수 행 여부 setter
    public void setOdd(boolean isOdd) {
        this.isOdd = isOdd;
    }

    /**
     * 이 Point 객체를 화면에 그리기 위한 위치로 변환하여 반환합니다.
     * 
     * @return 화면에 그리기 위한 새 Point 객체
     */
    public Point getPosition() {
        // y와 x를 기반으로 그리기 위한 위치 계산
        int drawY = getY() * GameSetting.HEX_HEIGHT * 2 / 3 + yOffset;
        int drawX = getX() * GameSetting.HEX_SIZE + (isOdd() ? GameSetting.HEX_SIZE / 2 : 0) + xOffset;

        // 계산된 위치로 새 Point 객체 반환
        return new Point(drawY, drawX);
    }
}
