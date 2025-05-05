package Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

import GameObject.*;
import GameEnum.*;
import Swing.GamePanel;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : IceManager.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 모든 빙판들을 관리함
 */

/**
 * IceManager 클래스는 게임의 얼음 블록을 관리하고,
 * 충격파 전파 및 펭귄 배치 등을 처리합니다.
 */
public class IceManager {

    private static IceManager instance;
    private ArrayList<ArrayList<Ice>> iceMap = new ArrayList<>(); // 얼음 맵을 저장하는 2D 리스트
    private Random random = new Random(); // 난수 생성기

    // IceManager의 인스턴스를 생성하는 싱글톤 패턴
    private IceManager() {
        init(GameSetting.MAP_SIZE_Y, GameSetting.MAP_SIZE_X); // 맵 크기에 맞춰 초기화
    }

    public static IceManager getInst() {
        if (instance == null) {
            instance = new IceManager();
        }
        return instance;
    }

    /**
     * 맵 크기에 맞게 얼음 블록을 초기화합니다.
     * 
     * @param y 맵의 세로 크기
     * @param x 맵의 가로 크기
     */
    private void init(int y, int x) {
        for (int i = 0; i < y; i++) {
            boolean isOdd = (i % 2 == 1); // 홀수 행 여부 확인
            ArrayList<Ice> iceRow = new ArrayList<>();
            for (int j = 0; j < x; j++) {
                Point pos = new Point(i, j, isOdd); // 위치를 나타내는 Point 객체 생성
                iceRow.add(new Ice(pos)); // Ice 객체 생성 후 추가
            }
            iceMap.add(iceRow); // 행 추가
        }
    }

    /**
     * 얼음 맵을 초기 상태로 리셋합니다.
     */
    public void clearIceMap() {
        for (int i = 0; i < iceMap.size(); i++) {
            for (int j = 0; j < iceMap.get(0).size(); j++) {
                Ice ice = getIce(i, j);
                iceMap.get(i).set(j, constructIce(ice.getPoint(), 0)); // 얼음 상태 초기화
                iceMap.get(i).get(j).setCurState(IceState.NONE); // 상태를 NONE으로 설정
            }
        }
    }

    /**
     * 맵에 랜덤한 얼음 상태를 생성합니다.
     */
    public void generateSquare() {
        for (int i = 0; i < iceMap.size(); i++) {
            for (int j = 0; j < iceMap.get(0).size(); j++) {
                Ice ice = getIce(i, j);
                iceMap.get(i).set(j, constructIce(ice.getPoint(), random.nextInt(1, 11))); // 랜덤한 HP 값 설정
                GameManager.getInst().addObject(getIce(i, j)); // GameManager에 얼음 객체 추가
            }
        }
    }

    /**
     * 충격파를 발생시켜 얼음에 영향을 미칩니다.
     * 
     * @param pos 충격파가 시작되는 위치
     * @param onSpreadFinished 충격파 전파 후 실행될 함수
     */
    public void impact(Point pos, Runnable onSpreadFinished) {
        int[][] visited = new int[GameSetting.MAP_SIZE_Y + 2][GameSetting.MAP_SIZE_X + 2]; // 방문 여부를 체크하기 위한 배열
        visited[pos.getY()][pos.getX()] = 1;
        
        getIce(pos.getY(), pos.getX()).getHit(GameSetting.HAMMER_DAMAGE); // 첫 충격 처리
        int nextDamage = GameSetting.HAMMER_DAMAGE / GameSetting.HAMMER_DAMAGEDIV; // 다음 충격의 데미지
        
        List<Point> firstList = new ArrayList<>();
        firstList.add(pos); // 첫 번째 위치를 리스트에 추가
        
        // 일정 시간 후 충격파 전파 시작
        new javax.swing.Timer((int)(GameSetting.IMPACTDELAY * 1000), e -> {
            ((Timer) e.getSource()).stop();
            spreadImpactBFS(firstList, nextDamage, visited, 1, onSpreadFinished); // 충격파 전파 시작
        }).start();
    }

    /**
     * BFS 알고리즘을 사용하여 충격파를 전파합니다.
     * 
     * @param startPoints 충격이 시작되는 지점들
     * @param damage 충격의 데미지
     * @param visited 방문 여부 배열
     * @param step 전파 단계
     * @param onSpreadFinished 전파 완료 후 실행될 함수
     */
    private void spreadImpactBFS(List<Point> startPoints, int damage, int[][] visited, int step, Runnable onSpreadFinished) {
        if (damage <= 0 || startPoints.isEmpty()) {
            if (onSpreadFinished != null) {
                onSpreadFinished.run(); // 전파 완료 후 호출
            }
            return;
        }
        
        List<Point> nextPoints = new ArrayList<>();
        
        for (Point p : startPoints) {
            int y = p.getY(), x = p.getX();
            
            if (visited[y][x] + 1 >= GameSetting.IMPACT_LIMIT)
                continue; // 전파 제한에 도달하면 종료
            
            // 6방향으로 충격을 전파
            for (int i = 0; i < HexDir.END.ordinal(); i++) {
                int[] offset = p.getOffset(HexDir.fromInt(i));
                int ny = y + offset[0];
                int nx = x + offset[1];
                boolean isOdd = offset[2] == 1; // 홀수 여부
                
                Point np = new Point(ny, nx, isOdd);
                
                if (!validAreaCheck(np)) // 유효한 영역인지 확인
                    continue;
                if (visited[ny][nx] != 0) // 이미 방문한 곳은 건너뛰기
                    continue;
                
                getIce(ny, nx).getHit(damage); // 얼음에 데미지 적용
                visited[ny][nx] = visited[y][x] + 1;
                nextPoints.add(np); // 다음 전파 지점 추가
            }
        }
        
        int nextDamage = damage / GameSetting.HAMMER_DAMAGEDIV; // 다음 데미지
        // 일정 시간 후 다시 전파
        new javax.swing.Timer((int)(GameSetting.IMPACTDELAY * 1000), e -> {
            ((Timer) e.getSource()).stop();
            spreadImpactBFS(nextPoints, nextDamage, visited, step + 1, onSpreadFinished);
        }).start();
    }

