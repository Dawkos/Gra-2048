import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Main extends JPanel {
    enum State {
        start, wygrana, uruchomiona, koniec
    }

    final Color[] colorTable = {
            new Color(0x701710), new Color(0xFFE4C3), new Color(0xfff4d3),
            new Color(0xffdac3), new Color(0xe7b08e), new Color(0xe7bf8e),
            new Color(0xffc4c3), new Color(0xE7948e), new Color(0xbe7e56),
            new Color(0xbe5e56), new Color(0x9c3931), new Color(0x701710)};
    final static int target = 2048;
    static int highest;
    static int score;
    private Color gridColor = new Color(0xBBADA0);
    private Color emptyColor = new Color(0xCDC1B4);
    private Color startColor = new Color(0xFFEBCD);
    private Random rand = new Random();
    private Tile[][] tiles;
    //rozmiar planszy
    private int side = 4;
    private State gamestate = State.start;
    private boolean checkingAvailableMoves;

    public Main() {
        setPreferredSize(new Dimension(900, 700));
        setBackground(new Color(0x625656));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startGame();
                repaint();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        //moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        //moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        //moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        //moveRight();
                        break;
                }
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g);
    }

    void startGame() {
        if (gamestate != State.uruchomiona) {
            score = 0;
            highest = 0;
            gamestate = State.uruchomiona;
            tiles = new Tile[side][side];
            //addRandomTile();
            //addRandomTile();
        }
    }

    void drawGrid(Graphics2D g) {
        g.setColor(gridColor);
        g.fillRoundRect(200, 100, 499, 499, 15, 15);
        if (gamestate == State.uruchomiona) {
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(emptyColor);
                        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
                    } else {
                        //drawTile(g, r, c);
                    }
                }
            }
        } else {
            g.setColor(startColor);
            g.fillRoundRect(215, 115, 469, 469, 7, 7);
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 128));
            g.drawString("2048", 310, 270);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            if (gamestate == State.wygrana) {
                g.drawString("Wygrałeś!", 390, 350);
            } else if (gamestate == State.koniec)
                g.drawString("Przegrałeś", 400, 350);
            g.setColor(gridColor);
            g.drawString("Kliknij by zagrać", 330, 470);
            g.drawString("(Używaj strzałek do sterowania)", 310, 530);
        }
    }
}