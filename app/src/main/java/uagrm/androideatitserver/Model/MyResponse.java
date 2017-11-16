package uagrm.androideatitserver.Model;

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
}
