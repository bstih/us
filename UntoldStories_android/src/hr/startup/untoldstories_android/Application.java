package hr.startup.untoldstories_android;

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate(); 
        FontsOverride.setDefaultFont(this, "MONOSPACE", "kirstenitc.ttf");

    }
}