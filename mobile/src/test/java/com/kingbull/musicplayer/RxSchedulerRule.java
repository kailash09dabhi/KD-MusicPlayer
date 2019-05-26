package com.kingbull.musicplayer;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit Test Rule which overrides RxJava and Android schedulers for use in unit tests.
 * <p>
 * All schedulers are replaced with Schedulers.trampoline().
 */
public class RxSchedulerRule implements TestRule {

  private Scheduler SCHEDULER_INSTANCE = Schedulers.trampoline();

  private Function<Scheduler, Scheduler> schedulerFunction = scheduler -> SCHEDULER_INSTANCE;

  private Function<Callable<Scheduler>, Scheduler> schedulerFunctionLazy = schedulerCallable -> SCHEDULER_INSTANCE;

  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunctionLazy);

        RxJavaPlugins.reset();
        RxJavaPlugins.setInitIoSchedulerHandler(schedulerFunctionLazy);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(schedulerFunctionLazy);
        RxJavaPlugins.setInitComputationSchedulerHandler(schedulerFunctionLazy);

        base.evaluate();

        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
      }
    };
  }
}