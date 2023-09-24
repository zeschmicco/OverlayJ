package overlay;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class Overlay extends Window implements NativeMouseListener {
    final Color transparent = new Color(0, 0, 0, 0);

    INIConfiguration conf = new INIConfiguration();
    boolean hideOnADS;

    int edgeLength;
    int thickness;
    int center;
    int offset;

    public Overlay() {
        super(null);

        readConfiguration();

        setAlwaysOnTop(true);
        setBackground(transparent);
        setLocation(calcLocation());
        setSize(calcDimension());

        setVisible(true);

        enableSystemTray();
        enableWinTransparency();
    }

    private void readConfiguration() {
        try (FileReader fileReader = new FileReader("overlayj.ini")) {
            conf.read(fileReader);
        } catch (IOException ex) {
            System.err.println("Could not read/find overlayj.ini, using defaults ...");
            System.err.println(ex.getMessage());
        } catch (ConfigurationException ex) {
            System.err.println("There was a problem understanding overlayj.ini, aborting ...");
            System.err.println(ex.getMessage());
            System.exit(1);
        } finally {
            hideOnADS = conf.getBoolean("hide_on_ads", false);
            edgeLength = conf.getInt("edge_length", 28);
            thickness = conf.getInt("thickness", 2);

            center = edgeLength / 2;
            offset = center - thickness / 2;
        }
    }

    void enableSystemTray() {
        MenuItem action = new MenuItem("Quit");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        PopupMenu trayPopupMenu = new PopupMenu();
        trayPopupMenu.add(action);

        URL imageURL = getClass().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
        TrayIcon trayIcon = new TrayIcon(image, "OverlayJ", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    Dimension calcDimension() {
        return new Dimension(edgeLength, edgeLength);
    }

    Point calcLocation() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        return new Point(dm.getWidth() / 2 - edgeLength / 2, dm.getHeight() / 2 - edgeLength / 2);
    }

    void enableWinTransparency() {
        HWND hwnd = new HWND();
        hwnd.setPointer(Native.getComponentPointer(this));

        int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
        wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;

        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        paintCross(graphics);
        paintDot(graphics);
    }

    private void paintDot(Graphics graphics) {
        graphics.setColor(Color.CYAN);

        // center, rectangle
        graphics.fillRect(offset, offset, thickness, thickness);

        // center, open circle
        graphics.drawOval(offset - 1, offset - 1, 3, 3);
    }

    private void paintCross(Graphics graphics) {
        graphics.setColor(Color.red);

        // left
        graphics.fillRect(0, offset, center - 6, thickness);

        // right
        graphics.fillRect(center + 6, offset, center - 6, thickness);

        // top
        // g.fillRect(y, 0, thinkness, center - 4);

        // bottom
        graphics.fillRect(offset, center + 6, thickness, center - 6);
    }

    public void nativeMousePressed(NativeMouseEvent evt) {
        if (!hideOnADS || evt.getButton() != MouseEvent.BUTTON2)
            return;
        setVisible(false);
    }

    public void nativeMouseReleased(NativeMouseEvent evt) {
        if (!hideOnADS || evt.getButton() != MouseEvent.BUTTON2)
            return;
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Overlay overlay = new Overlay();
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(overlay);
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
    }
}
