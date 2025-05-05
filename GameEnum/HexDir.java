package GameEnum;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : HexDir.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 빙판에 인접방향에 대한 이넘
 */

public enum HexDir {
    NW,
    NE,
    E,
    SE,
    SW,
    S,

    END;

    public static HexDir fromInt(int num) {
        HexDir[] states = HexDir.values();
        if (num >= 0 && num < states.length) {
            return states[num];
        }
        throw new IllegalArgumentException("Invalid number: " + num);
    }
}