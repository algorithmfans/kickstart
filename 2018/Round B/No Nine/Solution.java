import java.util.Scanner;

public class Solution {
  static final int DIGIT_NUM = 19;

  static int[][] c;

  public static void main(String[] args) {
    buildC();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      long F = sc.nextLong();
      long L = sc.nextLong();

      System.out.println(String.format("Case #%d: %d", tc, solve(F, L)));
    }

    sc.close();
  }

  static void buildC() {
    c = new int[DIGIT_NUM + 1][DIGIT_NUM + 1];
    for (int n = 0; n < c.length; ++n) {
      for (int r = 0; r <= n; ++r) {
        c[n][r] = C(n, r);
      }
    }
  }

  static long solve(long F, long L) {
    return computeLegalNum(L) - computeLegalNum(F - 1);
  }

  static long computeLegalNum(long limit) {
    int[] limitDigits = new int[DIGIT_NUM];
    for (int i = limitDigits.length - 1; i >= 0; --i) {
      limitDigits[i] = (int) (limit % 10);
      limit /= 10;
    }

    return search(limitDigits, new int[9], 0, DIGIT_NUM);
  }

  static long search(int[] limitDigits, int[] counts, int index, int restDigitNum) {
    if (index == counts.length - 1) {
      counts[index] = restDigitNum;

      return computeLegalNumForDigitSet(limitDigits, counts);
    }

    long result = 0;
    for (int i = 0; i <= restDigitNum; ++i) {
      counts[index] = i;
      result += search(limitDigits, counts, index + 1, restDigitNum - i);
    }

    return result;
  }

  static long computeLegalNumForDigitSet(int[] limitDigits, int[] counts) {
    int digitSum = 0;
    for (int i = 0; i < counts.length; ++i) {
      digitSum += i * counts[i];
    }
    if (digitSum % 9 == 0) {
      return 0;
    }

    return computeLessEqualNum(limitDigits, counts, 0);
  }

  static long computeLessEqualNum(int[] limitDigits, int[] counts, int index) {
    if (index == limitDigits.length) {
      return 1;
    }

    long result = 0;
    for (int digit = 0; digit < limitDigits[index]; ++digit) {
      if (counts[digit] != 0) {
        --counts[digit];

        int restDigitNum = limitDigits.length - index - 1;
        long product = 1;
        for (int i = 0; i < counts.length; ++i) {
          product *= c[restDigitNum][counts[i]];
          restDigitNum -= counts[i];
        }
        result += product;

        ++counts[digit];
      }
    }

    if (limitDigits[index] != 9 && counts[limitDigits[index]] != 0) {
      --counts[limitDigits[index]];
      result += computeLessEqualNum(limitDigits, counts, index + 1);
      ++counts[limitDigits[index]];
    }

    return result;
  }

  static int C(int n, int r) {
    int result = 1;
    for (int i = 0; i < r; ++i) {
      result = result * (n - i) / (i + 1);
    }

    return result;
  }
}
