package sokoban.cells;

import game.entities.Mob;
import game.gfx.Colors;
import game.gfx.Light;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

import java.util.Arrays;

public class Box extends Mob {

    private int color = Colors.get(432, 543, 432, 000);
    private SpriteSheet sheet = new SpriteSheet("/wall16x16.png");
    private boolean renderLight;

    public Box(Level level, int x, int y) {
        super(level, "Box", x, y, 1);
        solid = true;
        pushable = true;
        dimentions = new int[]{15, 0, 16, 0};

    }


    @Override
    public void tick() {
        if (level.player == null) return;

        if (getEntity(x+8, y+8) instanceof Goal) {
            renderLight = true;
        } else renderLight = false;


    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, 5 + 5*8, 0, 1, 16, Arrays.asList(0xffffff));

        if (renderLight) {
            screen.renderRoundLight(x + 5, y + 8, 8, -0x66, Light.SOFT, this);
        } else screen.removeLightSource(this);


    }

    @Override
    public void isPushed(int x, int y) {
        move(x, y);

    }

}
