package cn.jzyunqi.common.third.kdniao;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.express.ExpressHelper;
import cn.jzyunqi.common.feature.express.TraceDto;
import cn.jzyunqi.common.feature.express.TraceItemDto;
import cn.jzyunqi.common.third.kdniao.client.KdniaoClient;
import cn.jzyunqi.common.third.kdniao.enums.ExpressStatus;
import cn.jzyunqi.common.third.kdniao.response.KdniaoETResult;

/**
 * @author wiiyaya
 * @date 2024/3/8
 */
public class KdniaoExpressStrange implements ExpressHelper {

    private final KdniaoClient kdniaoClient;

    public KdniaoExpressStrange(KdniaoClient kdniaoClient){
        this.kdniaoClient = kdniaoClient;
    }

    @Override
    public TraceDto getExpressTraces(String expressCode, String trackingNo) throws BusinessException {
        KdniaoETResult traceResult = kdniaoClient.getTraces(expressCode, trackingNo);

        TraceDto traceDto = new TraceDto();
        traceDto.setExpressName(expressCode);
        traceDto.setExpressLogo(null);
        traceDto.setExpressPhone(null);
        traceDto.setTrackingNo(traceResult.getLogisticCode());
        traceDto.setStatus(ExpressStatus.transfer(traceResult.getState()).getDesc());
        traceDto.setTraceList(traceResult.getTraces().stream().map(traceInfo -> {
            TraceItemDto traceItemDto = new TraceItemDto();
            traceItemDto.setTimeStamp(traceInfo.getAcceptTime().toString());
            traceItemDto.setScanLocation(traceInfo.getAcceptStation());
            traceItemDto.setRemark(traceInfo.getRemark());
            return traceItemDto;
        }).toList());
        traceDto.setTraceUrl(null);
        return traceDto;
    }
}
