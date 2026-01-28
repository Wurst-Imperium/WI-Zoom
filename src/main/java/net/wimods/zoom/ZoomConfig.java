package net.wimods.zoom;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public final class ZoomConfig
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("wi_zoom.json");

    public static final ZoomConfig INSTANCE = new ZoomConfig();

    public double maxZoom = 50.0;
    public ActionBarMode actionBarMode = ActionBarMode.DISABLED;

    private ZoomConfig() {}

    public static void load()
    {
        if(!Files.exists(PATH))
        {
            save();
            return;
        }

        try(Reader reader = Files.newBufferedReader(PATH))
        {
            ZoomConfig loaded = GSON.fromJson(reader, ZoomConfig.class);
            if(loaded != null)
            {
                INSTANCE.maxZoom = loaded.maxZoom;
                INSTANCE.actionBarMode = loaded.actionBarMode;
            }
        }
        catch(Exception e)
        {
            System.out.println("[WI Zoom] Failed to load config: " + e);
            e.printStackTrace();
        }

        if(INSTANCE.actionBarMode == null)
            INSTANCE.actionBarMode = ActionBarMode.DISABLED;

        if(INSTANCE.maxZoom < 1.0)
            INSTANCE.maxZoom = 1.0;

    }

    public static void save()
    {
        try
        {
            Files.createDirectories(PATH.getParent());
        }
        catch(Exception e)
        {
            System.out.println("[WI Zoom] Failed to create config dir: " + e);
            e.printStackTrace();
        }

        try(Writer writer = Files.newBufferedWriter(PATH))
        {
            GSON.toJson(INSTANCE, writer);
        }
        catch(Exception e)
        {
            System.out.println("[WI Zoom] Failed to save config: " + e);
            e.printStackTrace();
        }
    }
}
