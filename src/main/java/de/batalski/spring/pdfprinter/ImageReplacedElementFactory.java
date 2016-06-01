package de.batalski.spring.pdfprinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class ImageReplacedElementFactory implements ReplacedElementFactory {

	private final ReplacedElementFactory superFactory;

	public ImageReplacedElementFactory(ReplacedElementFactory superFactory) {
		this.superFactory = superFactory;
	}

	@Override
	public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
			UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {

		Element element = blockBox.getElement();
		if (element == null) {
			return null;
		}

		String nodeName = element.getNodeName();
		String className = element.getAttribute("class");
		if ("div".equals(nodeName) && className.contains("image")) {

			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes(Paths.get(new ClassPathResource("/META-INF/pdfTemplates/" + element.getAttribute("src")).getURI()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Image image = null;
			try {
				image = Image.getInstance(bytes);
			} catch (BadElementException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FSImage fsImage = new ITextFSImage(image);

			if (fsImage != null) {
				if ((cssWidth != -1) || (cssHeight != -1)) {
					fsImage.scale(cssWidth, cssHeight);
				}
				return new ITextImageElement(fsImage);

			}
		}

		return superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
	}

	@Override
	public void reset() {
		superFactory.reset();
	}

	@Override
	public void remove(Element e) {
		superFactory.remove(e);
	}

	@Override
	public void setFormSubmissionListener(FormSubmissionListener listener) {
		superFactory.setFormSubmissionListener(listener);
	}
}
