package com.ph.greenkorthaidictionary.network.util;

/**
 * Created by preparkha on 15. 6. 10..
 */
public class NetworkConstantUtil {

    public interface NETWORK {
        int START = 100;
        int RESULT = 101;
        int ERROR = 102;
    }

    public interface RESULT_CODE {
        int SUCCESS = 200;
        int EMPTY_PARAMETER_THAT_IS_ESSENTIAL = 100;
        int ERROR_OF_CONNENTING_WITH_DB_OR_EXECUTE_QUERY = 101;
    }

    public interface API_IDX {
        int _1_GET_SERVICE_INFORMATION = 1;
    }

    public interface API_URL {
        String HOSTS_URL = "http://hyojoong2.cafe24.com/hante";

        String _1_GET_SERVICE_INFORMATION = HOSTS_URL + "/mobile/selectServiceInfo.php";

    }

    public interface KEY_HANDLER_ERROR {
        // handler error
        String HANDLER_HAS_MESSAGE = "HANDLER";
    }

    public interface MSG_HANDLER {
        // success
        String NETWORK_IS_STARTED = "Success, Network is started";
        String NETWORK_SHOWS_RESULT = "Success, Network shows result";

        // fail
        String _1_CONTEXT_IS_NULL = "Context is null because you don't set context";
        String _2_NETWORK_LISTENER_IS_NULL = "DatabaseListener is null because you don't set DatabaseListener";
    }

    public interface MSG_RESULT_CODE {
        //common
        String RESULT_CODE_IS_EMPTY = "Result_code is not in network response.";

        // login
        String LOGIN_IS_SUCCESSED = "로그인 성공";
        String USER_INFO_IS_NOT_IN_SERVER = "등록되지 않은 학번입니다.";
        String USER_PASSWORD_IS_NOT_MATCHED = "비밀번호가 맞지 않습니다.";
        String USER_DOES_NOT_USER_APP_AT_THE_SAME_TIME = "다른 사용자가 사용하고 있습니다.";

        String INQUIRE_IS_SUCCESSED = "조회되었습니다.";
    }

}
