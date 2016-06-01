package de.batalski.spring.pdfprinter;

import java.io.IOException;

import org.junit.Test;

import com.itextpdf.text.DocumentException;

public class FlyingSoucerTestServiceTest {

	@Test
	public void test() throws DocumentException, IOException {
		new FlyingSoucerTestService().test();
	}

}
