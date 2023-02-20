package codenemy.api.Util;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Piston.PistonFile;
import codenemy.api.Compiler.Model.Piston.PistonRequest;
import codenemy.api.Compiler.Model.Piston.PistonResponse;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Slf4j
public class CompilerUtility {
    static int EASY_POINT_MULTIPLIER = 10;
    static int MEDIUM_POINT_MULTIPLIER = 20;
    static int HARD_POINT_MULTIPLIER = 30;

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

        if (pistonResponse.run().signal() != null && pistonResponse.run().signal().equalsIgnoreCase("SIGKILL")) {
            testCaseResult.setError(List.of("Your code exceeded the runtime time limit\n"));
            return testCaseResult;
        }

        if (!pistonResponse.run().stderr().isEmpty() || !pistonResponse.run().stderr().isBlank()) {
            // Get the errors.
            List<String> errors = Arrays.asList(pistonResponse.run().stderr().split("\n"));
            testCaseResult.setError(errors);
            return testCaseResult;
        }

        String[] arrOfStdOut = pistonResponse.run().stdout().split("\n");
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();

        for(String s : arrOfStdOut) {
            if (s.startsWith("Result - ")) {
                result.add(s.replace("Result - ", ""));
            } else {
                output.add(s + "\n");
            }
        }

        testCaseResult.setOutput(output);
        testCaseResult.setResult(result);

        return testCaseResult;
    }

    public PistonResponse makeRequest(PistonRequest requestData) {
        String url = "https://emkc.org/api/v2/piston/execute";

        URL obj = null;
        HttpURLConnection con = null;

        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            // Set the request method and headers
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();

            // Add throttling
            Thread.sleep(3000); // wait for 1 second before sending the request

            // Set the request body
            String requestBody = objectMapper.writeValueAsString(requestData);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(requestBody);
            wr.flush();
            wr.close();

            // Get the response
            int responseCode = con.getResponseCode();

            // Check if the response is okay
            if (responseCode == HttpStatus.OK.value()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Deserialize the response
                return objectMapper.readValue(response.toString(), PistonResponse.class);
            }
        }
        catch (Exception exception) {
            log.info(exception.getMessage());
        }

        return null;
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
                firstTestCase.getOutput().equals(firstResult),
                result.getError());
    }

    public MultipleTestCaseResults calculateAllTestResultsWithResponse(Problem problem, TestCaseResult result) {
        // Output and testCaseList should be of same size.
        List<TestCase> testCaseList = problem.getTestCases();

        List<SingleTestCaseResult> listOfResults = new ArrayList<>();
        int passCount = 0;

        for(int i = 0; i < problem.getTestCases().size(); i++) {
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

    private int getPointMultiplier(String difficulty){
        return switch (difficulty) {
            case "Medium", "medium" -> MEDIUM_POINT_MULTIPLIER;
            case "Hard", "hard" -> HARD_POINT_MULTIPLIER;
            default -> EASY_POINT_MULTIPLIER;
        };
    }
}
