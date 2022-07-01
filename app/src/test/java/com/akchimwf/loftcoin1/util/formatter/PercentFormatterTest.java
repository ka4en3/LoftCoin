package com.akchimwf.loftcoin1.util.formatter;

import static com.google.common.truth.Truth.*;
import static org.junit.Assert.*;

import com.akchimwf.loftcoin1.util.formatter.PercentFormatter;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;

/*PercentFormatter is not related to Android->pure Java class->junit is enough to test*/
public class PercentFormatterTest {

    private PercentFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new PercentFormatter();
    }

    /*this test only runs on PC not on device*/
    @Test
    public void string_contains_exact_two_fractional_digits() {
        assertThat(formatter.format(1d)).isEqualTo("1.00%");       //pass
        assertThat(formatter.format(1.2345)).isEqualTo("1.23%");   //pass
        assertThat(formatter.format(1.2355)).isEqualTo("1.23%");   //fail because of rounding
    }
}