package andrii.goncharenko.unusualwhale.Controllers;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import andrii.goncharenko.unusualwhale.Models.Cloud;
import andrii.goncharenko.unusualwhale.Settings.DeviceSettings;

/**
 * Created by Andrey on 18.03.2015.
 */
public class CloudController {

    private final int CLOUD_IMAGES_COUNT = 4;

    private static CloudController instance;

    public static CloudController Instance() {
        return instance == null ? instance = new CloudController() : instance;
    }

    Random random = new Random();

    public List<Cloud> clouds = Collections.synchronizedList(new ArrayList<Cloud>());

    public void createClouds() {
        if (isNewCloudChanceMatched())
            clouds.add(new Cloud(random.nextInt(DeviceSettings.width), getRandomCloudImageIndex()));
    }

    public void lowerClouds() {
        for (Cloud cloud : clouds)
            cloud.lower();
    }

    public void deleteUnderScreenClouds() {
        synchronized (clouds) {
            for (Iterator<Cloud> item = clouds.listIterator(); item.hasNext(); ) {
                Cloud cloud = item.next();
                if (cloud.getYPosition() > DeviceSettings.height)
                    item.remove();
            }
        }
    }

    public void drawClouds(Canvas canvas, List<Drawable> cloudImages) {
        synchronized (clouds) {
            for (Iterator<Cloud> item = clouds.listIterator(); item.hasNext(); ) {
                Cloud cloud = item.next();
                cloudImages.get(cloud.imageIndex).setBounds(
                        cloud.getXPosition(),
                        cloud.getYPosition(),
                        cloud.getXPosition() + cloudImages.get(cloud.imageIndex).getMinimumWidth(),
                        cloud.getYPosition() + cloudImages.get(cloud.imageIndex).getMinimumHeight());
                cloudImages.get(cloud.imageIndex).draw(canvas);
            }
        }
    }

    private boolean isNewCloudChanceMatched() {
        return random.nextInt(50) == 1;
    }

    private int getRandomCloudImageIndex() {
        return random.nextInt(CLOUD_IMAGES_COUNT);
    }

    public void clear() {
        instance = null;
    }

}
