package Interface;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import GameObject.Point;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : Drawable.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 화면에 그려질 오브젝트가 가져야 할 인터페이스
 */

//draw메서드를 가진 인터페이스
public interface Drawable {
	public void draw(Graphics g, Point point, ImageObserver observer);
}
