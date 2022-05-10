package silkroad.utilities;

import java.util.Date;

public class TimeManager {

    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    public static Date toDate(Long milliseconds) {
        return new Date(milliseconds);
    }

}
