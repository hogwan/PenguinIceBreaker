package Manager;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : GameSetting.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 게임에 들어갈 상수값
 */

// 게임 세팅
public class GameSetting {
    public static final int MAP_SIZE_Y = 9;
    public static final int MAP_SIZE_X = 9;
    public static final int IMPACT_LIMIT = 4;
    public static final int HAMMER_DAMAGE = 10;
    public static final int HAMMER_DAMAGEDIV = 2;
    public static final int HEX_SIZE = 60;
    public static final int HEX_HEIGHT = (int) (Math.sqrt(3.0) * HEX_SIZE);
    public static final int FPS = 30;
    public static final double DELTATIME = 1.0/FPS;
    public static final double IMPACTDELAY = 0.2;
    public static final double IMPACTAMPLITUDE = 30.0;
    public static final int IMPACTSPEED = 900;
    public static final int PENGUINYOFFSET = -20;
    public static final double HAMMERDELAY = 1.4;
}
