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
    List<int[]> memo = new ArrayList<>();
    // 1段目の数値を揃える
    memo.add(pyramid.get(0));

    long now = System.currentTimeMillis();
    System.out.println("ファイル読み込みまで" + (now - start) + "ms");

    // 探索
    search(pyramid);
    // メモ確認
    System.out.println("----pyramid----");
    for (int[] row : pyramid) {
      for (int i : row) {
        System.out.print(i + " ");
      }
      System.out.println();
    }

    System.out.println("----結果----");
    int[] bottom = pyramid.get(pyramid.size() - 1);
    Arrays.sort(bottom);
    System.out.println(bottom[bottom.length - 1]);

    long end = System.currentTimeMillis();
    System.out.println("elapsed = " + (end - start) + "ms");
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

  private static void search(List<int[]> pyramid) {
    int tempMemory = 0;
    for (int i = 0; i < pyramid.size() - 1; i++) {
      int[] current = pyramid.get(i);
      int[] next = pyramid.get(i + 1);
      for (int j = 0; j < current.length; j++) {
        if (next[j] < current[j] + tempMemory) {
          next[j] = current[j] + tempMemory; 
        }
        tempMemory = next[j + 1];
        next[j + 1] = current[j] + next[j + 1];
      }
    }
  }

}