package model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import play.libs.Json;
import play.mvc.Http;
import utils.Constants;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class PaginatedAuctionListResponse {

    public static final String QUERY_PARAM_START = "start";
    private List<DisplayAuction> auctions;
    private String next;

    public static class Builder {
        public static PaginatedAuctionListResponse from(List<Auction> auctionList, int start,
                                                        int count, Http.Request request) {
            List<DisplayAuction> displayAuctions = auctionList.stream()
                    .map(DisplayAuction::new).collect(Collectors.toList());

            // setting next page url when items are available
            String nextPageURI = null;
            if (auctionList.size() >= count) {
                URIBuilder uriBuilder = null;
                try {
                    uriBuilder = new URIBuilder(request.path()).setHost(request.host()).setScheme(Constants.URL_SCHEME);
                } catch (URISyntaxException e) {
                }
                for(Map.Entry<String, String[]> param : request.queryString().entrySet()) {
                    if(!param.getKey().equals(QUERY_PARAM_START)) {
                        uriBuilder.addParameter(param.getKey(), param.getValue()[0]);
                    }
                }
                Objects.requireNonNull(uriBuilder);
                uriBuilder.addParameter(QUERY_PARAM_START, String.valueOf(start + count));
                nextPageURI = uriBuilder.toString();
                log.debug("Generated next page url :" + nextPageURI);
            }
            return new PaginatedAuctionListResponse(displayAuctions, nextPageURI);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("next", next);
        JSONArray items = new JSONArray();
        for (DisplayAuction auction : auctions) {
            items.put(new JSONObject(Json.toJson(auction).toString()));
        }
        jsonObject.put("auctions", items);
        return jsonObject;
    }

}
