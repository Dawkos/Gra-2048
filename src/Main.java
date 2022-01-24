import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

public class Main extends JPanel {
    enum State {
        start, won, running, over
    }
    final Color[] colorTable = {
            new Color(0x701710), new Color(0xFFE4C3), new Color(0xfff4d3),
            new Color(0xffdac3), new Color(0xe7b08e), new Color(0xe7bf8e),
            new Color(0xffc4c3), new Color(0xE7948e), new Color(0xbe7e56),
            new Color(0xbe5e56), new Color(0x9c3931), new Color(0x701710)};
    //final static int target = 2048;
    static int highest;
    static int score;
    private final Color gridColor = new Color(0xBBADA0);
    private final Color emptyColor = new Color(0xCDC1B4);
    private final Color startColor = new Color(0xFFEBCD);
    private final Random rand = new Random();
    private Tile[][] tiles;
    private final int side = 4;
    private State GameState = State.start;
    private boolean checkingAvailableMoves;

    public Main() throws ParserConfigurationException, IOException, SAXException {
        setPreferredSize(new Dimension(1920, 1080));
        setBackground(new Color(0x625656));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
        GameScore.setScore(GameScore.getScoreXML());


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
                    case KeyEvent.VK_UP -> moveUp();
                    case KeyEvent.VK_DOWN -> moveDown();
                    case KeyEvent.VK_LEFT -> moveLeft();
                    case KeyEvent.VK_RIGHT -> moveRight();
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
        try {
            drawGrid(g);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    void startGame() {
        if (GameState != State.running) {
            score = 0;
            highest = 0;
            GameState = State.running;
            tiles = new Tile[side][side];
            addRandomTile();
            addRandomTile();
            add2048Tile();
        }
    }
    void drawGrid(Graphics2D g) throws ParserConfigurationException, IOException, SAXException {
        g.setColor(gridColor);
        g.fillRoundRect(401, 25, 619, 619, 15, 15);
        if (GameState == State.running) {
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(emptyColor);
                        g.fillRoundRect(415 + c * 121, 40 + r * 121, 106, 106, 7, 7);
                    } else {
                        drawTile(g, r, c);
                    }
                }
            }
        } else {
            g.setColor(startColor);
            g.fillRoundRect(470, 115, 469, 469, 7, 7);
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 128));
            g.drawString("2048", 570, 270);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            if (GameState == State.won) {
                g.drawString("Wygrałeś!", 590, 350);
            } else if (GameState == State.over)
                g.drawString("Przegrałeś", 590, 350);
            g.setColor(gridColor);
            g.drawString("Kliknij aby zagrać", 570, 470);
            g.drawString("(używaj strzałek do sterowania)", 570, 530);
        }
        g.setColor(new Color(0xBBADA0));
        g.setFont(new Font("SansSerif", Font.BOLD, 25));
        g.drawString("Wynik: " + score, 175, 165);
        g.drawString("Najwyższy wynik: " + GameScore.getScoreXML().getScore(), 105, 480);
    }
    void drawTile(Graphics2D g, int r, int c) {
        int value = tiles[r][c].getValue();
        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);
        g.fillRoundRect(415 + c * 121, 40 + r * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);
        g.setColor(value < 128 ? colorTable[0] : colorTable[1]);
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
        int x = 415 + c * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 40 + r * 121 + (asc + (106 - (asc + dec)) / 2);
        g.drawString(s, x, y);
    }
    private void addRandomTile() {
        int pos = rand.nextInt(side * side);
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
        int val = rand.nextInt(10) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }
    private void add2048Tile() {
        int pos = rand.nextInt(side * side);
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
        int val = 1024;
        tiles[row][col] = new Tile(val);
    }
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
            int r = j / side;
            int c = j % side;
            if (tiles[r][c] == null)
                continue;
            int nextR = r + yIncr;
            int nextC = c + xIncr;
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];
                if (next == null) {
                    if (checkingAvailableMoves)
                        return true;
                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
                } else if (next.canMergeWith(curr)) {
                    if (checkingAvailableMoves)
                        return true;
                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
        if (moved) {
//            if (highest < target) {
//                clearMerged();
//                addRandomTile();
//                if (!movesAvailable()) {
//                    GameState = State.over;
//                }
//            } else if (highest == target)
//                GameState = State.won;
            clearMerged();
            addRandomTile();
            if (!movesAvailable()) {
                GameState = State.over;
              }
        }
        if(score > GameScore.getScore().getScore()){
            try {
                GameScore.setScoreXML(new BestScore(score));
            } catch (ParserConfigurationException | SAXException
                    | IOException | TransformerException e1) {
                e1.printStackTrace();
            }
        }
        return moved;
    }
    boolean moveUp() {
        return move(0, -1, 0);
    }
    boolean moveDown() {
        return move(side * side - 1, 1, 0);
    }
    boolean moveLeft() {
        return move(0, 0, -1);
    }
    boolean moveRight() {
        return move(side * side - 1, 0, 1);
    }
    void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }
    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            try {
                f.add(new Main(), BorderLayout.CENTER);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
