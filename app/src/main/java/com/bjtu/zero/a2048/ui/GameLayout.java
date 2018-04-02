package com.bjtu.zero.a2048.ui;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.bjtu.zero.a2048.Setting;
import com.bjtu.zero.a2048.core.Block;
import com.bjtu.zero.a2048.core.BlockChangeList;
import com.bjtu.zero.a2048.core.BlockChangeListItem;
import com.bjtu.zero.a2048.core.Board;
import com.bjtu.zero.a2048.core.GamePresenter;

import java.io.Serializable;

/**
 * 主要游戏界面。
 * 管理BlockView的显示。
 *
 * @author Infinity
 */
public class GameLayout extends FrameLayout implements Serializable {

    public int blockWidth;
    public int blockHeight;
    public int[][] centerX;
    public int[][] centerY;
    private int size;
    private BlockView[][] viewGrid;

    public GameLayout(Context context) throws NoSuchMethodException {
        super(context);
        throw new NoSuchMethodException();
    }

    public GameLayout(Context context, int width, int height, GamePresenter gamePresenter) {
        this(context, width, height, gamePresenter, Setting.Runtime.BOARD_SIZE);
    }

    public GameLayout(Context context, int width, int height, GamePresenter gamePresenter, int size) {
        super(context);
        this.size = size;
        Log.e("sssss", " " + 1);
        viewGrid = new BlockView[size][size];
        Log.e("sssss", " " + 2);
        setLayoutParams(new LayoutParams(width, height));
        Log.e("sssss", " " + 3);
        int boarder = (int) (width * Setting.UI.BOARD_BOARDER_PERCENT);
        Log.e("sssss", " " + 4);
        int boardWidth = width - boarder * 2;
        int boardHeight = height - boarder * 2;
        blockWidth = boardWidth / size;
        blockHeight = boardHeight / size;
        centerX = new int[size][size];
        centerY = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                centerX[i][j] = boarder + blockWidth * i + blockWidth / 2;
                centerY[i][j] = boarder + blockHeight * j + blockHeight / 2;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                addView(new BlockView(getContext(), new Block(),
                        centerX[i][j], centerY[i][j], blockWidth, blockHeight));
            }
        }
    }


    public void f(int width, int height, int size) {
        removeAllViews();
        //removeAllViewsInLayout();
        Log.e("sssss", " " + 1);
        viewGrid = new BlockView[size][size];
        Log.e("sssss", " " + 2);
        setLayoutParams(new LayoutParams(width, height));
        Log.e("sssss", " " + 3);
        int boarder = (int) (width * Setting.UI.BOARD_BOARDER_PERCENT);
        Log.e("sssss", " " + 4);
        int boardWidth = width - boarder * 2;
        int boardHeight = height - boarder * 2;
        blockWidth = boardWidth / size;
        blockHeight = boardHeight / size;
        centerX = new int[size][size];
        centerY = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                centerX[i][j] = boarder + blockWidth * i + blockWidth / 2;
                centerY[i][j] = boarder + blockHeight * j + blockHeight / 2;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                addView(new BlockView(getContext(), new Block(),
                        centerX[i][j], centerY[i][j], blockWidth, blockHeight));
            }
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBlock(int i, int j, Block block, boolean visible) {
        if (viewGrid[i][j] != null) {
            removeView(viewGrid[i][j]);
        }
        viewGrid[i][j] = new BlockView(getContext(), block,
                centerX[i][j], centerY[i][j], blockWidth, blockHeight);
        if (!visible) {
            viewGrid[i][j].setVisible(false);
        }
        addView(viewGrid[i][j]);
    }

    public void setBlock2(int i, int j) {
        if (viewGrid[i][j] != null) {
            Block block = viewGrid[i][j].getBlock();
            removeView(viewGrid[i][j]);
            viewGrid[i][j] = new BlockView(getContext(), block,
                    centerX[i][j], centerY[i][j], blockWidth, blockHeight);
            addView(viewGrid[i][j]);
        }
    }

    public void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (viewGrid[i][j] != null) {
                    removeView(viewGrid[i][j]);
                }
            }
        }
    }

    public void setBoard(Board board) {
        if (size != board.size()) {
            throw new AssertionError();
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setBlock(i, j, board.getBlock(i, j), true);
            }
        }
    }

    public void setBoard2() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setBlock2(i, j);
            }
        }
    }

    private Point findCoordinate(Block block) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (viewGrid[i][j] != null && viewGrid[i][j].getBlock() == block) {
                    return new Point(j, i); // deliberately transformation
                }
            }
        }
        return null;
    }

    public void refresh() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (viewGrid[i][j] != null) {
                    viewGrid[i][j].setGeometry(centerX[i][j], centerY[i][j], blockWidth, blockHeight);
                    viewGrid[i][j].invalidate();
                }
            }
        }
    }

    public void playTranslation(BlockChangeList list, Animation.AnimationListener animationListener) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(Setting.Runtime.ANIMATION_DURATION_MILLISECONDS);
        for (BlockChangeListItem item : list) {
            Point p = findCoordinate(item.block);
            if (p != null) {
                int toX = centerX[item.toY][item.toX] - centerX[p.x][p.y];
                int toY = centerY[item.toY][item.toX] - centerY[p.x][p.y];
                Animation animation = new TranslateAnimation(0, toX, 0, toY);
                animation.setDuration(Setting.Runtime.ANIMATION_DURATION_MILLISECONDS);
                if (animationListener != null) {
                    animation.setAnimationListener(animationListener);
                    animationListener = null;
                }
                viewGrid[p.y][p.x].setAnimation(animation);
                animationSet.addAnimation(animation);
                Log.e("ANIMATION",
                        "Block r=" + item.block.getRank()
                                + ", lPos (" + p.y + ", " + p.x + ")"
                                + "->(" + item.toX + ", " + item.toY + "), "
                );
            }
        }
        animationSet.start();
    }

    public void playSpawn(int i, int j, Block block) {
        setBlock(i, j, block, false);
        Point p = findCoordinate(block);
        if (p != null) {
            Animation animation = new ScaleAnimation(
                    Setting.UI.BLOCK_SPAWN_SCALE_FROM_PERCENT, 1f,
                    Setting.UI.BLOCK_SPAWN_SCALE_FROM_PERCENT, 1f,
                    Animation.ABSOLUTE, centerX[p.x][p.y],
                    Animation.ABSOLUTE, centerY[p.x][p.y]
            );
            animation.setDuration(Setting.Runtime.ANIMATION_DURATION_MILLISECONDS);
            viewGrid[i][j].startAnimation(animation);
            viewGrid[i][j].setVisible();
        }
    }
}
