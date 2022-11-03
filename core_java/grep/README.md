# Introduction
This app is an implementation of the grep linux command. One implementation involves using only core Java and and second implementation involves using Java 8 features such as Lambda and Stream APIs. Libraries used include the Java Regex API for pattern matching, FileReader/FileWriter for I/O and BufferedReader to handle large input data.  Moreover, a docker image was created and deployed onto Docker Hub for convenient distribution.

The app was developed using IntelliJ along with Maven to handle dependencies such as slf4j and log4j.


# Quick Start
The app can be used locally by either cloning the repo or pulling the image from docker hub.
### Cloning the Repo
Using your IDE of choice, build and run the main class of `JavaGrepImp` or `JavaGrepLambdaImp` (lambda and streams version) with the following arguments: `[regex] [inputDir] [outputFile]`.

### Docker Hub
```
# Pull image from docker hub
docker pull vraymond028/grep

# Build the docker container
docker build -t vraymond028/grep

# Run the docker container
docker run --rm -v `pwd`/data:data -v `pwd`/log:/log vraymond028/grep [regex] [inputDir] [outputFile]
```

# Implementation
Given the input arguments as stated above, the app searches through the files in the input directory for lines that match the given regex pattern. Matching lines are then written to the given output file.

## Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
An `OutOfMemoryError` exception could occur when the JVM runs out of heap memory. This issue could arise when our app recursively finds all of the files in the input directory. At the moment, our app stores all of these files in the heap memory. If the combined size of all of the files exceeds the allocated memory, an exception occurs.

A potential fix is to simply increase the maximum heap memory allocated to the JVM by manually setting the `-xmx` option, e.g. `java -Xmx512m JavaGrepImp.java`.

For input data that exceeds the total physical memory of our machine, we may have to resort to processing all I/O using the BufferedReader class or the Stream API. BufferedReader allows you to read a file without storing the entire contents of the file in memory but rather chunks of data as specified by the buffer size. Similarly, the Stream API will allow you to work with large inputs but will only store the contents that are currently being manipulated in the memory.

# Test
Testing was done by preparing sample test data along with sample test cases (varying regex patterns) and comparing the results to our expected output.

# Deployment
The app was dockerized by initially building an uber jar file containing the app and all its dependencies using Maven via the `mvn clean package` command. We are then able build a new docker image by copying our uber far file to the openjdk base image.  Refer to the Dockerfile for more details.

Finally, I built the image using the `docker build` command and pushed to Docker hub via `docker push`.

# Improvement
1. Set up unit tests using a unit testing framework such as JUnit
2. Cache the output of frequently used searches
3. Add more logging information to improve user friendliness 

