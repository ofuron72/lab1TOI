package org.example;

import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Точка входа лабораторной работы №1.
 *
 * <p>Консольное меню, позволяющее запустить:</p>
 * <ol>
 *     <li>расширенный алгоритм Евклида ({@link Euclid#extendedGcd});</li>
 *     <li>обратный алгоритм Евклида — поиск {@code a^(-1) mod m} ({@link Euclid#modInverse});</li>
 *     <li>решето Эратосфена ({@link Sieve#primesUpTo}).</li>
 * </ol>
 */
public class Main {

    public Main() {
    }

    /**
     * Запускает консольное меню и обрабатывает выбор пользователя до ввода {@code 0}.
     * Ошибки ввода и вычислений выводятся в консоль, после чего меню показывается снова.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("===== Лабораторная работа №1 =====");
            System.out.println("1. Расширенный алгоритм Евклида");
            System.out.println("2. Обратный алгоритм Евклида (a^-1 mod m)");
            System.out.println("3. Решето Эратосфена");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");

            String choice = in.next();
            try {
                switch (choice) {
                    case "1" -> extended(in);
                    case "2" -> inverse(in);
                    case "3" -> sieve(in);
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Неизвестный пункт меню");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: нужно ввести целое число");
                in.nextLine();
            } catch (ArithmeticException | IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Пункт меню 1: считывает {@code a} и {@code b}, выполняет расширенный алгоритм Евклида
     * с выводом таблицы шагов, печатает НОД, коэффициенты {@code x}, {@code y}
     * и проверку {@code a*x + b*y = НОД}.
     *
     * @param in сканер для чтения ввода пользователя
     */
    private static void extended(Scanner in) {
        System.out.print("a = ");
        long a = in.nextLong();
        System.out.print("b = ");
        long b = in.nextLong();

        Euclid.Result r = Euclid.extendedGcd(a, b, true);
        System.out.printf("%nНОД(%d, %d) = %d%n", a, b, r.gcd());
        System.out.printf("x = %d, y = %d%n", r.x(), r.y());
        System.out.printf("Проверка: %d*(%d) + %d*(%d) = %d%n",
                a, r.x(), b, r.y(), a * r.x() + b * r.y());
    }

    /**
     * Пункт меню 2: считывает {@code a} и {@code m}, находит {@code a^(-1) mod m}
     * обратным алгоритмом Евклида с выводом таблицы шагов и печатает проверку
     * {@code a * a^(-1) mod m = 1}. Проверка считается через {@link BigInteger},
     * чтобы избежать переполнения {@code long} при больших числах.
     *
     * @param in сканер для чтения ввода пользователя
     */
    private static void inverse(Scanner in) {
        System.out.print("a = ");
        long a = in.nextLong();
        System.out.print("m = ");
        long m = in.nextLong();

        long inv = Euclid.modInverse(a, m, true);
        BigInteger check = BigInteger.valueOf(a)
                .multiply(BigInteger.valueOf(inv))
                .mod(BigInteger.valueOf(m));
        System.out.printf("%n%d^(-1) mod %d = %d%n", a, m, inv);
        System.out.printf("Проверка: %d * %d mod %d = %d%n", a, inv, m, check);
    }

    /**
     * Пункт меню 3: считывает {@code n}, находит решетом Эратосфена все простые числа
     * от 2 до {@code n} и печатает их количество и сами числа (по 10 в строке).
     *
     * @param in сканер для чтения ввода пользователя
     */
    private static void sieve(Scanner in) {
        System.out.print("n = ");
        int n = in.nextInt();

        List<Integer> primes = Sieve.primesUpTo(n);
        System.out.printf("Простых чисел от 2 до %d: %d%n", n, primes.size());
        for (int i = 0; i < primes.size(); i++) {
            System.out.printf("%6d", primes.get(i));
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

}

