package cn.goal.goal.services;

import cn.goal.goal.services.object.Analyse;
import cn.goal.goal.services.util.TypeTransfer;
import cn.goal.goal.utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenlin on 25/02/2017.
 */
public class AnalyseService {
    public static final String apiServer = Config.apiServer;
    public static final String analyseUrl = Config.analyseUrl;

    /**
     * 获取本月实时分析数据
     * @return 失败返回null;
     */
    public static Analyse getAnalyseOfCurrentMonth() {
        HttpRequest request = HttpRequest
                .get(apiServer + analyseUrl)
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    JSONObject data = result.getJSONObject("data");
                    return new Analyse(
                            TypeTransfer.getGoalsFinishedRecordArrayFromJSON(data.getJSONArray("goalsFinishedRecord")),
                            data.getJSONArray("goalsCreated").length(),
                            data.getJSONArray("goalsDoing").length(),
                            data.getJSONArray("goalsFinished").length(),
                            data.getJSONArray("goalsUnfinished").length(),
                            FocusTimeService.getFocusTime()
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
