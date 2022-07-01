package com.akchimwf.loftcoin1.util.formatter;

import static com.google.common.truth.Truth.*;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.akchimwf.loftcoin1.util.formatter.PriceFormatter;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.text.NumberFormat;
import java.util.Locale;

/*Context dependent class*/
@RunWith(AndroidJUnit4.class)
public class PriceFormatterTest {

    private PriceFormatter formatter;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        formatter = new PriceFormatter(context);
    }

    @Config(manifest = Config.NONE)
    @Test
    public void format_EUR() {
        assertThat(formatter.format("EUR", 1.23)).isEqualTo(NumberFormat
                .getCurrencyInstance(Locale.GERMANY).format(1.23));
    }

    @Config(manifest = Config.NONE)
    @Test
    public void format_RUB() {
        assertThat(formatter.format("RUB", 1.23)).isEqualTo(NumberFormat
                .getCurrencyInstance(new Locale("ru", "RU")).format(1.23));
    }

    @Config(manifest = Config.NONE)
    @Test
    public void format_default() {
        /*get default locales for device*/
        final LocaleListCompat locales = ConfigurationCompat.getLocales(context.getResources().getConfiguration());

        /*for CAD should be used default device locale*/
        assertThat(formatter.format("CAD", 1.23)).isEqualTo(NumberFormat
                .getCurrencyInstance(locales.get(0)).format(1.23));
    }
}