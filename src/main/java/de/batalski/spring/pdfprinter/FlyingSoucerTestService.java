package de.batalski.spring.pdfprinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public class FlyingSoucerTestService {

	public void test() throws DocumentException, IOException {
	    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	    templateResolver.setPrefix("META-INF/pdfTemplates/");
	    templateResolver.setSuffix(".html");
	    templateResolver.setTemplateMode("XHTML");
	    templateResolver.setCharacterEncoding("UTF-8");

	    TemplateEngine templateEngine = new TemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver);

	    Context ctx = new Context();
	    ctx.setVariable("message", "This is a very nice message");
	    String htmlContent = templateEngine.process("messageTpl", ctx);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ITextRenderer renderer = new ITextRenderer();
	    ITextFontResolver fontResolver = renderer.getFontResolver();

	    ClassPathResource regular = new ClassPathResource("/META-INF/fonts/LiberationSerif-Regular.ttf");
	    fontResolver.addFont(regular.getURL().toString(), BaseFont.IDENTITY_H, true);

	    renderer.setDocumentFromString(htmlContent);
	    renderer.layout();
	    renderer.createPDF(os);

	    byte[] pdfAsBytes = os.toByteArray();
	    os.close();

	    try (OutputStream writer = Files.newOutputStream( Paths.get("/tmp/message.pdf"))) {
	        writer.write(pdfAsBytes);
	    }
	}

}