    /**
     * 주어진 좌표가 게임 맵 내에서 유효한지 체크합니다.
     * 
     * @param pos 체크할 좌표
     * @return 유효한 좌표이면 true, 아니면 false
     */
    public boolean validAreaCheck(Point pos) {
        if (pos.getY() < 0 || pos.getX() < 0 || pos.getY() >= GameSetting.MAP_SIZE_Y
                || pos.getX() >= GameSetting.MAP_SIZE_X) {
            return false; // 맵 범위 밖이면 false
        }

        Ice ice = iceMap.get(pos.getY()).get(pos.getX());
        if (ice.getCurState() == IceState.NONE || ice.getCurState() == IceState.BROKEN) {
            return false; // 얼음이 없거나 이미 깨졌으면 false
        }

        return true;
    }

    /**
     * 유효하지 않은 얼음들을 깨뜨립니다.
     */
    public void irrelevantIceBreak() {
        ArrayList<Penguin> list = GameManager.getInst().getAlivePenguins();
        int[][] visited = new int[GameSetting.MAP_SIZE_Y + 2][GameSetting.MAP_SIZE_X + 2]; // 방문 여부 배열

        // 펭귄마다 해당 지역의 유효한 얼음 구역을 찾아 DFS를 통해 전파
        for (Penguin penguin : list) {
            if (visited[penguin.getPoint().getY()][penguin.getPoint().getX()] == 0) {
                relevantIceDFS(penguin.getPoint(), visited); // DFS로 전파
            }
        }

        // 방문하지 않은 곳은 강제로 깨뜨리기
        for (int i = 0; i < iceMap.size(); i++) {
            for (int j = 0; j < iceMap.get(i).size(); j++) {
                if (visited[i][j] == 0) {
                    getIce(i, j).getHit(999999); // 강제 충격
                }
            }
        }
    }

    /**
     * DFS를 사용하여 유효한 얼음 구역을 찾아 전파합니다.
     * 
     * @param point 현재 검사할 점
     * @param visited 방문 여부 배열
     */
    public void relevantIceDFS(Point point, int[][] visited) {
        visited[point.getY()][point.getX()] = 1; // 방문 처리
        int y = point.getY();
        int x = point.getX();
        for (int i = 0; i < HexDir.END.ordinal(); i++) {
            int[] offset = point.getOffset(HexDir.fromInt(i));
            int ny = y + offset[0];
            int nx = x + offset[1];
            boolean isOdd = offset[2] == 1; // 홀수 여부
            Point np = new Point(ny, nx, isOdd);

            if (!validAreaCheck(np)) // 유효하지 않으면 건너뛰기
                continue;
            if (visited[ny][nx] != 0) // 이미 방문한 곳은 건너뛰기
                continue;

            relevantIceDFS(np, visited); // DFS 전파
        }
    }

    /**
     * 펭귄을 유효한 얼음 블록에 배치합니다.
     * 
     * @param penguin 배치할 펭귄
     */
    public void placePenguin(Penguin penguin) {
        ArrayList<Ice> placableIces = searchValidPenguinPosition(); // 배치 가능한 얼음 찾기
        int rand = random.nextInt(0, placableIces.size()); // 랜덤으로 선택
        placableIces.get(rand).putOnPenguin(penguin); // 얼음에 펭귄 배치

        int y = placableIces.get(rand).getPoint().getY();
        int x = placableIces.get(rand).getPoint().getX();

        System.out.println(penguin.getName() + "펭귄이 " + y + ", " + x + "에 배치됨");
    }

    /**
     * 펭귄을 배치 가능한 얼음 블록을 찾아 반환합니다.
     * 
     * @return 유효한 얼음 블록 목록
     */
    public ArrayList<Ice> searchValidPenguinPosition() {
        ArrayList<Ice> validIces = new ArrayList<>();
        for (ArrayList<Ice> iceRow : iceMap) {
            for (Ice ice : iceRow) {
                if (validAreaCheck(ice.getPoint()) && ice.getPenguin() == null) {
                    validIces.add(ice);
                }
            }
        }
        return validIces;
    }

	/**
     * 해머를 배치 가능한 얼음 블록을 찾아 반환합니다.
     * 
     * @return 유효한 얼음 블록 목록
     */
    public ArrayList<Ice> searchValidHammerPosition() {
        ArrayList<Ice> validIces = new ArrayList<>();
        for (ArrayList<Ice> iceRow : iceMap) {
            for (Ice ice : iceRow) {
                if (validAreaCheck(ice.getPoint())) {
                    validIces.add(ice);
                }
            }
        }
        return validIces;
    }

    /**
     * 모든 얼음 맵을 출력할 수 있도록 문자열로 반환합니다.
     * 
     * @return 얼음 맵 문자열
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Ice> iceRow : iceMap) {
            for (Ice ice : iceRow) {
                sb.append(ice.getHp()).append(" "); // 각 얼음의 HP를 문자열로 추가
            }
            sb.append("\n");
        }
        return sb.toString(); // 전체 얼음 맵 반환
    }

    // iceMap에서 (y, x) 위치의 Ice 객체를 가져옵니다.
    private Ice getIce(int y, int x) {
        return iceMap.get(y).get(x);
    }

    // 새로운 Ice 객체를 생성합니다.
    private Ice constructIce(Point point, int hp) {
        return new Ice(point, hp);
    }
}
