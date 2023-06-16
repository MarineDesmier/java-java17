package java8.ex06;

import org.junit.Test;

import java.util.logging.Logger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Exercice 07 - Stream Parallel - Effet de bord
 */
public class Stream_06_Test {

	// Soit une implémentation impérative de la somme
	private long imperativeSum(long n) {
		long result = 0;

		for (long i = 1L; i < n; i++) {
			result += i;
		}
		return result;
	}

	// Soit une structure permettant de stocker le total
	private class Accumulator {
		private long total;

		private void add(long value) {
			total += value;
		}
	}

	// TODO compléter la méthode pour que le calcul de la somme soit fait avec une
	// instance d'Accumulator
	private long sumWithAccumulator(long n) {
		// TODO créer une instance de l'accumulateur (classe Accumulator)
		Accumulator acc = new Accumulator();
		LongStream.rangeClosed(1, n - 1)
				// TODO pour chaque élément de longStream, invoquer la méthode add de
				// l'accumulateur (acc)
				.forEach(acc::add);
		return acc.total;
	}

	// TODO exécuter le test pour valider l'implémentation de sumWithAccumulator
	@Test
	public void test_sumWithAccumulator() throws Exception {
		Stream.of(1L, 1000L, 10000L).forEach(n -> {
			long result1 = imperativeSum(n);
			long result2 = sumWithAccumulator(n);

			assertThat(result1, is(result2));
		});
	}

	// TODO reprendre le code de sumWithAccumulator et rendre le traitement
	// parallèle (.parallel())
	private long sumWithAccumulatorParallel(long n) {
		Accumulator acc = new Accumulator();
		LongStream.rangeClosed(1, n - 1).parallel().forEachOrdered(acc::add);
		return acc.total;
	}

	// TODO Exécuter le test
	// Que constatez-vous ? Le test test_sumWithAccumulatorParallel vérifie que les
	// résultats de sumWithAccumulatorParallel sont identiques à ceux de
	// imperativeSum et sumWithAccumulator pour différentes valeurs de n.
	@Test
	public void test_sumWithAccumulatorParallel() throws Exception {

		Stream.of(1L, 2L, 3L, 10L, 20L, 50L, 1000L, 100000L).forEach(n -> {
			long result1 = imperativeSum(n);
			long result2 = sumWithAccumulator(n);
			long result3 = sumWithAccumulatorParallel(n);

			assertThat("n=" + n, result1, is(result2));
			assertThat("n=" + n, result1, is(result3));

			Logger.getGlobal().info("Test ok avec n=" + n);
		});
	}
	// forEachOrdered est une méthode de la classe Stream qui exécute une action sur
	// chaque élément du flux, en garantissant que les éléments sont traités dans
	// l'ordre d'origine du flux. Cela signifie que l'ordre des éléments est
	// préservé lors de l'exécution parallèle.

	//	Lorsque vous utilisez forEach sur un flux parallèle, les éléments peuvent être traités
	//	simultanément par plusieurs threads, ce qui peut entraîner un ordre de
	//	traitement différent de l'ordre d'origine.Cela peut être plus efficace en termes de
	//	performance lorsque l'ordre n'a pas d'importance, mais cela peut poser problème si l'ordre des
	//	éléments est important. En revanche, forEachOrdered garantit que chaque élément du flux
	//	est traité dans l'ordre d'origine, même lors de l'exécution parallèle. Cela signifie que si
	//	vous avez besoin de conserver l'ordre des éléments dans votre traitement, vous devriez utiliser
	//	forEachOrdered à la place de forEach ur un flux parallèle.

}
