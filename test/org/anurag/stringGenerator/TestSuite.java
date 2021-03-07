package org.anurag.stringGenerator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicGeneratorTest.class, RepeaterTest.class, LiteralTest.class, RegExpBuilderTest.class })
public class TestSuite {

}
