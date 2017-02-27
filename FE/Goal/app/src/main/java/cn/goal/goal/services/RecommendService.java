package cn.goal.goal.Services;

import cn.goal.goal.Services.object.Recommend;
import cn.goal.goal.Services.util.TypeTransfer;
import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenlin on 26/02/2017.
 */
public class RecommendService {
    private static final String apiServer = Config.apiServer;
    private static final String recommendUrl = Config.recommendUrl;

    private static ArrayList<Recommend> recommendArrayList;

    /**
     * 获取推荐列表
     * @return 失败返回null;
     */
    public static ArrayList<Recommend> getRecommend() {
        if (recommendArrayList != null) return recommendArrayList;
        return refreshAndGetRecommend();
    }

    /**
     * 刷新并获取推荐列表
     * @return 失败返回null;
     */
    public static ArrayList<Recommend> refreshAndGetRecommend() {
        HttpRequest request = HttpRequest
                .get(apiServer + recommendUrl)
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    recommendArrayList = TypeTransfer.getRecommendArrayFromJSON(result.getJSONArray("data"));
                    return recommendArrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void logout() {
        recommendArrayList = null;
    }
}
