package test;

import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.PrintStream;

/**
 * @author kamontat
 * @version 1.0
 * @since Sat 01/Apr/2017 - 2:02 AM
 */
public class SuiteTester {
	// can config
	private static final boolean ignoreException = false; // ignore to print exception stack
	
	private static RealSystem system = new RealSystem();
	
	public static void main(String[] args) {
		JUnitCore core = new JUnitCore();
		core.addListener(new MyListener(system));
		
		// add new testcase here
		core.run(Test.class);
	}
	
	static class MyListener extends TextListener {
		private static final String TAB = String.format("%4s", "");
		private final PrintStream writerOut;
		private boolean ignore = ignoreException;
		
		public MyListener(JUnitSystem system) {
			super(system);
			writerOut = system.out();
		}
		
		@Override
		public void testStarted(Description description) {
			writerOut.append(String.format("\nStarting: %-20s", getDescriptionString(description)));
		}
		
		@Override
		public void testFailure(Failure failure) {
			writerOut.append("Fail");
		}
		
		@Override
		public void testIgnored(Description description) {
			writerOut.append(String.format("\nIgnored:  %-20s", getDescriptionString(description)));
		}
		
		@Override
		protected void printHeader(long runTime) {
			// do nothing
		}
		
		@Override
		protected void printFailures(Result result) {
			writerOut.append("\n");
			writerOut.append("\n");
			super.printFailures(result);
		}
		
		@Override
		protected void printFailure(Failure each, String prefix) {
			writerOut.append(prefix).append(") ");
			writerOut.append(getDescriptionString(each.getDescription()));
			writerOut.append(each.getMessage()).append("\n");
			if (!ignore) {
				StackTraceElement[] e = each.getException().getStackTrace();
				for (int i = 1; i < e.length; i++) {
					if (e[i].isNativeMethod()) break;
					writerOut.append(TAB).append(e[i].toString()).append("\n");
				}
			}
		}
		
		@Override
		protected void printFooter(Result result) {
			writerOut.append("\n");
			writerOut.append(String.format("Test run: %d, Failure: %d, Ignore: %d, Time: %s", result.getRunCount(), result.getFailureCount(), result.getIgnoreCount(), super.elapsedTimeAsString(result.getRunTime()))).append("\n");
			
			if (result.wasSuccessful()) writerOut.append("COMPLETE!").append("\n");
			else writerOut.append("FAIL!");
			
			done();
		}
		
		private void done() {
			writerOut.close();
		}
		
		private String getDescriptionString(Description description) {
			String classAndPackageName = description.getClassName();
			String className = classAndPackageName.substring(classAndPackageName.lastIndexOf(".") + 1, classAndPackageName.length());
			return String.format("%s-%s ", className, description.getMethodName());
		}
	}
}