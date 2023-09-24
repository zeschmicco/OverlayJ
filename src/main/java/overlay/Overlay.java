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
import java.net.URL;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class Overlay extends Window {
    final Color transparent = new Color(0, 0, 0, 0);

    int edgeLength = 28;
    int thinkness = 2;

    public Overlay() {
        super(null);

        enableSystemTray();

        setAlwaysOnTop(true);
        setBackground(transparent);
        setLocation(calcLocation());
        setSize(calcDimension());

        setVisible(true);

        enableWinTransparency();
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
    public void paint(Graphics g) {
        super.paint(g);

        int center = edgeLength / 2;
        int offset = center - thinkness / 2;

        g.setColor(Color.red);

        // left
        g.fillRect(0, offset, center - 6, thinkness);

        // right
        g.fillRect(center + 6, offset, center - 6, thinkness);

        // top
        // g.fillRect(y, 0, thinkness, center - 4);

        // bottom
        g.fillRect(offset, center + 6, thinkness, center - 6);

        g.setColor(Color.CYAN);
        
        // center, rectangle
        g.fillRect(offset, offset, thinkness, thinkness);

        // center, open circle
        g.drawOval(offset - 1, offset - 1, 3, 3);
    }

    public static void main(String[] args) {
        new Overlay();
    }
}
