# Introduction
- What does this project do?  
  This project is a Java 8 Maven-based Application that allows users to search for the occurrence of a string pattern in all files under a directory and save it as an output file. The application provides two different implementations for searching the lines in files: one using traditional for loops and another using Java 8 Stream/Lambda functions. The Stream/Lambda implementation provides better performance than the for-loop implementation. The application is deployed on the Docker Hub for ease of distribution and can be easily accessed from any machine with Docker installed. This project is useful for anyone who needs to search for texts that contain specific patterns in large numbers of files quickly and efficiently.
- Technologies
    - Java 8
    - Maven
    - Java Streams/Lambda Functions
    - Java IO APIs
    - Docker

# Quick Start
**Required Packages:**
1. Java 8
2. Maven
3. Docker

**How to run the App:**
1. Using Docker
    ```bash
    #pull the docker image form the Docker Hub
    docker pull seanchen1999/grep
    
    #Run the docker container with 3 arguments <Regex> <RootDir> <OutputFile>
    docker run --rm \
    -v `pwd`/data:/data -v `pwd`/output:/output \
    seanchen1999/grep '.*Romeo.*Juliet.*' /data /output/grep.out
    ```

2. Run the project directly
    - Clone the Github Project.
    - Use Maven to build the project.
    - Run `main` functions inside the implementations of the JavaGrep interface.

# Implemenation
The App contains 1 interface:
- JavaGrep

And two implementations of JavaGrep interface:
- JavaGrepImp (Using for loop approach)
- JavaGrepStreamImp (Using Streams/Lambda approach)

## Pseudocode
Pseudocode for the 'process()' function, which is the core function that connects all other methods.
```Python
matchedLines = []
for each file in fileList:
    lines = readLines(file)
    for each line in lines:
        if containsPattern(line):
            matchedLines.add(line)
writeToFile(matchedLiens)
```

## Performance Issue
The application may throw `java.lang.OutOfMemoryError: GC overhead limit exceeded` Exception when dealing with a large amount of data, typically when the data size is greater than the JVM memory size. This is because the program is using `List<>` to pass data between different functions.

Some possible solutions:
- Pass `Streams` instead of `Lists` between functions; this would require a new Interface.
- Using BufferReader at the same time might also help.

# Test
I implemented two JUnit classes to test the program because I wanted a systematic and repeatable way to test my program during my development process.  
Two JUnit classes are for testing two implementations separately. The coverage is 94% and 84% on JavaGrepImp and JavaGrepStreamImp, respectively.

# Deployment
This app is deployed on Docker Hub for easier distribution. Any computer that has Docker installed is able to run this App.
**How to deploy your App on Docker Hub:**
```bash
#navigate to the root direcotry of your project
cd <your_project_dir>

#log in with your Docker Hub account
docker login -u <docker_username>

#Use maven to pack your Java App
mvn clean package
#build a new docker image locally
docker build -t <docker_username>/<your_image_name>
#verify your image
docker image ls | grep "<your_image_name>"
#run docker container
docker run --rm \
-v `pwd`/data:/data -v `pwd`/output:/output \
seanchen1999/grep '.*Romeo.*Juliet.*' /data /output/grep.out

#push your image to Docker Hub
docker push <docker_username>/<your_image_name>
```

# Improvement
1. Add JavaGrepStream interface and its implementation, which allows functions to pass `Streams` between functions. This will allow the App to process data that has larger size than it's physical memory.
2. Add a controller so that the user can chose which implement to use in arguments.
