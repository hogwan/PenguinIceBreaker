package GameEnum;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : ObjectType.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 오브젝트 타입에 대한 이넘
 */

public enum ObjectType {
	ICE,
	PENGUIN,
	HAMMER,

	END,

	NONE;

	public static ObjectType fromInt(int num) {
		ObjectType[] states = ObjectType.values();
		if (num >= 0 && num < states.length) {
			return states[num];
		}
		throw new IllegalArgumentException("Invalid number: " + num);
	}
}