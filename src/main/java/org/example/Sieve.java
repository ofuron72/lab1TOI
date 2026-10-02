package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Решето Эратосфена — поиск всех простых чисел, не превосходящих заданное {@code n}.
 *
 * <p>Класс-утилита: содержит только статические методы, экземпляры не создаются.</p>
 */
public final class Sieve {

    /**
     * Закрытый конструктор — запрещает создание экземпляров утилитного класса.
     */
    private Sieve() {
    }

    /**
     * Возвращает все простые числа в диапазоне {@code [2, n]}.
     *
     * <p>Алгоритм: заводится массив флагов «составное» для чисел {@code 0..n}.
     * Для каждого ещё не вычеркнутого {@code p} (оно простое) вычёркиваются
     * кратные ему числа, начиная с {@code p * p} — меньшие кратные уже вычеркнуты
     * меньшими простыми. Перебор {@code p} останавливается, когда {@code p * p > n}.
     * Невычеркнутые числа — простые.</p>
     *
     * <p>Сложность: время {@code O(n log log n)}, память {@code O(n)}.</p>
     *
     * @param n верхняя граница поиска (включительно)
     * @return список простых чисел по возрастанию; пустой список, если {@code n < 2}
     */
    public static List<Integer> primesUpTo(int n) {
        List<Integer> primes = new ArrayList<>();
        if (n < 2) {
            return primes;
        }

        boolean[] composite = new boolean[n + 1];
        for (int p = 2; (long) p * p <= n; p++) {
            if (!composite[p]) {
                for (int k = p * p; k <= n; k += p) {
                    composite[k] = true;
                }
            }
        }

        for (int i = 2; i <= n; i++) {
            if (!composite[i]) {
                primes.add(i);
            }
        }
        return primes;
    }
}
