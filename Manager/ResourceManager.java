package Manager;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : ResourceManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 펭귄 얼음 깨기게임에 사용될 리소스(이미지)를 관리
 */

public class ResourceManager {
    // ResourceManager 클래스의 인스턴스를 하나만 생성하기 위한 싱글톤 패턴 구현
    private static ResourceManager instance;
    
    // 리소스를 저장할 HashMap, 키는 리소스 이름, 값은 해당 리소스의 Image 객체
    private HashMap<String, Image> resourceMap;

    // ResourceManager의 생성자, resourceMap 초기화
    private ResourceManager() {
        resourceMap = new HashMap<>();
    }

    // ResourceManager의 인스턴스를 반환하는 메소드 (싱글톤 구현)
    public static ResourceManager getInst() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // 리소스를 초기화하고 이미지를 로드하는 메소드
    public void init() {
        try {
            // 각 리소스 이미지 파일을 읽어서 HashMap에 추가
            addImage("normalIce", new ImageIcon(getClass().getResource("/Resources/NormalIce.png")).getImage());
            addImage("crackedIce", new ImageIcon(getClass().getResource("/Resources/CrackedIce.png")).getImage());
            addImage("brokenIce", new ImageIcon(getClass().getResource("/Resources/BrokenIce.png")).getImage());
            addImage("redPenguin", new ImageIcon(getClass().getResource("/Resources/RedPenguin.png")).getImage());
            addImage("greenPenguin", new ImageIcon(getClass().getResource("/Resources/GreenPenguin.png")).getImage());
            addImage("bluePenguin", new ImageIcon(getClass().getResource("/Resources/BluePenguin.png")).getImage());
            addImage("yellowPenguin", new ImageIcon(getClass().getResource("/Resources/YellowPenguin.png")).getImage());
            addImage("purplePenguin", new ImageIcon(getClass().getResource("/Resources/PurplePenguin.png")).getImage());
            addImage("hammer", new ImageIcon(getClass().getResource("/Resources/Hammer.png")).getImage());
            addImage("fallingBluePenguin", new ImageIcon(getClass().getResource("/Resources/fallingBluePenguin.png")).getImage());
            addImage("fallingRedPenguin", new ImageIcon(getClass().getResource("/Resources/fallingRedPenguin.png")).getImage());
            addImage("fallingYellowPenguin", new ImageIcon(getClass().getResource("/Resources/fallingYellowPenguin.png")).getImage());
            addImage("fallingPurplePenguin", new ImageIcon(getClass().getResource("/Resources/fallingPurplePenguin.png")).getImage());
            addImage("fallingGreenPenguin", new ImageIcon(getClass().getResource("/Resources/fallingGreenPenguin.png")).getImage());
        } catch (Exception e) {
            e.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력
        }
    }

    // 이미지 리소스를 resourceMap에 추가하는 메소드
    public void addImage(String imageName, Image image) {
        resourceMap.put(imageName, image); // 지정된 이름으로 이미지를 저장
    }

    // 이름으로 이미지 리소스를 반환하는 메소드
    public Image getImage(String imageName) {
        return resourceMap.get(imageName); // 지정된 이름에 해당하는 이미지를 반환
    }
}
