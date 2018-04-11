package com.xujiaji.dmlib2;

/**
 * 弹幕的运动方向
 */
public enum Direction
{
    /**
     * 弹幕从右至左运动(默认方案)
     */
    RIGHT_LEFT(1),

    /**
     * 弹幕从左至右运动
     */
    LEFT_RIGHT(2),

    /**
     * 弹幕从上至下运动
     */
    UP_DOWN(3),

    /**
     * 弹幕从下至上运动
     */
    DOWN_UP(4);


    int value;

    Direction(int v)
    {
        value = v;
    }

    public static Direction getType(int value)
    {
        Direction type;
        switch (value)
        {
            case 1:
                type = Direction.RIGHT_LEFT;
                break;
            case 2:
                type = Direction.LEFT_RIGHT;
                break;
            case 3:
                type = Direction.UP_DOWN;
                break;
            case 4:
                type = Direction.DOWN_UP;
                break;
            default:
                type = Direction.RIGHT_LEFT;
                break;
        }

        return type;
    }
}
