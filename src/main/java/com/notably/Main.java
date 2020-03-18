package com.notably;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.notably.logic.correction.CorrectionEngine;
import com.notably.logic.correction.CorrectionResult;
import com.notably.logic.correction.StringCorrectionEngine;

/**
 * The main entry point to the application.
 *
 * This is a workaround for the following error when MainApp is made the
 * entry point of the application:
 *
 *     Error: JavaFX runtime components are missing, and are required to run this application
 *
 * The reason is that MainApp extends Application. In that case, the
 * LauncherHelper will check for the javafx.graphics module to be present
 * as a named module. We don't use JavaFX via the module system so it can't
 * find the javafx.graphics module, and so the launch is aborted.
 *
 * By having a separate main class (Main) that doesn't extend Application
 * to be the entry point of the application, we avoid this issue.
 */
public class Main {
    /**
     *
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter number of options supplied: ");
            int n = scanner.nextInt();

            List<String> options = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                System.out.print(String.format("Enter option #%d: ", i + 1));
                options.add(scanner.next());
            }

            System.out.print("Enter correction threshold: ");
            int threshold = scanner.nextInt();

            System.out.println();

            CorrectionEngine<String> correctionEngine = new StringCorrectionEngine(options, threshold);

            while (true) {
                System.out.print("Query: ");
                String query = scanner.next();

                CorrectionResult<String> result = correctionEngine.correct(query);
                switch (result.getCorrectionStatus()) {
                case UNCHANGED:
                case CORRECTED:
                    System.out.println("Corrected: " + result.getCorrectedItem().get());
                    break;
                case FAILED:
                default:
                    System.out.println("Failed to correct!");
                }
            }
        }
    }
}
