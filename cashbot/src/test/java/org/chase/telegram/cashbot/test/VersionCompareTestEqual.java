package org.chase.telegram.cashbot.test;

import static org.junit.Assert.*;

import org.chase.telegram.cashbot.Version;
import org.junit.Before;
import org.junit.Test;

public class VersionCompareTestEqual {

	Version v1;
	Version v2;
	
	@Before
	public void setUp() throws Exception {
		v1 = new Version(1, 1, 0);
		v2 = new Version(1, 1, 0);
	}

	@Test
	public void test() {
		assertEquals(0, v1.compareTo(v2));
	}

}
