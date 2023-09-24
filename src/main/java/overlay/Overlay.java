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

public class Overlay extends Window {

    int edgeLength = 28;
    int thinkness = 2;

    public Overlay() {
        super(null);

        setAlwaysOnTop(true);
        setLocation(calcLocation());
        setSize(calcDimension());
        setTransparent();

        setVisible(true);

        try {
            sysTray();
        } catch (AWTException e) {
            e.printStackTrace();
        }
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
        // g.drawOval(offset - 2, offset - 2, 4, 4);
        g.drawOval(offset - 1, offset - 1, 3, 3);
    }

    void sysTray() throws AWTException {
        SystemTray systemTray = SystemTray.getSystemTray();

        URL imageURL = getClass().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);

        PopupMenu trayPopupMenu = new PopupMenu();

        // 1t menuitem for popupmenu
        MenuItem action = new MenuItem("Quit");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(action);

        // setting tray icon
        TrayIcon trayIcon = new TrayIcon(image, "OverlayJ", trayPopupMenu);
        // adjust to default size as per system recommendation
        trayIcon.setImageAutoSize(true);

        systemTray.add(trayIcon);
    }

    void setTransparent() {
        Color transparent = new Color(127, 0, 0, 0);
        setBackground(transparent);
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

    public static void main(String[] args) {
        new Overlay();
    }
}
