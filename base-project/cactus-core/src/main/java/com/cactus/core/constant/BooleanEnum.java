package com.cactus.core.constant;

/**
 * 用1、0代表true和false
 * @author lht
 * @since 2022/6/3 5:07 下午
 */
public enum BooleanEnum {
    /**
     * true 1
     */
    TRUE(1),
    /**
     * false 0
     */
    FALSE(0);

    private final int val;

    BooleanEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
