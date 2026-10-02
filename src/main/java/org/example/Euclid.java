package org.example;

/**
 * Алгоритмы Евклида: расширенный алгоритм и обратный алгоритм
 * (нахождение обратного элемента по модулю).
 *
 * <p>Класс-утилита: содержит только статические методы, экземпляры не создаются.</p>
 */
public final class Euclid {

    /**
     * Закрытый конструктор — запрещает создание экземпляров утилитного класса.
     */
    private Euclid() {
    }

    /**
     * Результат расширенного алгоритма Евклида.
     * Для входных чисел {@code a} и {@code b} выполняется равенство
     * {@code a * x + b * y = gcd} (соотношение Безу).
     *
     * @param gcd наибольший общий делитель чисел {@code a} и {@code b} (всегда &ge; 0)
     * @param x   коэффициент при {@code a}
     * @param y   коэффициент при {@code b}
     */
    public record Result(long gcd, long x, long y) {
    }

    /**
     * Расширенный алгоритм Евклида (итеративная версия).
     *
     * <p>Находит {@code d = НОД(a, b)} и целые коэффициенты {@code x}, {@code y}, такие что
     * {@code a * x + b * y = d}. На каждом шаге вычисляются частное {@code q = r[i-1] / r[i]}
     * и новые значения по формулам:</p>
     * <pre>
     * r[i+1] = r[i-1] - q * r[i]
     * x[i+1] = x[i-1] - q * x[i]
     * y[i+1] = y[i-1] - q * y[i]
     * </pre>
     * <p>Алгоритм останавливается, когда остаток становится равен нулю;
     * последний ненулевой остаток и есть НОД.</p>
     *
     * @param a       первое число
     * @param b       второе число
     * @param verbose {@code true} — печатать в консоль таблицу шагов (q, r, x, y)
     * @return тройка {@link Result}: НОД и коэффициенты {@code x}, {@code y}
     */
    public static Result extendedGcd(long a, long b, boolean verbose) {
        long r0 = a, r1 = b;
        long x0 = 1, x1 = 0;
        long y0 = 0, y1 = 1;

        if (verbose) {
            System.out.printf("%-6s %-12s %-12s %-12s %-12s%n", "шаг", "q", "r", "x", "y");
            System.out.printf("%-6s %-12s %-12d %-12d %-12d%n", "0", "-", r0, x0, y0);
            System.out.printf("%-6s %-12s %-12d %-12d %-12d%n", "1", "-", r1, x1, y1);
        }

        int step = 2;
        while (r1 != 0) {
            long q = r0 / r1;

            long r2 = r0 - q * r1;
            long x2 = x0 - q * x1;
            long y2 = y0 - q * y1;

            r0 = r1; r1 = r2;
            x0 = x1; x1 = x2;
            y0 = y1; y1 = y2;

            if (verbose) {
                System.out.printf("%-6d %-12d %-12d %-12d %-12d%n", step++, q, r1, x1, y1);
            }
        }

        // НОД должен быть неотрицательным (при отрицательных входных данных меняем знаки)
        if (r0 < 0) {
            r0 = -r0; x0 = -x0; y0 = -y0;
        }
        return new Result(r0, x0, y0);
    }

    /**
     * Обратный алгоритм Евклида: нахождение обратного элемента {@code a^(-1) mod m},
     * т.е. такого числа {@code x} из диапазона {@code [0, m)}, что {@code a * x ≡ 1 (mod m)}.
     *
     * <p>Использует расширенный алгоритм для пары {@code (m, a)}: если
     * {@code m * x + a * y = 1}, то по модулю {@code m} слагаемое {@code m * x} обнуляется
     * и остаётся {@code a * y ≡ 1 (mod m)}, значит {@code y} — искомый обратный элемент
     * (приводится к диапазону {@code [0, m)}).</p>
     *
     * <p>Обратный элемент существует только при {@code НОД(a, m) = 1}.</p>
     *
     * @param a       число, для которого ищется обратный элемент (может быть отрицательным)
     * @param m       модуль, должен быть больше 1
     * @param verbose {@code true} — печатать в консоль таблицу шагов
     * @return обратный элемент в диапазоне {@code [0, m)}
     * @throws IllegalArgumentException если {@code m <= 1}
     * @throws ArithmeticException      если {@code НОД(a, m) != 1} и обратного элемента не существует
     */
    public static long modInverse(long a, long m, boolean verbose) {
        if (m <= 1) {
            throw new IllegalArgumentException("Модуль должен быть > 1");
        }
        long aNorm = Math.floorMod(a, m);
        Result r = extendedGcd(m, aNorm, verbose);
        if (r.gcd() != 1) {
            throw new ArithmeticException(
                    "Обратного элемента нет: НОД(" + a + ", " + m + ") = " + r.gcd() + " ≠ 1");
        }
        return Math.floorMod(r.y(), m);
    }
}
