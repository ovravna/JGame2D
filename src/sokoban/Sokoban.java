package sokoban;

import game.Game;
import game.GameListener;
import game.entities.Player;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.*;

public class Sokoban implements GameListener {
    Game game;
    Player player;
    Screen screen;
    Level level;

    public Sokoban() {
        game = new Game("Sokoban",this);
//        new Lantern(level, "lantern", 10, 50, Light.SOFT);
//        new Wall(level, 50, 50);
        new Box(level, 10, 50);
        new Goal(level, 40, 50);

        new Box(level, 10, 80);
        new Goal(level, 40, 80);

        new Lantern(level, 20, 10);

//        new Box(level, "box", 40, 40);
//        new Ball(level, "Ball", player.x, player.y);
        game.setLighting(true);
        game.setDaylightCycle(false);
        game.setCycleTime(1);
        game.setLight(0);

        game.start();
    }


    public static void main(String[] args) {
        new Sokoban();
    }

    @Override
    public void newScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void newLevel(Level level) {
        this.level = level;
    }

    @Override
    public void newPlayer(Player player) {
        this.player = player;
    }
}
