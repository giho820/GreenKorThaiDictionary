package com.ph.greenkorthaidictionary.util;

/**
 * Created by preparkha on 15. 6. 9..
 */
public class KorThaiDicConstantUtil {

    public interface TYPE_FRAG_MAIN {
        int LIST_ITEM = 0;
        int DETAIL_ITEM = 1;
        int UPDATE_LIST_ITEM = 2;
    }

    public interface TYPE_APPENDIX {
        int CONSONANT_VOWEL = 50;
        int INTONATION = 51;
        int INTONATION_PRAC = 52;
        int READ_WRITE_KOR = 53;
    }

    public interface KEY_SHARED_PREFERENCE {
        String USER_ID = "USER_ID";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String USER_TYPE = "USER_TYPE";
        String CHECK_LICENSE = "CHECK_LICENSE";
        String CHECK_DATABASE = "CHECK_DATABASE";
    }

    public interface MSG_INTERNAL_ERROR {

        String _1_CONTEXT_IS_NULL = "Context is null because you don't set context";
        String _2_API_IDX_AND_URL_IS_NULL = "API_idx and URL is null because you don't set API_idx and URL";
        String _3_NETWORK_LISTENER_IS_NULL = "NetworkListener is null because you don't set NetworkListener";
        String _4_COMMON_LOADING_DIG_IS_NULL = "Common loading dlglog is null because you don't set Common loading dlglog";
        String _5_ACTIVITY_IS_NULL = "Activity is null because you don't set activity";

    }

    public interface MSG_TOAST {

        String _1_USER_ID_AND_PASSWORD_ARE_NULL = "아이디와 비밀번호를 입력해주세요.";
    }
}
