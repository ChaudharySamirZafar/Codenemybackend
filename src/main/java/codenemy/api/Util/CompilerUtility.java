package codenemy.api.Util;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Piston.PistonFile;
import codenemy.api.Compiler.Model.Piston.PistonRequest;
import codenemy.api.Compiler.Model.Piston.PistonResponse;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class CompilerUtility {
    private final static int EASY_POINT_MULTIPLIER = 10;
    private final static int MEDIUM_POINT_MULTIPLIER = 20;
    private final static int HARD_POINT_MULTIPLIER = 30;

    /**
     * A method that takes a code snippet and return the code snippet output via a Model.
     * @return a model that contains the output, result & errors of the code snippet
     */
    public TestCaseResult getTestCaseResult(String fileName, String fileContent, String programmingLang, String version) {

        TestCaseResult testCaseResult = new TestCaseResult(0, null);

        List<PistonFile> fileArrayList = List.of(new PistonFile(fileName, fileContent));
        PistonRequest pistonRequest = new PistonRequest(programmingLang, fileArrayList);
        pistonRequest.setVersion(version);

        PistonResponse pistonResponse = makeRequest(pistonRequest);

        if (pistonResponse == null) {
            log.info("The request to piston is breaking.");
            return null;
        }

        // If the process to run the code was killed then tell the user their code took too long
        if (pistonResponse.run().signal() != null && pistonResponse.run().signal().equalsIgnoreCase("SIGKILL")) {
            testCaseResult.setError(List.of("Your code exceeded the runtime time limit\n"));
            return testCaseResult;
        }

        // If the code contained error then return them to the user
        if (!pistonResponse.run().stderr().isEmpty() || !pistonResponse.run().stderr().isBlank()) {
            List<String> errors = Arrays.asList(pistonResponse.run().stderr().split("\n"));
            testCaseResult.setError(errors);
            return testCaseResult;
        }

        extractDataFromPistonResponse(pistonResponse, testCaseResult);

        return testCaseResult;
    }

    /**
     * A method that extracts the output and results from the code snippet response.
     */
    private void extractDataFromPistonResponse(PistonResponse pistonResponse, TestCaseResult testCaseResult) {

        String[] arrOfStdOut = pistonResponse.run().stdout().split("\n");

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();

        /*
          The result array is the result the method returns when it has been run. The code
          Output is what the user want's to print out. i.e. a print() in python
         */

        for (String s : arrOfStdOut) {
            if (s.startsWith("Result - ")) {
                result.add(s.replace("Result - ", ""));
            } else {
                output.add(s + "\n");
            }
        }

        testCaseResult.setOutput(output);
        testCaseResult.setResult(result);
    }

    /**
     * This method calls the Piston API to compile, run and receive the output of the code snippet
     */
    public PistonResponse makeRequest(PistonRequest requestData) {

        WebClient client = WebClient.create("https://emkc.org/api/v2/piston");

        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting request data to JSON", e);
        }

        int maxRetries = 3;
        int retryCount = 0;
        long retryDelayMs = 1000;

        /*
            Sometimes a TOO_MANY_REQUESTS httpStatus can be returned, if so retry the request.
            This will only be re-tried 3 times and 1000ms delay has been added for a little throttling.
        */

        while (true) {
            try {
                return client
                        .post()
                        .uri("/execute/")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(BodyInserters.fromValue(requestBody))
                        .retrieve()
                        .bodyToMono(PistonResponse.class)
                        .block(Duration.ofSeconds(10));
            } catch (WebClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS && retryCount < maxRetries) {
                    // If we receive a 429 error and haven't exceeded the max retries, wait and retry the request.
                    retryCount++;
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    // If the error is not a 429 error, or if we've exceeded the max retries, re-throw the exception.
                    throw e;
                }
            }
        }
    }

    /**
     * This method is run when a user runs his code, so their code output is ran against one testcase.
     * This method calculates the result of the testCaseResult and stores the information within the model SingleTestCaseResult
     */
    public SingleTestCaseResult calculateSingleTestResultWithResponse(Problem problem, TestCaseResult result) {

        TestCase firstTestCase = problem.getTestCases().get(0);
        String firstResult = result.getResult().get(0);

        return new
                SingleTestCaseResult(
                firstTestCase.getInput(),
                firstTestCase.getOutput(),
                firstResult,
                result.getOutput(),
                firstTestCase.getOutput().equals(firstResult),
                result.getError());
    }

    /**
     * This method is run when a user submits his code, so their code output is ran against all testcases.
     * This method calculates the result of the testCaseResult and stores the information within the model MultipleTestCaseResults
     */
    public MultipleTestCaseResults calculateAllTestResultsWithResponse(Problem problem, TestCaseResult result) {

        List<TestCase> testCaseList = problem.getTestCases();

        List<SingleTestCaseResult> listOfResults = new ArrayList<>();
        int passCount = 0;

        for (int i = 0; i < problem.getTestCases().size(); i++) {
            String output = result.getResult().get(i);
            TestCase testCase = testCaseList.get(i);

            listOfResults.add(new SingleTestCaseResult(
                    testCase.getInput(),
                    testCase.getOutput(),
                    output,
                    null,
                    testCase.getOutput()
                            .equals(output),
                    result.getError())
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

    /**
     * @param difficulty of the problem
     * @return returns the correct point's multiplier for the problem submission.
     */
    private int getPointMultiplier(String difficulty) {

        return switch (difficulty) {
            case "Medium", "medium" -> MEDIUM_POINT_MULTIPLIER;
            case "Hard", "hard" -> HARD_POINT_MULTIPLIER;
            default -> EASY_POINT_MULTIPLIER;
        };
    }
}
