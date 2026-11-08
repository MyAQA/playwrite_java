package utils.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Deque;


@Slf4j
public final class ExtentSteps {

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    private static final ThreadLocal<Deque<ExtentTest>> STACK = ThreadLocal.withInitial(ArrayDeque::new);

    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private static final ThreadLocal<Throwable> REPORTED = new ThreadLocal<>();

    private ExtentSteps() {
    }

    public static void setPage(Page page) {
        PAGE.set(page);
    }

    public static void clearPage() {
        PAGE.remove();
    }

    static void startTest(ExtentTest test) {
        STACK.get().clear();
        STACK.get().push(test);
        REPORTED.remove();
    }

    static void finishTest() {
        STACK.remove();
        REPORTED.remove();
    }

    public static boolean isReported(Throwable t) {
        return t != null && REPORTED.get() == t;
    }

    public static void step(String name, Runnable action) {
        step(name, () -> {
            action.run();
            return null;
        });
    }

    public static <T> T step(String name, ThrowingSupplier<T> action) {
        Deque<ExtentTest> stack = STACK.get();
        if (stack.isEmpty()) {
            return sneaky(action);
        }
        ExtentTest node = stack.peek().createNode(name);
        stack.push(node);

        try {
            T result = action.get();
            node.pass("OK");
            return result;
        } catch (Throwable t) {
            if (isReported(t)) {
                node.fail("Failed in nested step");
            } else {
                node.fail(t);
                attachSreenshot(node, "Screenshot: " + name);
                REPORTED.set(t);
            }
            throw sneakyThrow(t);
        } finally {
            stack.pop();
        }
    }

    public static void info(String message) {
        Deque<ExtentTest> stack = STACK.get();
        if (!stack.isEmpty()) {
            stack.peek().info(message);
        }
    }

    private static <T> T sneaky(ThrowingSupplier<T> action) {
        try {
            return action.get();
        } catch (Throwable t) {
            throw sneakyThrow(t);
        }
    }

    static void attachSreenshot(ExtentTest node, String title) {
        Page page = PAGE.get();
        if (page == null || page.isClosed()) {
            return;
        }

        try {
            byte[] png = ScreenshotsUtil.capture(page, "failed_step");
            String base64 = Base64.getEncoder().encodeToString(png);
            node.fail(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64, title).build());

        } catch (Exception e) {
            log.warn("Could not take screenshot for step '{}': {}", title, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneakyThrow(Throwable t) throws E {
        throw (E) t;
    }
}
