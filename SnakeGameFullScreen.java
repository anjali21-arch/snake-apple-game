import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGameFullScreen extends JPanel implements ActionListener, KeyListener {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;

    int unit = 25;
    int gameUnits = (width * height) / (unit * unit);

    int delay = 100;

    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];

    int bodyParts = 3;
    int applesEaten;

    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    JButton restartButton;
    JButton exitButton;

    SnakeGameFullScreen() {

        random = new Random();

        this.setPreferredSize(screenSize);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);

        setLayout(null);

        restartButton = new JButton("Restart");
        restartButton.setBounds(width/2 - 120, height/2 + 100, 110, 50);
        restartButton.setFont(new Font("Arial", Font.BOLD, 18));
        restartButton.setVisible(false);

        restartButton.addActionListener(e -> {
            restartButton.setVisible(false);
            exitButton.setVisible(false);
            startGame();
        });

        exitButton = new JButton("Exit");
        exitButton.setBounds(width/2 + 10, height/2 + 100, 110, 50);
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setVisible(false);

        exitButton.addActionListener(e -> System.exit(0));

        add(restartButton);
        add(exitButton);

        startGame();
    }

    public void startGame() {

        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';

        for(int i=0;i<gameUnits;i++){
            x[i] = 0;
            y[i] = 0;
        }

        x[0] = (width/2/unit)*unit;
        y[0] = (height/2/unit)*unit;

        newApple();

        running = true;

        timer = new Timer(delay, this);
        timer.start();

        requestFocusInWindow();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unit, unit);

            for (int i = 0; i < bodyParts; i++) {

                if (i == 0)
                    g.setColor(Color.green);
                else
                    g.setColor(new Color(45,180,0));

                g.fillRect(x[i], y[i], unit, unit);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + applesEaten, 40, 40);

        } else {

            gameOver(g);
        }
    }

    public void newApple() {

        appleX = random.nextInt(width/unit) * unit;
        appleY = random.nextInt(height/unit) * unit;
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U': y[0] -= unit; break;
            case 'D': y[0] += unit; break;
            case 'L': x[0] -= unit; break;
            case 'R': x[0] += unit; break;
        }
    }

    public void checkApple() {

        if (x[0] == appleX && y[0] == appleY) {

            bodyParts++;
            applesEaten++;

            newApple();
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i])
                running = false;
        }

        if (x[0] < 0 || x[0] >= width || y[0] < 0 || y[0] >= height)
            running = false;

        if (!running) {

            timer.stop();

            restartButton.setVisible(true);
            exitButton.setVisible(true);
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        g.drawString("GAME OVER", width/2 - 220, height/2 - 60);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Score: " + applesEaten, width/2 - 90, height/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;

            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;

            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;

            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");

        SnakeGameFullScreen gamePanel = new SnakeGameFullScreen();

        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setVisible(true);
    }
}