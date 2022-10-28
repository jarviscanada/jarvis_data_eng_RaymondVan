package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {
  BufferedReader br;
  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    BasicConfigurator.configure();

    JavaGrepLambdaImp javaGrepImp = new JavaGrepLambdaImp();
    javaGrepImp.setRegex(args[0]);    // eg.: .*Romeo.*Juliet.*
    javaGrepImp.setRootPath(args[1]); // eg.: grep/data/txt
    javaGrepImp.setOutFile(args[2]);  // eg.: grep/out/outputFileLambda.txt

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    listFilesStream(getRootPath())
            .forEach(file -> {
              readLinesStream(file)
                  .forEach(line -> {
                    if (containsPattern(line)) {
                      matchedLines.add(line);
                    }
                  });
            });
    writeToFile(matchedLines);
  }

  public Stream<File> listFilesStream(String rootDir) {
    File f = new File(rootDir);
    return Arrays.stream(f.listFiles());
  }

  public Stream<String> readLinesStream(File inputFile) throws IllegalArgumentException {
    try {
      br = new BufferedReader(new FileReader(inputFile));
      return br.lines();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
