package org.dbs24.component;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class AvatarsService extends AbstractApplicationService {

    @Value("${config.wa.subscription.avatar.maxWidth:96}")
    private Integer maxWidth;
    @Value("${config.wa.subscription.avatar.maxHeight:96}")
    private Integer maxHeight;

    public Mono<byte[]> downsizeImage(byte[] imgSource) {

        return Mono.just(imgSource)
                .flatMap(src -> {

                    byte[] dest = src;

                    try {

                        final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(src));

                        if (bufferedImage.getWidth() > maxWidth || bufferedImage.getHeight() > maxHeight) {

                            final Image img = ImageIO.read(new ByteArrayInputStream(src));
                            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            final BufferedImage resizeImage = resizeImage(img, maxWidth, maxHeight);

                            ImageIO.write(resizeImage, "jpg", baos);
                            baos.flush();
                            dest = baos.toByteArray();

                        }

                    } catch (Throwable throwable) {
                        log.error(throwable);

                        throwable.printStackTrace();
                    }

                    return Mono.just(dest);

                });
    }

    private BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}
