package org.example;

import java.math.BigInteger;
import java.util.Scanner;

/** Решение системы x ≡ a[i] (mod m[i]) последовательным объединением сравнений. */
public final class ChineseRemainder {
    private ChineseRemainder() {
    }

    /** Все решения имеют вид x = remainder + modulus * k, где k — целое. */
    public record Result(BigInteger remainder, BigInteger modulus) {
    }

    /**
     * Обобщённая китайская теорема об остатках: модули могут быть не взаимно простыми.
     * BigInteger исключает переполнение при вычислении общего модуля.
     * @throws IllegalArgumentException если массивы пусты, размеры различаются или модуль неположителен
     * @throws ArithmeticException если система несовместна
     */
    public static Result solve(BigInteger[] residues, BigInteger[] moduli) {
        if (residues == null || moduli == null || residues.length == 0
                || residues.length != moduli.length) {
            throw new IllegalArgumentException("Нужны непустые массивы одинаковой длины");
        }
        BigInteger x = BigInteger.ZERO;
        BigInteger period = BigInteger.ONE;
        for (int i = 0; i < residues.length; i++) {
            BigInteger m = moduli[i];
            if (residues[i] == null || m == null || m.signum() <= 0) {
                throw new IllegalArgumentException("Остаток должен быть задан, модуль должен быть > 0");
            }
            BigInteger difference = residues[i].mod(m).subtract(x);
            BigInteger gcd = period.gcd(m);
            if (!difference.mod(gcd).equals(BigInteger.ZERO)) {
                throw new ArithmeticException("Система не имеет решений (сравнение №" + (i + 1) + ")");
            }
            // x + period * t ≡ a (mod m); делим сравнение на НОД.
            BigInteger reducedModulus = m.divide(gcd);
            BigInteger t = BigInteger.ZERO;
            if (!reducedModulus.equals(BigInteger.ONE)) {
                t = difference.divide(gcd)
                        .multiply(period.divide(gcd).modInverse(reducedModulus))
                        .mod(reducedModulus);
            }
            x = x.add(period.multiply(t));
            period = period.multiply(reducedModulus); // НОК прежнего периода и m
            x = x.mod(period);
        }
        return new Result(x, period);
    }
    /** Самостоятельный запуск решения системы сравнений. */
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            chineseRemainder(in);
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Ошибка: нужно ввести все данные в виде целых чисел");
        } catch (ArithmeticException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    /** Считывает систему сравнений и печатает все её решения. */
    private static void chineseRemainder(Scanner in) {
        System.out.print("Количество сравнений: ");
        int n = in.nextInt();
        if (n <= 0) {
            throw new IllegalArgumentException("Количество сравнений должно быть > 0");
        }
        BigInteger[] residues = new BigInteger[n];
        BigInteger[] moduli = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            System.out.printf("Сравнение %d: x ≡ a (mod m). Введите a и m: ", i + 1);
            residues[i] = in.nextBigInteger();
            moduli[i] = in.nextBigInteger();
        }
        ChineseRemainder.Result result = ChineseRemainder.solve(residues, moduli);
        System.out.printf("x ≡ %s (mod %s)%n", result.remainder(), result.modulus());
        System.out.printf("Все решения: x = %s + %s * k, k — целое%n",
                result.remainder(), result.modulus());
    }
}

