package cn.jzyunqi.common.third.kdniao.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.kdniao.response.KdniaoETResult;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;

/**
 * @author wiiyaya
 * @date 2018/6/5.
 */
@Slf4j
public class KdniaoClient {

    private final String appId;

    private final String appSecret;

    private final RestTemplate restTemplate;

    public KdniaoClient(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.restTemplate = new RestTemplate();
    }

    public KdniaoETResult getTraces(String expressCode, String trackingNum) throws BusinessException {
        KdniaoETResult kdniaoETResult;
        try {
            String requestData = "{'OrderCode':'','ShipperCode':'" + expressCode + "','LogisticCode':'" + trackingNum + "'}";
            String dataSign = DigestUtilPlus.MD5.sign(requestData + appSecret, Boolean.TRUE);

            URI orderTracesUri = new URIBuilder("http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx")
                    .addParameter("RequestData", URLEncoder.encode(requestData, StringUtilPlus.UTF_8))
                    .addParameter("EBusinessID", appId)
                    .addParameter("RequestType", "1002")
                    .addParameter("DataSign", URLEncoder.encode(dataSign, StringUtilPlus.UTF_8))
                    .addParameter("DataType", "2")
                    .build();


            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            RequestEntity<String> requestEntity = new RequestEntity<>(header, HttpMethod.POST, orderTracesUri);
            ResponseEntity<KdniaoETResult> groupRst = restTemplate.exchange(requestEntity, KdniaoETResult.class);

            kdniaoETResult = groupRst.getBody();
        } catch (Exception e) {
            log.error("======ExpressHelper.getKdniaoExpressTraces other error", e);
            return new KdniaoETResult();
        }

        if (kdniaoETResult != null && kdniaoETResult.getSuccess()) {
            return kdniaoETResult;
        } else {
            if (kdniaoETResult == null) {
                kdniaoETResult = new KdniaoETResult();
            }
            log.error("======ExpressHelper.getKdniaoExpressTraces 200 error[{}]", kdniaoETResult.getReason());
            return new KdniaoETResult();
        }
    }

}
