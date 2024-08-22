import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int answer = solution.solution(new int[]{-2, 3, 0, 2, -5});
        System.out.println("최종 답: " + answer);
    }
}

class Solution {
    public int solution(int[] number) {
        return findThreeSum(number, 0, 0, 0, new ArrayList<Integer>());
    }

    // 백트래킹을 위한 재귀 함수
    private int findThreeSum(int[] number, int index, int count, int sum, List<Integer> currentSelection) {
        // 현재 상태 로그 출력
        System.out.println("findThreeSum 호출: index=" + index + ", count=" + count + ", sum=" + sum + ", 선택된 숫자들=" + currentSelection);

        // 기저 조건: 세 숫자를 선택했고, 그 합이 0일 때
        if (count == 3) {
            System.out.println("기저 조건 충족 (count=3): sum=" + sum + " -> 반환값=" + (sum == 0 ? 1 : 0) + ", 최종 선택된 숫자들=" + currentSelection);
            return sum == 0 ? 1 : 0;
        }

        // 기저 조건: 더 이상 선택할 숫자가 없을 때
        if (index >= number.length) {
            System.out.println("기저 조건 충족 (index 범위 초과): 0 반환");
            return 0;
        }

        // 현재 index의 숫자를 선택하는 경우
        currentSelection.add(number[index]);
        System.out.println("숫자 포함 number[" + index + "] = " + number[index] + ", 현재 선택된 숫자들=" + currentSelection);
        int include = findThreeSum(number, index + 1, count + 1, sum + number[index], currentSelection);
        currentSelection.remove(currentSelection.size() - 1); // 재귀에서 돌아올 때 숫자 제거

        // 현재 index의 숫자를 선택하지 않는 경우
        System.out.println("숫자 제외 number[" + index + "] = " + number[index] + ", 현재 선택된 숫자들=" + currentSelection);
        int exclude = findThreeSum(number, index + 1, count, sum, currentSelection);

        // 두 경우에서 나온 결과를 합산
        int result = include + exclude;
        System.out.println("findThreeSum 반환: index=" + index + ", count=" + count + ", sum=" + sum + " -> 결과=" + result + ", 현재 선택된 숫자들=" + currentSelection);
        return result;
    }
}
