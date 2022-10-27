package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

  public static void main(String[] args) {
    String[] test = {"hi", "bye", "abc", "lol"};
    IntStream test1 = IntStream.of(1,2,3,4,5);
    LambdaStreamExcImp exc = new LambdaStreamExcImp();

    exc.printMessages(test, exc.getLambdaPrinter("pre - ", " - suf"));
    exc.printOdd(test1, exc.getLambdaPrinter("pre - ", " - suf"));
  }

  @Override
  public Stream<String> createStrStream(String... strings) {
    return Arrays.stream(strings);
  }

  @Override
  public Stream<String> toUpperCase(String... strings) {
    return Arrays.stream(strings).map(str -> str.toUpperCase());
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(s -> !s.contains(pattern));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    return Arrays.stream(arr);
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    return stream.collect(Collectors.toList());
  }

  @Override
  public List<Integer> toList(IntStream intStream) {
    return intStream.boxed().collect(Collectors.toList());
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    return IntStream.range(start, end);
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    return intStream.mapToDouble(num -> Math.sqrt(num));
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    return intStream.filter(num -> num % 2 != 0);
  }

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    return s -> System.out.println(prefix + s + suffix);
  }

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    createStrStream(messages).forEach(printer);
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    getOdd(intStream).mapToObj(i -> String.valueOf(i)).forEach(printer);
  }
}
