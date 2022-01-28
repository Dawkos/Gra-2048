import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Main extends JPanel {
    enum State {
        start, running, over
    }
    final Color[] colorTable = {
            new Color(0x701710), new Color(0xFFE4C3), new Color(0xfff4d3),
            new Color(0xffdac3), new Color(0xe7b08e), new Color(0xe7bf8e),
            new Color(0xffc4c3), new Color(0xE7948e), new Color(0xbe7e56),
            new Color(0xbe5e56), new Color(0x9c3931), new Color(0x701710),
            new Color(0xC1B222), new Color(0xDC5D14), new Color(0xDC1414)};
    final Color[] valueColor = {
            new Color(0x776E65), new Color(0xF9F6F2)
    };
    static int score;
    private final Color gridColor = new Color(0xBBADA0);
    private final Color emptyColor = new Color(0xCDC1B4);
    private final Color startColor = new Color(0xFFEBCD);
    private final Random rand = new Random();
    private Tile[][] tiles;
    private final int side = 4;
    private State GameState = State.start;
    String path = System.getProperty("user.dir")+"\\load.txt";
    private boolean checkingAvailableMoves;

    public Main() throws ParserConfigurationException, IOException, SAXException {

        setPreferredSize(new Dimension(425, 525));
        setBackground(new Color(0xFAF8EF));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
        GameScore.setScore(GameScore.getScoreXML());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }

                if (e.getKeyCode() == KeyEvent.VK_L) {
                    try {
                        loadGame();
                        JOptionPane.showMessageDialog(null, "Wczytano gre", "2048", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Problem z wczytaniem gry", "2048", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_S) {
                    try {
                        saveGame();
                        JOptionPane.showMessageDialog(null, "Zapisano gre", "2048", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Problem z zapisaniem gry", "2048", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
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
            GameState = State.running;
            tiles = new Tile[side][side];
            addRandomTile();
            addRandomTile();
        }
    }
    void restartGame() {
        if (GameState == State.running) {
            tiles = new Tile[side][side];
            addRandomTile();
            addRandomTile();
            score = 0;
            JOptionPane.showMessageDialog(null, "Zresetowano gre", "2048", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    void loadGame() throws IOException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        GameState = State.running;
        tiles = new Tile[side][side];
        int counter = 0;
        int load_value;
        //ustalamy kafelki do wczytania
        for (int row=0; row<side; row++) {
            for (int col=0; col<side; col++) {
                if(sc.hasNextInt() && counter<=side*side) {
                    load_value = sc.nextInt();
                    if (load_value == 0)
                        continue;
                    tiles[row][col] = new Tile(load_value);
                    counter++;
                }
            }
        }
        sc.close();
        //wczytujemy wynik
        String load_score = Files.readAllLines(Paths.get(path)).get(4);
        score = Integer.parseInt(load_score);
    }
    void saveGame() throws IOException {
        FileWriter fw = new FileWriter(path);
        int value;
        //zapisujemy kafelki do pliku
        for (int row=0; row<side; row++) {
            for (int col=0; col<side; col++) {
                if (tiles[row][col] == null) {
                    fw.write(String.valueOf(0)+" ");
                } else {
                    value = tiles[row][col].getValue();
                    fw.write(String.valueOf(value)+" ");
                }
            }
            fw.write(System.getProperty("line.separator"));
        }
        //zapisanie wyniku do pliku
        fw.write(score +System.getProperty("line.separator"));
        fw.close();
    }
    void drawGrid(Graphics2D g) throws ParserConfigurationException, IOException, SAXException {
        g.setColor(gridColor);
        if (GameState == State.start || GameState == State.over) {
            g.fillRoundRect(12, 12, 400, 500, 20, 20);    //layout drugiej strony gdzie gramy
        }

        if (GameState == State.running) {
            g.fillRoundRect(12, 12, 400, 90, 20, 20);     //pierwsza strona
            g.fillRoundRect(12, 112, 400, 400, 20, 20);   //druga strona gdzie gramy

            g.setColor(valueColor[0]);
            g.fillRoundRect(18, 18, 100, 77, 15, 15);     //wynik
            g.fillRoundRect(130, 18, 100, 77, 15, 15);    //najlepszy wynik

            g.setColor(valueColor[1]);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            drawStringCenter(g, "Wynik", 100, 18, 44);
            drawStringCenter(g, "Najlepszy", 100, 130, 44);

            String s = String.valueOf(score);
            String best = String.valueOf(GameScore.getScoreXML().getScore());
            drawStringCenter(g, s, 100, 18, 68);
            drawStringCenter(g, best, 100, 130, 68);
            g.drawString("R - zrestartuj gre", 250, 44);
            g.drawString("S - zapisz gre", 250, 64);
            g.drawString("L - wczytaj gre", 250, 84);
            for (int row = 0; row<side; row++) {
                for (int col = 0; col<side; col++) {
                    if (tiles[row][col] == null) {
                        g.setColor(emptyColor);
                        g.fillRoundRect(20 + col * 98, 120 + row * 98, 90, 90, 10, 10);  //third layer
                    } else {
                        drawTile(g, row, col);
                    }
                }
            }
        } else {
            g.setColor(startColor);
            g.fillRoundRect(27, 27, 370, 470, 10, 10);  //third layer
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 100));
            drawStringCenter(g, "2048", 370, 27, 160);

            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.setColor(new Color(0x776E65));
            drawStringCenter(g, "Wybierz rozmiar planszy i wcisnij enter", 370, 27, 340);
            drawStringCenter(g, "Wcisnij L, aby wczytać ostatnią gre", 370, 27, 370);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawStringCenter(g, "Podczas gry używaj klawiszy strzałek", 370, 27, 460);
        }
    }
    private void drawStringCenter(Graphics2D g, String str, int width_rect, int xPos_rect, int yPos_string){
        int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        int startPos = (width_rect - stringLen) / 2;
        g.drawString(str, startPos + xPos_rect, yPos_string);
    }
    void drawTile(Graphics2D g, int row, int col) {
        int value = tiles[row][col].getValue();
        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);  //return log 2 of "value"
        g.fillRoundRect(20 + col * 98, 120 + row * 98, 90, 90, 10, 10);      //tile background
        String s = String.valueOf(value);
        g.setColor(value < 128 ? colorTable[0] : colorTable[1]);
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
        int x = 20 + col * 98 + (90 - fm.stringWidth(s)) / 2;
        int y = 120 + row * 98 + (asc + (90 - (asc + dec)) / 2);
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
        int val = rand.nextInt(10) == 0 ? 512 : 256;
        tiles[row][col] = new Tile(val);
    }

    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
            int row = j / side;
            int col = j % side;
            if (tiles[row][col] == null)
                continue;
            int nextR = row + yIncr;
            int nextC = col + xIncr;
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
                Tile next = tiles[nextR][nextC];
                Tile current = tiles[row][col];
                if (next == null) {
                    if (checkingAvailableMoves)
                        return true;
                    tiles[nextR][nextC] = current;
                    tiles[row][col] = null;
                    row = nextR;
                    col = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
                } else if (next.canMergeWith(current)) {
                    if (checkingAvailableMoves)
                        return true;
                    int value = next.mergeWith(current);

                    score += value;
                    tiles[row][col] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
        if (moved) {

            clearMerged();
            addRandomTile();
            if (!movesAvailable()) {
                GameState = State.over;
                JOptionPane.showMessageDialog(null, "Game Over", "2048", JOptionPane.INFORMATION_MESSAGE);
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
                    tile.setMerged();
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
