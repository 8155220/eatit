package uagrm.androideatit.Model;

import java.util.List;

/**
 * Created by Shep on 10/29/2017.
 */

public class MyResponse {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;

    @Override
    public String toString() {
        return "MyResponse{" +
                "multicast_id=" + multicast_id +
                ", success=" + success +
                ", failure=" + failure +
                ", canonical_ids=" + canonical_ids +
                ", results=" + results +
                '}';
    }
}
