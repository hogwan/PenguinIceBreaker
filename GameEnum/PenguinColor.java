package GameEnum;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : PenguinColor.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 펭귄 색깔에 대한 이넘
 */

public enum PenguinColor {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    PURPLE,
    END;

    public static PenguinColor fromInt(int num) {
		PenguinColor[] states = PenguinColor.values();
		if (num >= 0 && num < states.length) {
			return states[num];
		}
		throw new IllegalArgumentException("Invalid number: " + num);
	}
}
