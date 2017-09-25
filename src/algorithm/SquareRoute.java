package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    List<int[]> rows = readFromSysin();
    // List<int[]> rows = readFromFile("格子.txt");
    // List<int[]> rows = createRandomRoute(1000);

    long start = System.currentTimeMillis();
    final int size = rows.size();
    
    // ひし形に変換
    List<int[]> diamond = convertToDiamond(rows);
    for (int[] r: diamond) {
      for (int n : r) {
        System.out.print(n + " ");
      }
      System.out.println();
    }

    // 探索と結果の出力
    search(diamond, size);
    for (int[] r: diamond) {
      for (int n : r) {
        System.out.print(n + " ");
      }
      System.out.println();
    }

    System.out.println(diamond.get(diamond.size() - 1)[0]);

    long end = System.currentTimeMillis();
    // System.out.println("elapsed = " + (end - start) + "ms");
  }

  /**
   * 標準入力から経路を作成する
   * 
   * @return 経路
   */
  private static List<int[]> readFromSysin() {
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
    return rows;
  }

  /**
   * 経路を書いたファイルを読み込む
   * 
   * @param name
   *          ファイル名
   * @return 経路
   * @throws IOException
   */
  private static List<int[]> readFromFile(String name) throws IOException {
    List<int[]> rows = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(name))) {
      String line = br.readLine();
      while (line != null) {
        int[] cols = Stream.of(line.split("")).mapToInt(Integer::parseInt).toArray();
        rows.add(cols);
        line = br.readLine();
      }
    }
    return rows;
  }

  /**
   * ランダムに経路を作成する
   * 
   * @param size
   *          経路の1辺の大きさ
   * @return 経路
   */
  private static List<int[]> createRandomRoute(int size) {
    List<int[]> rows = new ArrayList<>();
    Random r = new Random();
    for (int k = 0; k < size; k++) {
      int[] row = new int[size];
      for (int i = 0; i < size; i++) {
        row[i] = r.nextInt(10);
      }
      rows.add(row);
    }
    return rows;

  }

  /**
   * 正方形をひし形に変換する
   * 
   * @param rows
   * @return ひし形
   */
  private static List<int[]> convertToDiamond(List<int[]> rows) {
    List<int[]> diamond = new ArrayList<>();
    int size = rows.size();
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

    return diamond;
  }

  /**
   * ひし形の上半分と下半分で処理を分ける.
   * 頂点から対角上の頂点までの経路の最大値を探索する。
   * 結果は、引数のListの末尾の値にセットされる。
   * 
   * @param route
   *          探索したい経路
   * @param size
   *          正方形の辺の長さ
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
    for (int i = 0; i < end - 1; i++) {
      int[] current = pyramid.get(i);
      int[] next = pyramid.get(i + 1);
      next[0] = current[0] + next[0];
      next[next.length - 1] = current[current.length -1] + next[next.length - 1];
      for (int j = 1; j < current.length; j++) {
        next[j] = Math.min(next[j] + current[j - 1], next[j] + current[j]);
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
        next[j] = Math.min(next[j] + current[j], next[j] + current[j + 1]);
      }
    }
  }

}