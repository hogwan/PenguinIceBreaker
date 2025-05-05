package GameObject;

import GameEnum.ObjectType;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : GameObject.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 펭귄얼음깨기 오브젝트 클래스
 */

/**
 * 게임 오브젝트의 기본 클래스입니다.
 * 모든 게임 내 오브젝트는 이 클래스를 상속받아 좌표(Point), 타입(ObjectType), 제거 여부 등을 관리합니다.
 */
public class GameObject implements Comparable<GameObject> {

    // 오브젝트의 타입 (예: PENGUIN, ICE, NONE 등)
    private ObjectType type = ObjectType.NONE;

    // 오브젝트의 위치
    private Point point;

    // 제거 상태를 나타내는 플래그 (true면 제거됨)
    private boolean isRemoved = false;

    // 기본 생성자
    public GameObject() {
    }

    // 위치를 설정하는 생성자
    public GameObject(Point point) {
        this.point = point;
    }

    // 현재 위치를 반환
    public Point getPoint() {
        return point;
    }

    // 위치를 설정
    public void setPoint(Point point) {
        this.point = point;
    }

    // 제거 여부 반환
    public boolean isRemoved() {
        return isRemoved;
    }

    // 제거 여부 설정
    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    // 오브젝트 타입 설정
    public void setType(ObjectType type) {
        this.type = type;
    }

    // 오브젝트 타입 반환
    public ObjectType getType() {
        return type;
    }

    /**
     * GameObject끼리의 비교 기준 정의
     * Y 좌표를 기준으로 오름차순 정렬
     */
    @Override
    public int compareTo(GameObject o) {
        return Integer.compare(this.getPoint().getY(), o.getPoint().getY());
    }
}
