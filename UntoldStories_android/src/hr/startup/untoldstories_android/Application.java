package hr.startup.untoldstories_android;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public final class Application extends android.app.Application {
	@Override
	public void onCreate() {
		super.onCreate();
		FontsOverride.setDefaultFont(this, "MONOSPACE", "kirstenitc.ttf");
		int glowDrawableId = getResources().getIdentifier("overscroll_glow", "drawable", "android");
        int edgeDrawableId = getResources().getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidGlow = ContextCompat.getDrawable(this, glowDrawableId);
        Drawable androidEdge = ContextCompat.getDrawable(this, edgeDrawableId);
        androidGlow.setAlpha(0);        
        androidGlow.setVisible(false, false);
       androidEdge.setVisible(false, false);
	}
}