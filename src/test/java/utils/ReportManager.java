package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 📊 ReportManager — Thread-safe Extent Report node manager.
 *
 * ── GROUPING STRATEGY ────────────────────────────────────────────────────────
 * Cucumber 7.x Scenario Outline row IDs follow the format:
 *
 *   "features/02_Dashboard.feature:22;copy-account-details-from-account-screen;0"
 *                                  ^^ outline start line — STABLE KEY
 *
 * Regular Scenario IDs have no ";" suffix:
 *   "features/02_Dashboard.feature:4"
 *
 * We use the portion BEFORE the first ";" as the grouping key. This is the
 * feature file URI + line number, which uniquely identifies one outline block
 * and remains the same for ALL rows of that outline.
 *
 * ── COUNT STRATEGY ───────────────────────────────────────────────────────────
 * - Each unique outline block (by line key) = 1 scenario.
 * - Each regular Scenario                  = 1 scenario.
 * - Each row (whether outline or single)   = 1 test case.
 * - The "📊 Execution Summary" node is NOT counted (info only).
 */
public class ReportManager {

    // ── Current test node (child for outlines, top-level for singles) ─────────
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ── Grouping registry: lineKey → parent ExtentTest node ──────────────────
    // lineKey = "feature-uri:line" (stable across all rows of the same outline)
    private static final Map<String, ExtentTest> parentNodes = new LinkedHashMap<>();

    // ── Base scenario name cache: lineKey → display name ─────────────────────
    private static final Map<String, String> parentNames = new LinkedHashMap<>();

    // ── Row counter per outline: lineKey → row count ──────────────────────────
    private static final Map<String, AtomicInteger> rowCounters = new LinkedHashMap<>();

    // ── Scenario pass/fail tracking: lineKey → true=has failure ─────────────
    private static final Map<String, Boolean> scenarioHasFailure = new LinkedHashMap<>();

    // ── Counters ─────────────────────────────────────────────────────────────
    private static final AtomicInteger totalScenarios  = new AtomicInteger(0);
    private static final AtomicInteger passedScenarios  = new AtomicInteger(0);
    private static final AtomicInteger failedScenarios  = new AtomicInteger(0);
    private static final AtomicInteger totalTestCases   = new AtomicInteger(0);
    private static final AtomicInteger passedTestCases  = new AtomicInteger(0);
    private static final AtomicInteger failedTestCases  = new AtomicInteger(0);

    // ══════════════════════════════════════════════════════════════════════════
    // 🔹 Public API — Node Creation
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Creates a standalone top-level test node for a regular (non-outline) scenario.
     * Registers it as 1 new scenario.
     *
     * @param extent       The ExtentReports instance
     * @param scenarioId   Cucumber scenario ID (e.g. "features/file.feature:4")
     * @param scenarioName Human-readable scenario name
     */
    public static void createSingleTest(ExtentReports extent, String scenarioId, String scenarioName) {
        ExtentTest node = extent.createTest(scenarioName);
        test.set(node);

        // Register as 1 scenario
        scenarioHasFailure.put(scenarioId, false);
        totalScenarios.incrementAndGet();

        System.out.println("[ReportManager] Single: \"" + scenarioName + "\" (key=" + scenarioId + ")");
    }

    /**
     * Creates or reuses a parent node for a Scenario Outline row, then creates
     * a child node for this specific iteration.
     *
     * @param extent         The ExtentReports instance
     * @param lineKey        Stable grouping key = the part before ";" in the scenario ID
     *                       (e.g. "features/02_Dashboard.feature:22")
     * @param outlineTitle   The base outline scenario title (shared across all rows)
     * @param iterationLabel A descriptive label for this row (e.g. "Copy Account Number")
     */
    public static void createOrReuseScenarioNode(
            ExtentReports extent, String lineKey, String outlineTitle, String iterationLabel) {

        ExtentTest parent;

        synchronized (parentNodes) {
            if (!parentNodes.containsKey(lineKey)) {
                // First row for this outline → create parent node
                parent = extent.createTest(outlineTitle);
                parentNodes.put(lineKey, parent);
                parentNames.put(lineKey, outlineTitle);
                rowCounters.put(lineKey, new AtomicInteger(0));
                scenarioHasFailure.put(lineKey, false);
                totalScenarios.incrementAndGet();
                System.out.println("[ReportManager] New outline parent: \"" + outlineTitle + "\" (key=" + lineKey + ")");
            } else {
                parent = parentNodes.get(lineKey);
                System.out.println("[ReportManager] Reusing outline parent: \"" + parentNames.get(lineKey) + "\" (key=" + lineKey + ")");
            }
        }

        int rowNum = rowCounters.get(lineKey).incrementAndGet();
        String childLabel = "Row " + rowNum + ": " + iterationLabel;
        ExtentTest child = parent.createNode(childLabel);
        test.set(child);

        System.out.println("[ReportManager] Child node created: \"" + childLabel + "\"");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 🔹 Public API — Result Tracking
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Records the result of one test case (one Cucumber scenario / outline row).
     * Call from @After for every scenario.
     *
     * @param lineKey Grouping key (same value passed to createSingleTest / createOrReuseScenarioNode)
     * @param passed  true = passed, false = failed
     */
    public static void markTestCaseResult(String lineKey, boolean passed) {
        totalTestCases.incrementAndGet();
        if (passed) {
            passedTestCases.incrementAndGet();
        } else {
            failedTestCases.incrementAndGet();
            // Mark the parent scenario as failed too
            scenarioHasFailure.put(lineKey, true);
        }
    }

    /**
     * Finalizes scenario-level pass/fail counts.
     * Call ONCE from @AfterAll before flushing the report.
     */
    public static void finalizeScenarioCounts() {
        for (Map.Entry<String, Boolean> entry : scenarioHasFailure.entrySet()) {
            if (entry.getValue()) {
                failedScenarios.incrementAndGet();
            } else {
                passedScenarios.incrementAndGet();
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 🔹 Public API — Thread-local Accessors
    // ══════════════════════════════════════════════════════════════════════════

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void log(String message) {
        if (test.get() != null) {
            test.get().info(message);
        }
    }

    // ── Counter Getters ──────────────────────────────────────────────────────
    public static int getTotalScenarios()  { return totalScenarios.get(); }
    public static int getPassedScenarios() { return passedScenarios.get(); }
    public static int getFailedScenarios() { return failedScenarios.get(); }
    public static int getTotalTestCases()  { return totalTestCases.get(); }
    public static int getPassedTestCases() { return passedTestCases.get(); }
    public static int getFailedTestCases() { return failedTestCases.get(); }

    /**
     * Checks whether a given lineKey is already tracked as an outline parent.
     * Used by Hooks to determine if a node needs to be created or reused.
     */
    public static boolean isOutlineParentTracked(String lineKey) {
        return parentNodes.containsKey(lineKey);
    }
}
