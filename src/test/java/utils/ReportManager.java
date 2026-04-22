package utils;

import com.aventstack.extentreports.ExtentTest;

public class ReportManager {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

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
}
