package game.gfx;

import game.Game;
import game.level.Lighting;

import java.util.Arrays;
import java.util.List;


public class Screen {

    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;

    public static final byte BIT_MIRROR_X = 0x1;
    public static final byte BIT_MIRROR_Y = 0x2;
    public static int filterColor;
    private boolean lightOn = true;

    public static List<Integer> defaultIgnoreColors = Arrays.asList(0xfa05f0);

    public int[] pixels;

    public int xOffset = 0;
    public int yOffset = 0;

    public int  width;
    public int height;

    public SpriteSheet sheet;

    private Lighting lighting;


    public Screen(int width, int height, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        this.pixels = new int[width*height];
//        this.light = new Integer[width*height];
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale) {
        render(xPos, yPos, tile, mirrorDir, scale, 8, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, int block) {
        render(xPos, yPos, tile, mirrorDir, scale, block, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, int block, List<Integer> ignoreColors) {
        render(xPos, yPos, this.sheet, tile, mirrorDir, scale, block, ignoreColors);
    }


    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, int block) {
        render(xPos, yPos, sheet, tile, mirrorDir, scale, block, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, List<Integer> ignoreColors) {
        render(xPos, yPos, sheet, tile, mirrorDir, scale, 8, ignoreColors);
    }


    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, int block, List<Integer> ignoreColors) {
//        filterColor = Game.getLight();
        int logBlock = log2(block);

        if (logBlock == -1) throw new RuntimeException("Invalid block");

        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        int scaleMap = scale-1;
        int xTile = tile%(sheet.width >> logBlock);
        int yTile = tile/(sheet.width >> logBlock);
        int tileOffset = (xTile << logBlock)+(yTile << logBlock)*sheet.width;


        for (int y = 0;y < block;y++) {
            int ySheet = y;
            if (mirrorY)
                ySheet = block-y-1;

            int yPixel = y+yPos+(y*scaleMap)-((scaleMap << logBlock)/2);

            for (int x = 0;x < block;x++) {
                int xSheet = x;
                if (mirrorX)
                    xSheet = block-x-1;
                int xPixel = x+xPos+(x*scaleMap)-((scaleMap << logBlock)/2);

                int col = sheet.pixels[xSheet+ySheet*sheet.width+tileOffset];


                if (!ignoreColors.contains(col)) {
                    for (int yScale = 0;yScale < scale;yScale++) {
                        if (yPixel+yScale < 0 || yPixel+yScale >= height)
                            continue;
                        for (int xScale = 0;xScale < scale;xScale++) {
                            if (xPixel+xScale < 0 || xPixel+xScale >= width)
                                continue;
                            pixels[(xPixel+xScale)+(yPixel+yScale)*width] =
                                    !lightOn ? col : colorSelector(col, lighting.lightCombiner((xPixel+xScale)+(yPixel+yScale)*width));
                        }
                    }
                }
            }
        }
    }


//    private Integer lightCombiner(int i) {
//        int r = -0xff;
//
//
//        Integer temp;
//
//        for (Integer[] light : lightSources.values()) {
//            temp = light[i];
//
//            if (temp == null) {
//                temp = filterColor;
//            }
//            r = temp > r ? temp:r;
//        }
//
//        if (r == -0xff) {
//            r = filterColor;
//        }
//
//        return r;
//    }


    public void setLighting(Lighting lighting) {
        System.out.println("Screen "+lighting);
        this.lighting = lighting;
    }

    public void setLightRendering(boolean lightOn) {
        this.lightOn = lightOn;
    }

    public boolean setFilter(long clock, int cycleSeconds, boolean rise, int maxFilter) {
        double time = 60*cycleSeconds;

        filterColor = (int) (Game.getLight()*(1-Math.sin(2*(clock/time))));

        if (rise) {
            return filterColor == maxFilter;
        } else
            return filterColor < Game.getLight();

    }


//    public void renderRoundLight(int x, int y, int radius, int filter, Entity this_entity) {
//        renderRoundLight(x, y, radius, filter, Light.SOFT, this_entity);
//    }
//
//    public void renderRoundLight(int x, int y, int radius, int filter, Light light, Entity this_entity) {
//        renderRoundLight(x, y, radius, filter, 2, 0, light, this_entity);
//    }
//
//    public void renderRoundLight(int x, int y, int radius, int filter, int xOff, int yOff, Entity this_entity) {
//        renderRoundLight(x, y, radius, filter, xOff, yOff, Light.SOFT, this_entity);
//    }
//
//    public void renderRoundLight(int x, int y, int radius, int filter, int xOffset, int yOffset, Light lighting, Entity this_entity) {
//        Integer[] light = new Integer[width*height];
//        boolean rgbFilter = false;
//
//        int radSqur = radius*radius;
//
//        x -= this.xOffset-xOffset;
//        y -= this.yOffset-yOffset;
//
//        double distance;
//
//        int a, r = 0, g = 0, b = 0;
//        if (Math.abs(filter)> 0xff) {
//            a = filter >> 24;
//            r = filter >> 16;
//            g = filter >> 8;
//            b = filter;
//            rgbFilter = true;
//
//        } else a = filter;
//
//
//
//
//        for (int xa = 0;xa < width;xa++) {
//            for (int ya = 0;ya < height;ya++) {
//                distance = Math.pow(xa-x, 2)+Math.pow(ya-y, 2);
//
////                if (distance < radSqur*0.01) {
////                    this.light[xa+ya*width] = filter;
////                } else
//
//                // TODO: 14.04.2016 ender kode for håndtering av filter > 0xff
//                if (distance < radSqur) {
//
//                    int shade = 1;
//
//                    if (lighting == Light.SOFT) {
//                        shade = ((int) ((filterColor*distance)-a*(radSqur-distance))/radSqur);
//                    } else if (lighting == Light.HARD) {
//                        shade = a;
//                    }
//
////                    System.out.println(temp.length);
//                    if (rgbFilter) {
//                        int f = (shade-a)/filterColor;
//
//
//
//                        light[xa+ya*width] = ((f*r) << 16)+((f*b) << 8)+(f*g);
//
//                    } else light[xa+ya*width] = shade;
//
//                } else
////                if (distance < radSqur*1.1)
//                    light[xa+ya*width] = filterColor;
//            }
//        }
//        lightSources.put(this_entity, light);
//    }




    private static int colorSelector(int color, Integer filter) {
        if (filter == null) return Screen.colorSelector(color, filterColor);
//        System.out.println(Integer.toHexString(filter));
        if (filter > Math.abs(0xff)) {
            int r = (filter/0x10000)%0x100;
            int g = (filter/0x100)%0x100;
            int b = filter%0x100;
//            System.out.printf("filter: %s %s %s\n", r, g, b);


            return colorSelector(color, r, g, b);
        }

        return colorSelector(color, filter, filter, filter);
    }

    private static int colorSelector(int color, int rFilter, int gFilter, int bFilter) {

        int r = (color/0x10000)%0x100;
        int g = (color/0x100)%0x100;
        int b = color%0x100;


//        System.out.printf("color: %s %s %s\n", r, g, b);

        List<Integer> rgb = Arrays.asList(r, g, b);
        List<Integer> filters = Arrays.asList(rFilter, gFilter, bFilter);

        for (int i = 0;i < 3;i++) {
            if (rgb.get(i) + filters.get(i) < 0) rgb.set(i, 0);
            else if (rgb.get(i) + filters.get(i) > 0xff) rgb.set(i, 0xff);
            else rgb.set(i, rgb.get(i)+filters.get(i));
        }



//        System.out.printf("%s %s %s\n",Integer.toHexString(r), Integer.toHexString(g), Integer.toHexString(b));
        return (rgb.get(0) << 16)+(rgb.get(1) << 8)+rgb.get(2);
    }

    private static int log2(int block) {
        for (int t = 1, x = 0; t <= block; t *= 2, x++) {
            if (t == block) {
                return x;
            }
        }
        return -1;

    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;


    }

    
}
















//    public int[] colors = new int[MAP_WIDTH*MAP_WIDTH*4];



//    public void render(int[] pixels, int offset, int row) {
//        for (int yTile = yOffset >> 3;yTile <= (yOffset+height) >> 3;yTile++) {
//            int yMin = yTile*8-yOffset;
//            int yMax = yMin+8;
//
//            if (yMin < 0) yMin = 0;
//            if (yMax > height) yMax = height;
//
//
//            for (int xTile = xOffset >> 3;xTile <= (xOffset+width) >> 3;xTile++) {
//                int xMin = xTile*8-xOffset;
//                int xMax = xMin+8;
//
//                if (xMin < 0) xMin = 0;
//                if (xMax > width) xMax = width;
//
//                int tileIndex = (xTile & MAP_WIDTH_MASK)+(yTile & MAP_WIDTH_MASK)*MAP_WIDTH;
//
//                for (int y = yMin;y < yMax;y++) {
//                    int sheetPixel = ((y+yOffset) & 7)*sheet.width+((xMin+xOffset) & 7);
//                    int tilePixel = offset+xMin+y*row;
//                    for (int x = xMin;x < xMax;x++) {
//                        int color = tileIndex*4+sheet.pixels[sheetPixel++];
//                        pixels[tilePixel++] = colors[color];
//                    }
//                }
//            }
//        }
//    }





















