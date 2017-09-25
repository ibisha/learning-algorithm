package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * https://projecteuler.net/problem=67
 * をピラミッドではなく格子で考える
 * 
 * 格子状の経路を左上から右下に進むとき経路上の数値の和の最大値を考える
 * 
 * 3 5 0
 * 7 4 1
 * 2 4 6
 * => 24
 * 
 * ピラミッド型の応用で、ひし形にして考える
 * 3
 * 7 5
 * 2 4 0
 * 4 1
 * 6
 *
 */
public class SquareRoute {

  public static void main(String[] args) throws Exception {
    List<int[]> rows = new ArrayList<>();
    try (Scanner in = new Scanner(System.in);) {
      while (true) {
        String input = in.nextLine();
        if (input == null || input.equals(""))
          break;
        int[] cols = Stream.of(input.split("")).mapToInt(Integer::parseInt).toArray();
        rows.add(cols);
      }
    }
    // Random r = new Random();
    // for (int k = 0; k < side; k++) {
    // int[] row = new int[side];
    // for (int i = 0; i < side; i++) {
    // row[i] = r.nextInt(10);
    // }
    // rows.add(row);
    // }

    long start = System.currentTimeMillis();

    // 1000*1000にすると1分弱かかる
    final int size = rows.size();
    System.out.println(size);

    // ピラミッド型に変換
    List<int[]> diamond = new ArrayList<>();
    int[] row = null;
    for (int i = 0; i < size; i++) {
      row = new int[i + 1];
      for (int j = 0; j <= i; j++) {
        row[j] = rows.get(i - j)[j];
      }
      diamond.add(row);
    }
    for (int i = size - 1; i > 0; i--) {
      row = new int[i];
      for (int j = 0; j < i; j++) {
        row[j] = rows.get(size - 1 - j)[(size - i) + j];
      }
      diamond.add(row);
    }

    long now = System.currentTimeMillis();
    System.out.println("経路作成まで" + (now - start) + "ms");

    // 行列確認
    System.out.println("----before----");
    for (int[] r : diamond) {
      for (int n : r) {
        System.out.print(n + " ");
      }
      System.out.println();
    }

    // 探索と結果の出力
    search(diamond, size);
    // 探索後確認
    System.out.println("----after----");
    for (int[] r : diamond) {
      for (int n : r) {
        System.out.print(n + " ");
      }
      System.out.println();
    }

    System.out.println("----結果----");
    System.out.println(diamond.get(diamond.size() - 1)[0]);

    long end = System.currentTimeMillis();
    System.out.println("elapsed = " + (end - start) + "ms");
  }

  /**
   * ひし形の上半分と下半分で処理を分ける.
   * 頂点から対角上の頂点までの経路の最大値を探索する。
   * 結果は、引数のListの末尾の値にセットされる。
   * 
   * @param route 探索したい経路
   * @param size 正方形の辺の長さ
   */
  private static void search(List<int[]> route, int size) {
    topToBottom(route, size);
    bottomToTop(route, size);
  }

  /**
   * 頂点から底辺に向かって探索する。
   * 底辺から最大値を探す手間が必要。
   * 
   * 破壊的な実装なので注意
   * 
   * 2段目に1段目+2段目の数値、3段目に1段目+2段目+3段目を計算して、真ん中の数値は2通りあるうちの大きい数値を保存する。
   * これを最下段まで繰り返す。
   * 
   * 3
   * 10 7
   * 12 14 10
   * 20 19 23 13
   * 
   * @param pyramid
   */
  private static void topToBottom(List<int[]> pyramid, int end) {
    int tempMemory;
    for (int i = 0; i < end - 1; i++) {
      tempMemory = 0;
      int[] current = pyramid.get(i);
      int[] next = pyramid.get(i + 1);
      next[0] = current[0] + next[0];
      for (int j = 0; j < current.length; j++) {
        next[j] = Math.max(next[j], current[j] + tempMemory);

        tempMemory = next[j + 1];
        next[j + 1] = current[j] + next[j + 1];
      }
    }
  }

  /**
   * 底辺から頂点に向かって探索する。
   * 頂点の値が、最大値となる。
   * 
   * 破壊的な実装なので注意
   * 
   * @param pyramid
   */
  private static void bottomToTop(List<int[]> pyramid, int start) {
    for (int i = start - 1; i < pyramid.size() - 1; i++) {
      int[] current = pyramid.get(i);
      int[] next = pyramid.get(i + 1);

      for (int j = 0; j < current.length - 1; j++) {
        next[j] = Math.max(next[j] + current[j], next[j] + current[j + 1]);
      }
    }
  }

}