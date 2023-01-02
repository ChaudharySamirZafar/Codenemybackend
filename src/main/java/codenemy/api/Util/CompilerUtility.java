package codenemy.api.Util;

import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class CompilerUtility {
    static int EASY_POINT_MULTIPLIER = 10;
    static int MEDIUM_POINT_MULTIPLIER = 20;
    static int HARD_POINT_MULTIPLIER = 30;

    private File currentFile = null;

    public void createNewFile(String name){
        currentFile = new File(name);

        try {
            if (currentFile.createNewFile()) {
                log.info("File {} created", name);
            }
        }
        catch (IOException ex) {
            log.info("File {} creation failed", name);
        }
    }

    public void writeScriptToFile(String script) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(currentFile));
            bufferedWriter.write(script);
            bufferedWriter.close();
        }
        catch (IOException ignored) {
            log.info("Failed writing script to file");
        }
    }

    public Process startProcess(String command){
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        }
        catch (IOException exception) {
            log.info("IOException in process");
            log.info("Command {} ", command);
        } catch (InterruptedException e) {
            log.info("InterruptedException in process");
            log.info("Command {} ", command);
        }

        return process;
    }

    public TestCaseResult retrieveTestCaseResult(Request request, Process process) {

        TestCaseResult testCaseResult = retrieveScriptReturnValues(request);

        String output;

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));

        try {
            while ((output = stdInput.readLine()) != null) {
                System.out.println(output);
                // Add output here.
                assert testCaseResult != null;
                testCaseResult.getOutput().add(output);
            }

            while ((output = stdError.readLine()) != null) {
                System.out.println(output);
            }

            stdInput.close();
            stdError.close();
        }
        catch (IOException exception) {
            System.out.println("Error with printing the data");
        }

        return testCaseResult;
    }

    public TestCaseResult retrieveScriptReturnValues(Request request) {
        Scanner scanner;

        try {
            File file = new File("results_" + request.username() + ".txt");
            if (file.exists()){
                file.createNewFile();
            }
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException ignored) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> listOfOutput = new ArrayList<>();

        TestCaseResult testCaseResult = null;
        while (scanner.hasNextLine()) {
            String output = scanner.nextLine();
            listOfOutput.add(output);
            testCaseResult = new TestCaseResult(1, listOfOutput);
        }

        scanner.close();

        return testCaseResult;
    }

    public SingleTestCaseResult calculateSingleTestResultWithResponse(Problem problem, TestCaseResult result) {

        TestCase firstTestCase = problem.getTestCases().get(0);
        String firstResult = result.getResult().get(0);

        return new
                SingleTestCaseResult(
                firstTestCase.getInput(),
                firstTestCase.getOutput(),
                firstResult,
                result.getOutput(),
                firstTestCase.getOutput()
                .equals(firstResult));
    }

    public MultipleTestCaseResults calculateAllTestResultsWithResponse(Problem problem, TestCaseResult result) {
        // Output and testCaseList should be of same size.
        List<TestCase> testCaseList = problem.getTestCases();

        boolean sameSize = testCaseList.size() == result.getResult().size();

        int timesToLoop = testCaseList.size();
        if (!sameSize) {
           timesToLoop = Math.min(testCaseList.size(), result.getOutput().size());
        }

        List<SingleTestCaseResult> listOfResults = new ArrayList<>();
        int passCount = 0;

        for(int i = 0; i < timesToLoop; i++) {
           String output = result.getResult().get(i);
           TestCase testCase = testCaseList.get(i);

           listOfResults.add(new SingleTestCaseResult(
                   testCase.getInput(),
                   testCase.getOutput(),
                   output,
                   null,
                   testCase.getOutput()
                           .equals(output))
           );

           boolean pass = testCase.getOutput().equals(output);

           if (pass) passCount++;
        }

        int percentage = (((passCount) * 100) / testCaseList.size());

        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        multipleTestCaseResults.setTestCaseResultList(listOfResults);
        multipleTestCaseResults.setPercentage(percentage);
        multipleTestCaseResults.setPoints(percentage * getPointMultiplier(problem.getDifficulty()));

        return multipleTestCaseResults;
    }

    private int getPointMultiplier(String difficulty){
        return switch (difficulty) {
            case "Medium", "medium" -> MEDIUM_POINT_MULTIPLIER;
            case "Hard", "hard" -> HARD_POINT_MULTIPLIER;
            default -> EASY_POINT_MULTIPLIER;
        };
    }

    public void deleteFile(String name){
        File file = new File(name);

        if (file.delete()){
            log.info("File {} deleted", name);
        } else {
            log.info("File {} not deleted", name);
        }
    }
}
