package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-25
 * Time: 上午12:07
 * To change this template use File | Settings | File Templates.
 */
public class DoradoIcons {

    public static final Icon DORADO7_ICON = load("/icons/dorado7.png");
    public static final Icon DORADO7_TOUCH_ICON = load("/icons/dorado7-touch.gif");
    public static final Icon DORADO7_PROPERTY_SHEET=load("/icons/dorado7-property-sheet.png");
    public static final Icon DORADO7_VIEW_FILE = load("/icons/view-file.gif");
    public static final Icon DORADO7_TOUCH_FILE = load("/icons/touch-file.gif");
    public static final Icon UPDATE_CONFIG_RULES = load("/icons/update-rules.png");

    private static Icon load(String path) {
        return IconLoader.getIcon(path, DoradoIcons.class);
    }

    public static Icon load(File file) {
        URL url=null;
        try {
             url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return IconLoader.findIcon(url);
    }

}