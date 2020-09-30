package model;

import akka.http.javadsl.model.Uri;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import play.Logger;
import play.libs.typedmap.TypedKey;
import play.mvc.Http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class PaginatedAuctionListResponse {

    private List<DisplayAuction> auctions;
    private String next;

    public static class Builder {
        public static PaginatedAuctionListResponse from(List<Auction> auctionList, int start,
                                                        int count, String requestURI) {
            List<DisplayAuction> displayAuctions = auctionList.stream()
                    .map(DisplayAuction::new).collect(Collectors.toList());
            URI uri = null;
            try {
                uri = new URI(requestURI);
            } catch (URISyntaxException ignored) {
            }
            Objects.requireNonNull(uri, "Malformed uri shared - " + requestURI);

            // setting next page url when items are available
            String nextPageURI = null;
            if (auctionList.size() >= count) {
                nextPageURI = new Http.RequestBuilder()
                        .uri(uri).attr(TypedKey.create("start"), start + count).build().uri();
                log.debug("Generated next page url :" + nextPageURI + ", for received current page - " + requestURI);
            }
            return new PaginatedAuctionListResponse(displayAuctions, nextPageURI);
        }
    }

}
