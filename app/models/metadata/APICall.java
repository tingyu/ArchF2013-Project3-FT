package metadata;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;

import com.fasterxml.jackson.databind.JsonNode;

public class APICall {

	public static JsonNode callAPI(String apiString) {

		Promise<WS.Response> responsePromise = WS.url(apiString).get();
		final Promise<JsonNode> bodyPromise = responsePromise
				.map(new Function<WS.Response, JsonNode>() {
					@Override
					public JsonNode apply(WS.Response response)
							throws Throwable {
						return response.asJson();// assumed you checked the
													// response code for 200
					}
				});

		return bodyPromise.get(play.mvc.Http.Status.REQUEST_TIMEOUT);
	}
}