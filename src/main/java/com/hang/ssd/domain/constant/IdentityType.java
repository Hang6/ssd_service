package com.hang.ssd.domain.constant;

/**
 * 身份类型
 * @author yinhang
 */
public enum IdentityType {
    //用户
    COMSUMER(1),

    //骑手
    RIDER(2);

    private int typeCode;

    IdentityType(int typeCode){
        this.typeCode = typeCode;
    }

    public int getTypeCode() {return typeCode;}

    public static IdentityType findByValue(int typeCode){
        switch (typeCode){
            case 1:
                return COMSUMER;
            case 2:
                return RIDER;
            default:
                return null;
        }
    }
}
