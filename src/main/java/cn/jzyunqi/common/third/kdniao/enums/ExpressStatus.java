package cn.jzyunqi.common.third.kdniao.enums;

import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/6/5.
 */
@Getter
@AllArgsConstructor
public enum ExpressStatus {

    /**
     * nothing
     */
    N("无轨迹"),

    /**
     * on the way
     */
    O("在途中"),

    /**
     * sign
     */
    S("签收"),

    /**
     * problem
     */
    P("问题件"),;

    /**
     * 描述
     */
    private final String desc;

    public static ExpressStatus transfer(String state) {
        if (StringUtilPlus.isNotEmpty(state)) {
            switch (state) {
                case "0":
                    return ExpressStatus.N;
                case "2":
                    return ExpressStatus.O;
                case "3":
                    return ExpressStatus.S;
                case "4":
                    return ExpressStatus.P;
                default:
                    break;
            }
        }
        return ExpressStatus.P;
    }
}
