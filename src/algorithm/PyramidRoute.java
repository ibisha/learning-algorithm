package algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * https://projecteuler.net/problem=67
 * 
 * 三角形の頂点から下まで移動するとき, その数値の合計の最大値を求める。
 * ファイルを入力として以下のようなピラミッドを考える
 * 
 * 3
 * 7 4
 * 2 4 6
 * 8 5 9 3
 * 
 * 単純な再帰では100段程度でも長大な時間を要するので、メモ化再帰を使う。
 * 2段目に1段目+2段目の数値、3段目に1段目+2段目+3段目を計算して、真ん中の数値は2通りあるうちの大きい数値を保存する。
 * これを最下段まで繰り返す。
 * 
 * 3
 * 10 7
 * 12 14 10
 * 20 19 23 13
 *
 */
public class PyramidRoute {

  private static final String INPUT = "test/p67-triangle.txt";

  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();

    File file = new File(INPUT);
    List<int[]> pyramid = parseFile(file);

    long end = System.currentTimeMillis();
    System.out.println("ファイル読み込みまで" + (end - start) + "ms");

    start = System.currentTimeMillis();
    // 頂点から底辺に向かって探索
    topToBottom(pyramid);
    // System.out.println("----pyramid----");
    // for (int[] row : pyramid) {
    // for (int i : row) {
    // System.out.print(i + " ");
    // }
    // System.out.println();
    // }
    // 底辺から最大値を探す一手間が必要
    int[] bottom = pyramid.get(pyramid.size() - 1);
    Arrays.sort(bottom);
    System.out.println();
    end = System.currentTimeMillis();
    System.out.println("topToBottom : " + bottom[bottom.length - 1] + ", elapsed : " + (end - start) + "ms");

    // 底辺から頂点に向かって探索
    List<int[]> pyramid2 = parseFile(file);
    start = System.currentTimeMillis();
    bottomToTop(pyramid2);
    end = System.currentTimeMillis();
    System.out.println("bottomToTop : " + pyramid2.get(0)[0] + ", elapsed : " + (end - start) + "ms");
    
  }

  private static List<int[]> parseFile(File file) throws IOException {
    List<int[]> pyramid = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file));) {
      String line = null;
      while ((line = br.readLine()) != null) {
        int[] row = Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        pyramid.add(row);
      }
    }

    return pyramid;
  }

  private static void bottomToTop(List<int[]> pyramid) {
    for (int i = pyramid.size() - 1; i > 0; i--) {
      int[] current = pyramid.get(i);
      int[] next = pyramid.get(i - 1);

      for (int j = 0; j < current.length - 1; j++) {
        next[j] = Math.max(next[j] + current[j], next[j] + current[j + 1]);
      }
    }
  }

  private static void topToBottom(List<int[]> pyramid) {
    int tempMemory;
    for (int i = 0; i < pyramid.size() - 1; i++) {
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

}